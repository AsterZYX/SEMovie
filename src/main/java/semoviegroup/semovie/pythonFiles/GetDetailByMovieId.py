from socket import *
from time import ctime
import requests
from bs4 import BeautifulSoup
from requests.exceptions import RequestException
import socket
import threading
import socketserver
import json, types, string
import os, time
from xml.dom.minidom import Document


def open_url(url):
    try:
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36'}
        response = requests.get(url, headers=headers)
        if response.status_code == 200:
            return response.text
        return None
    except RequestException:
        return None


def parse_page(html):
    soup = BeautifulSoup(html, 'lxml')

    directors = soup.find_all(class_='celebrity-group')[0]
    directorsname = directors.find_all(name='a', class_='name')
    directorsimg = directors.find_all(name='img')

    directorList = []

    length = len(directorsname)
    for i in range(0, length):
        di = {}
        s = directorsname[i].get_text().replace(' ', '')
        s = s.replace('\n', '')
        di['name'] = s
        di['img'] = directorsimg[i].attrs['data-src']
        di['identity'] = 'director'
        di['role'] = ''
        directorList.append(di)
    print(directorList)

    actors = soup.find_all(class_='celebrity-group')[1]
    actorsname = actors.find_all(name='a', class_='name')
    actorsimg = actors.find_all(name='img')
    actorsrole = actors.find_all(class_='role')

    actorList = []

    length = len(actorsname)
    for i in range(0, length):
        ac = {}
        s = actorsname[i].get_text().replace(' ', '')
        s = s.replace('\n', '')
        ac['name'] = s
        ac['img'] = actorsimg[i].attrs['data-src']
        ac['identity'] = 'actor'
        ss = actorsrole[i].get_text().replace(' ', '')
        ss = ss.replace('\n', '')
        ac['role'] = ss
        actorList.append(ac)
    print(actorList)
    pics = soup.find(class_='album clearfix')
    picss = pics.find_all(name='img')
    picList = []
    for i in picss:
        picList.append(i.attrs['data-src'])
    print(picList)
    writeInfoToXml(directorList, actorList, picList, 'Movie.xml')


def writeInfoToXml(directorList, actorList, picList, filename):
    # 创建dom文档
    doc = Document()

    # 创建根节点
    movie = doc.createElement('movie')
    # 根节点插入dom树
    doc.appendChild(movie)

    dl = doc.createElement('directorList')
    movie.appendChild(dl)
    for v in directorList:
        (name, img, role, identity) = (v['name'], v['img'], v['role'], v['identity'])
        di = doc.createElement('director')
        dl.appendChild(di)

        namee = doc.createElement('name')
        name_text = doc.createTextNode(name)
        namee.appendChild(name_text)
        di.appendChild(namee)

        idenn = doc.createElement('identity')
        iden_text = doc.createTextNode(identity)
        idenn.appendChild(iden_text)
        di.appendChild(idenn)

        imgg = doc.createElement('img')
        img_text = doc.createTextNode(img)
        imgg.appendChild(img_text)
        di.appendChild(imgg)

        rolee = doc.createElement('role')
        role_text = doc.createTextNode(role)
        rolee.appendChild(role_text)
        di.appendChild(rolee)

    dl = doc.createElement('actorList')
    movie.appendChild(dl)
    for v in actorList:
        (name, img, role, identity) = (v['name'], v['img'], v['role'], v['identity'])
        di = doc.createElement('actor')
        dl.appendChild(di)

        namee = doc.createElement('name')
        name_text = doc.createTextNode(name)
        namee.appendChild(name_text)
        di.appendChild(namee)

        idenn = doc.createElement('identity')
        iden_text = doc.createTextNode(identity)
        idenn.appendChild(iden_text)
        di.appendChild(idenn)

        imgg = doc.createElement('img')
        img_text = doc.createTextNode(img)
        imgg.appendChild(img_text)
        di.appendChild(imgg)

        rolee = doc.createElement('role')
        role_text = doc.createTextNode(role)
        rolee.appendChild(role_text)
        di.appendChild(rolee)

    dl = doc.createElement('picList')
    movie.appendChild(dl)
    for v in picList:
        di = doc.createElement('pic')
        dl.appendChild(di)
        imgg = doc.createElement('img')
        img_text = doc.createTextNode(v)
        imgg.appendChild(img_text)
        di.appendChild(imgg)

    try:
        with open(filename, 'w', encoding='UTF-8') as fh:
            # 4.writexml()第一个参数是目标文件对象，第二个参数是根节点的缩进格式，第三个参数是其他子节点的缩进格式，
            # 第四个参数制定了换行格式，第五个参数制定了xml内容的编码。
            doc.writexml(fh, indent='', addindent='\t', newl='\n', encoding='UTF-8')
            print('写入xml OK!')
    except Exception as err:
        print('错误信息：{0}'.format(err))


import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8002))
    print('bind socket succ!')

    sock.listen(5)
    print('listen succ!')

except:
    print("init socket error!")

while True:
    print("listen for client...")
    conn, addr = sock.accept()
    print("get client")
    print(addr)

    conn.settimeout(30)
    szBuf = conn.recv(1024)
    print("recv:" + str(szBuf, 'gbk'))
    base_url = str(szBuf, 'gbk')[8:-2]
    print(base_url)
    html = open_url(base_url)
    parse_page(html)

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"result saved at D:\PycharmProjects\SEmovie\Movie.xml")

    conn.close()
    print("end of servive")
