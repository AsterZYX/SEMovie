import requests
from bs4 import BeautifulSoup
from requests.exceptions import RequestException
import socket
import threading
import socketserver
import json, types, string
import os, time
from xml.dom.minidom import Document

# 热映的url
movieid_list = []
rating_list = []
time_list = []
language_list = []
name_list = []
post_list = []
writers_list = []
film_locations_list = []
directors_list = []
rating_count_list = []
actors_list = []
plot_simple_list = []
year_list = []
country_list = []
type_list = []
release_date_list = []
also_known_as_list = []
wanting_list = []
poster_list = []


def parse_page():
    r = requests.get('https://movie.douban.com/cinema/nowplaying/dongying/')
    res = r.text
    soup = BeautifulSoup(res, 'html.parser')
    div = soup.find(id='nowplaying').find_all("li", attrs={"class": "list-item"})
    # print(div)
    nowplaying = ''
    for i in range(0, 4):
        name = div[i].find("li", attrs={"class": 'stitle'}).find('a').get('title')
        if div[i].find("li", attrs={"class": 'srating'}).find('span', 'subject-rate') != None:
            rating = div[i].find("li", attrs={"class": 'srating'}).find('span', 'subject-rate').get_text()
            print(rating)
        else:
            rating = "暂无评分"
        # plot_simple = div[i].find("li", attrs={"class": 'stitle'}).find('a').get('href')
        poster = div[i].find("li", attrs={"class": 'poster'}).find('a').find('img').attrs['src']
        # print(poster)
        name_list.append(name)
        rating_list.append(rating)
        post_list.append(poster)
        # plot_simple_list.append(plot_simple)
    jresp = json.dumps(name_list)
    res = json.loads(jresp)

    # print(name)
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

        Title = doc.createElement('title')
        Title_text = doc.createTextNode(name_list[i])
        Title.appendChild(Title_text)
        movie.appendChild(Title)

        Rating = doc.createElement('rating')
        Rating_text = doc.createTextNode(str(rating_list[i]))
        Rating.appendChild(Rating_text)
        movie.appendChild(Rating)

        Poster = doc.createElement('poster')
        Poster_text = doc.createTextNode(post_list[i])
        Poster.appendChild(Poster_text)
        movie.appendChild(Poster)
    with open('Showing.xml', 'wb') as f:
        f.write(doc.toprettyxml(indent='\t', encoding='utf-8'))


import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8010))
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
    parse_page()

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"result saved at D:\PycharmProjects\SEmovie\Showing.xml")

    conn.close()
    print("end of servive")
