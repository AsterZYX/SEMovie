# -*-coding=UTF-8 -*-
import requests
import json
from bs4 import BeautifulSoup
import webbrowser
import xlwt
from xml.dom.minidom import Document

headers = {
    'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36',
    'Host': 'movie.douban.com'}
name_list = []
rating_list = []
peoplecount_list = []
url_list = []
# 得到前100名的电影名、评分、评分人数

for i in range(0, 4):
    link = 'https://movie.douban.com/top250?start=' + str(i * 25) + '&filter='
    r = requests.get(link, headers=headers, timeout=10)
    print(str(i + 1), 'states:', r.status_code)
    # print(r.text)
    soup = BeautifulSoup(r.text, "html.parser")
    for tag in soup.find_all('div', class_='info'):
        # print tag
        m_name = tag.find('span', class_='title').get_text()
        m_rating_score = float(tag.find('span', class_='rating_num').get_text())
        m_people = tag.find('div', class_="star")
        m_span = m_people.findAll('span')
        m_peoplecount = m_span[3].contents[0]
        m_url = tag.find('a').get('href')
        print(m_name + "        " + str(m_rating_score) + "           " + m_peoplecount + "    " + m_url)
        name_list.append(m_name)
        rating_list.append(str(m_rating_score))
        peoplecount_list.append(m_peoplecount)
        url_list.append(m_url)
# 写入xml文件中
doc = Document()
# 创建根节点movielist
movielist = doc.createElement('movielist')
# 根节点插入dom树
doc.appendChild(movielist)

# 对于每组数据创建父节点movie,子节点title,rating,plot_simple
for i in range(len(name_list)):
    movie = doc.createElement('movie')
    movielist.appendChild(movie)
    # 片名
    Title = doc.createElement('title')
    Title_text = doc.createTextNode(name_list[i])
    Title.appendChild(Title_text)
    movie.appendChild(Title)
    # 评分
    Rating = doc.createElement('rating')
    Rating_text = doc.createTextNode(str(rating_list[i]))
    Rating.appendChild(Rating_text)
    movie.appendChild(Rating)
    # 评分人数
    Peoplecount = doc.createElement('peoplecount')
    Peoplecount_text = doc.createTextNode(peoplecount_list[i])
    Peoplecount.appendChild(Peoplecount_text)
    movie.appendChild(Peoplecount)
    # 地址
    # Url = doc.createElement('url')
    # Url_text = doc.createTextNode(url_list[i])
    # Url.appendChild(Url_text)
    # movie.appendChild(Url)
with open('Rating100.xml', 'wb') as f:
    f.write(doc.toprettyxml(indent='\t', encoding='utf-8'))
import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8007))
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
    #parse_page()

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"D:\PycharmProjects\SEmovie\Rating100.xml")

    conn.close()
    print("end of servive")