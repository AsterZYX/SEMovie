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
    # html = requests.get("https://maoyan.com/films?showType=3&catId=3&sourceId=2&yearId=13&sortId=1&offset=30")
    soup = BeautifulSoup(html, 'lxml')

    items = soup.select('dd')
    for item in items:
        poster = item.find_all(name='img')[1].attrs['data-src']
        title = item.find(name='div', class_='channel-detail movie-item-title').get_text().strip()
        rating = item.find(name='div', class_='channel-detail channel-detail-orange').get_text()
        movieid = item.find(name='a').attrs['data-val']
        # score = item.find(name='p', class_='score').get_text()
        yield {
            'poster': poster,
            'title': title,
            'rating': rating,
            'movieid': movieid[9:-1]
        }


# 将self.orderDict中的信息写入本地xml文件，参数filename是xml文件名
def writeInfoToXml(movieDict, filename):
    # 创建dom文档
    doc = Document()

    # 创建根节点
    movielist = doc.createElement('movielist')
    # 根节点插入dom树
    doc.appendChild(movielist)

    # 依次将orderDict中的每一组元素提取出来，创建对应节点并插入dom树
    for v in movieDict:
        # 分离出姓名，电话，地址，点餐次数
        (poster, movieid, title, rating) = (v['poster'], v['movieid'], v['title'], v['rating'])

        # 每一组信息先创建节点<movie>，然后插入到父节点<movielist>下
        movie = doc.createElement('movie')
        movielist.appendChild(movie)

        # 将电话插入<order>中，处理同上
        posterr = doc.createElement('poster')
        poster_text = doc.createTextNode(poster)
        posterr.appendChild(poster_text)
        movie.appendChild(posterr)

        # 将地址插入<order>中，处理同上
        titlee = doc.createElement('title')
        title_text = doc.createTextNode(title)
        titlee.appendChild(title_text)
        movie.appendChild(titlee)

        # 将点餐次数插入<order>中，处理同上
        ratingg = doc.createElement('maoyanrating')
        rating_text = doc.createTextNode(rating)
        ratingg.appendChild(rating_text)
        movie.appendChild(ratingg)

        # 将点餐次数插入<order>中，处理同上
        movieidd = doc.createElement('movieid')
        movieid_text = doc.createTextNode(movieid)
        movieidd.appendChild(movieid_text)
        movie.appendChild(movieidd)

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

    sock.bind(('localhost', 8001))
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
    result = parse_page(html)
    list = []
    for item in result:
        list.append(item)
    jresp = json.dumps(list)
    res = json.loads(jresp)

    writeInfoToXml(res, 'AbstractSearchMovies.xml')

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"result saved at D:\PycharmProjects\SEmovie\AbstractSearchMovies.xml")

    conn.close()
    print("end of servive")
