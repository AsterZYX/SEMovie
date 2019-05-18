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
import urllib.request
import pandas as pd
import urllib.parse
import requests
import re
import matplotlib.pyplot as plt
from imageio import imread
from lxml import html
from bs4 import BeautifulSoup
from bs4 import UnicodeDammit
import random
import time
import os


def find_ID(name):  # name即剧名
    try:
        url1 = 'https://movie.douban.com/j/subject_suggest?q='
        url2 = urllib.parse.quote(name)  # URL只允许一部分ASCII字符，其他字符（如汉字）是不符合标准的，此时就要进行编码。
        url = url1 + url2  # 生成针对该剧的链接，上面链接红字部分即为编码的name
        html = requests.get(url)  # 访问链接，获取html页面的内容
        html = html.content.decode()  # 对html的内容解码为utf-8格式
        html_list = html.replace('\/', '/')  # 将html中的\/全部转换成/，只是为了看着方便（不换也行）
        html_list = html_list.split('},{')  # 将html页面中的每一个条目提取为列表的一个元素。

        # 定义正则，目的是从html中提取想要的信息（根据title提取id）
        str_title = '"title":"' + name + '"'  ##匹配剧名name
        pattern_title = re.compile(str_title)

        str_id = '"id":"' + '[0-9]*'  ##匹配该剧的id值
        pattern_id = re.compile(str_id)

        # 从html_list中的每个item中提取对应的ID值
        id_list = []  # ID存放列表
        for l in html_list:  # 遍历html_list
            find_results_title = re.findall(pattern_title, l, flags=0)  # 找到匹配该剧name的条目item
            if find_results_title != []:  # 如果有title=name的条目，即如果有匹配的结果
                find_results_id = re.findall(pattern_id, l, flags=0)  # 从该匹配的item中的寻找对应的id之
                id_list.append(find_results_id)  # 将寻找到的id值储存在id_list中

        # 可能匹配到了多个ID（可能是同名不同剧），根据产生的id的数量，使剧名name匹配产生的id，使两个list相匹配
        name_list = [name] * len(id_list)

        # 对id_list的格式进行修整，使之成为标准列表格式
        id_list = str(id_list).replace('[', '').replace(']', '').replace("'", '').replace('"id":"', '').replace(' ', '')
        id_list = id_list.split(',')

    except:  # 如果不能正常运行上述代码（不能访问网页等），输出未成功的剧名name。
        print('ERROR:', name)
    return name_list, id_list


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


import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8003))
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
    print("recv:" + str(szBuf, 'utf-8'))
    moviename = str(szBuf, 'utf-8')[8:-2]
    print(moviename)

    name_list, id_list = find_ID(moviename)
    print(id_list[0])

    html = open_url(
        'http://m.douban.com/movie/subject/' + id_list[0] + '/')
    soup = BeautifulSoup(html, 'lxml')

    soup = BeautifulSoup(html, 'lxml')
    doubanrating = soup.find(class_='ll rating_num').get_text()
    print(doubanrating)

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(bytes(doubanrating, encoding="utf8"))

    conn.close()
    print("end of servive")
