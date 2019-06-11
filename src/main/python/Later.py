import requests
import json
from bs4 import BeautifulSoup
import webbrowser
import xlwt
from xml.dom.minidom import Document
from requests.exceptions import RequestException
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
# 热映的url


def parse_page():
    url = 'https://movie.douban.com/coming'
    try:
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.84 Safari/537.36'}
        r = requests.get(url, headers=headers)
        #print(r)
        if r.status_code == 200:
            return r.text
        return None
    except RequestException:
        return None
    res = r.text
    soup = BeautifulSoup(res, 'html.parser')
    trs = soup.find('table', 'coming_list').find('tbody').find_all('tr')
    coming = ''
    # print(div)
    nowplaying = ''
    for i in range(0, 4):
        print(i)
        if trs[i].find_all('td')[0].text.strip() != None:
            time = trs[i].find_all('td')[0].text.strip()
        else:
            time = '暂无上映日期'

        if trs[i].find_all('td')[1].text.strip() != None:
            name = trs[i].find_all('td')[1].text.strip()
        else:
            name = '暂无片名'

        if trs[i].find_all('td')[2].text.strip() != None:
            type = trs[i].find_all('td')[2].text.strip()
        else:
            type = '暂无类型'

        if trs[i].find_all('td')[3].text.strip() != None:
            country = trs[i].find_all('td')[3].text.strip()
        else:
            country = '暂无地区'

        if trs[i].find_all('td')[4].text.strip() != None:
            wanting = trs[i].find_all('td')[4].text.strip()
        else:
            wanting = '暂无想看人数'

        if  trs[i].find_all('td')[1].find('a').get('href').strip()  != None:
            plot_simple =  trs[i].find_all('td')[1].find('a').get('href').strip()
        else:
            plot_simple = '暂无简介'

        time_list.append(time)
        name_list.append(name)
        type_list.append(type)
        country_list.append(country)
        wanting_list.append(wanting)
        plot_simple_list.append(plot_simple)
        # print(name_list)
        # print(rating_list)
        # print(plot_simple_list)
        # if i < 10 :
        # print(div[i].find("li", attrs={"class": 'srating'}).find('span','subject-rate'))

    file = xlwt.Workbook()

    table = file.add_sheet('即将上映')
    table.write(0, 0, "id")
    table.write(0, 1, "上映时间")
    table.write(0, 2, "片名")
    table.write(0, 3, "类型")
    table.write(0, 4, "地区")
    table.write(0, 5, "想看人数")
    table.write(0, 6, "简介")

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
        # 上映时间
        Time = doc.createElement('time')
        Time_text = doc.createTextNode(time_list[i])
        Time.appendChild(Time_text)
        movie.appendChild(Time)
        # 类型
        Type= doc.createElement('type')
        Type_text = doc.createTextNode(type_list[i])
        Type.appendChild(Type_text)
        movie.appendChild(Type)
        # 地区
        Country = doc.createElement('country')
        Country_text = doc.createTextNode(country_list[i])
        Country.appendChild(Country_text)
        movie.appendChild(Country)
        # 想看人数
        Wanting = doc.createElement('wanting')
        Wanting_text = doc.createTextNode(str(wanting_list[i]))
        Wanting.appendChild(Wanting_text)
        movie.appendChild(Wanting)
        # 简介
        Plot_simple = doc.createElement('plot_simple')
        Plot_simple_text = doc.createTextNode(plot_simple_list[i])
        Plot_simple.appendChild(Plot_simple_text)
        movie.appendChild(Plot_simple)
    with open('Later.xml', 'wb') as f:
        f.write(doc.toprettyxml(indent='\t', encoding='utf-8'))


import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8011))
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
        conn.send(b"result saved at D:\PycharmProjects\SEmovie\Later.xml")

    conn.close()
    print("end of servive")
