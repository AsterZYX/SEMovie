#-*- coding: utf-8 -*-
import re
import os
import json
import requests
from multiprocessing import Pool
from requests.exceptions import RequestException
from xml.dom.minidom import Document

import socket
import threading
import socketserver


def get_one_page(url):
    '''
    获取网页html内容并返回
    '''
    try:
        # 获取网页html内容
        response = requests.get(url)
        # 通过状态码判断是否获取成功
        if response.status_code == 200:
            return response.text
        return None
    except RequestException:
        return None

def parse_one_page(html):
    '''
    解析HTML代码，提取有用信息并返回
    '''
    # 正则表达式进行解析
    pattern = re.compile('<dd>.*?board-index.*?>(\d+)</i>.*?data-src="(.*?)".*?name">'
        + '<a.*?>(.*?)</a>.*?"star">(.*?)</p>.*?releasetime">(.*?)</p>'
        + '.*?integer">(.*?)</i>.*?fraction">(.*?)</i>.*?</dd>', re.S)
    # 匹配所有符合条件的内容
    items = re.findall(pattern, html)

    for item in items:
        yield {
            'index': item[0],
            'image': item[1],
            'title': item[2],
            'actor': item[3].strip()[3:],
            'time': item[4].strip()[5:],
            'score': item[5] + item[6]
        }


def save_image_file(url, path):
    '''
    保存电影封面
    '''
    ir = requests.get(url)
    if ir.status_code == 200:
        with open(path, 'wb') as f:
            f.write(ir.content)
            f.close()


def writeInfoToXml(scoreDict, filename):
    # 创建dom文档
    doc = Document()

    # 创建根节点
    scorelist = doc.createElement('scorelist')
    # 根节点插入dom树
    doc.appendChild(scorelist)

    # 依次将score中的每一组元素提取出来，创建对应节点并插入dom树
    for v in scoreDict:
        # 分离
        (index, image, title, actor, time, score) = (v['index'], v['image'], v['title'], v['actor'], v['time'], v['score'])
        #(index, image, title, actor, time, score) = (v[0], v[1], v[2], v[3], v[4], v[5])
        scores = doc.createElement('scores')
        scorelist.appendChild(scores)

        # 将排名插入<scores>中
        # 创建节点<index>
        maoyantitle = doc.createElement('maoyantitle')
        # 创建<index>下的文本节点
        maoyantitle_text = doc.createTextNode(index)
        # 将文本节点插入到<index>下
        maoyantitle.appendChild(maoyantitle_text)
        # 将<index>插入到父节点<scores>下
        scores.appendChild(maoyantitle)

        # 将图片地址插入<scores>中，处理同上
        imageUrl = doc.createElement('imageUrl')
        imageUrl_text = doc.createTextNode(image)
        imageUrl.appendChild(imageUrl_text)
        scores.appendChild(imageUrl)

        titlee = doc.createElement('title')
        title_text = doc.createTextNode(title)
        titlee.appendChild(title_text)
        scores.appendChild(titlee)

        actorr = doc.createElement('actor')
        actor_text = doc.createTextNode(actor)
        actorr.appendChild(actor_text)
        scores.appendChild(actorr)

        timee = doc.createElement('time')
        time_text = doc.createTextNode(time)
        timee.appendChild(time_text)
        scores.appendChild(timee)

        scoree = doc.createElement('score')
        score_text = doc.createTextNode(score)
        scoree.appendChild(score_text)
        scores.appendChild(scoree)

    try:
        with open(filename, 'w', encoding='UTF-8') as fh:
            doc.writexml(fh, indent='', addindent='\t', newl='\n', encoding='UTF-8')
            print('写入')
    except Exception as err:
        print('错误信息：{0}'.format(err))


def main(offset):
    url=str(offset)
    #url = 'http://maoyan.com/board/4?offset=' + str(offset)
    #url = 'https://maoyan.com/films?offset=' + str(offset)
    html = get_one_page(url)
    # 封面文件夹不存在则创建
    if not os.path.exists('covers'):
        os.mkdir('covers')

    list = []

    for item in parse_one_page(html):
        print(item)
        list.append(item)
    jresp = json.dumps(list)
    res = json.loads(jresp)
    writeInfoToXml(res, 'maoyanRating.xml')

#if __name__ == '__main__':
    # 使用多进程提高效率
    #pool = Pool()
    #pool.map(main, [i*10 for i in range(10)])

import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8006))
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
    main(base_url)

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"D:\PycharmProjects\SEmovie\maoyanRating.xml")

    conn.close()
    print("end of servive")