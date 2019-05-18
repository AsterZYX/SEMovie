# -*-coding=UTF-8 -*-
import requests
import json
from bs4 import BeautifulSoup
import webbrowser
import xlwt
from xml.dom.minidom import Document
import re
import requests
from fontTools.ttLib import TTFont
from lxml import etree

import socket


def ok(base_url):
    # 购票信息：location_list，name_list
    location_list = []
    Cinema_Name_list = []
    index_url = base_url
    headers = {
        "User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) "
                      "Chrome/66.0.3359.139 Safari/537.36 "
    }
    r = requests.get(index_url)
    # 第一页offset=0,第二页offset=每页显示的数量，第三页offset=每页显示的数量*2
    res = r.text
    soup = BeautifulSoup(res, 'html.parser')
    div = soup.find_all('div', class_='cinema-info')
    for i in div:
        cinema = i.a.text.strip()
        print(cinema)
        Cinema_Name_list.append(cinema)
        location = i.p.text.strip()
        print(location)
        location_list.append(location)

    # 写入xml文件中
    doc = Document()
    # 创建根节点movielist
    cinemalist = doc.createElement('cinemalist')
    # 根节点插入dom树
    doc.appendChild(cinemalist)

    # 获取首页内容
    response_index = requests.get(index_url, headers=headers).text
    index_xml = etree.HTML(response_index)
    wrongfont_list = []
    rightfont_list = []
    for i in range(1, 13):
        # print(i)
        info_list = index_xml.xpath('//*[@id="app"]/div[2]/div[' + str(i) + ']/div[3]//text()')
        # print(info_list)
        # info_list = index_xml.xpath('//*[@id="app"]/div[2]/div[2]/div[3]//text()')
        # print(info_list)
        if info_list[3] != None:
            a = u'票价：%s' % (info_list[3])
            # print (a)
            wrongfont_list.append(a)

    # 获取字体文件的url
    woff_ = re.search(r"url\('(.*\.woff)'\)", response_index).group(1)
    woff_url = 'http:' + woff_
    response_woff = requests.get(woff_url, headers=headers).content
    # print(woff_)
    with open('fonts.woff', 'wb') as f:
        f.write(response_woff)

    # base_nums， base_fonts 需要自己手动解析映射关系， 要和basefonts.woff一致
    baseFonts = TTFont('4e3a603c59dad5bac18056b3e60d53aa2084.woff')
    base_nums = ['5', '4', '2', '9', '8', '0', '1', '3', '7', '6']
    base_fonts = ['uniE82F', 'uniF6B0', 'uniE5C6', 'uniF524', 'uniF2CD', 'uniEDDD', 'uniF50F', 'uniEE0A', 'uniE062',
                  'uniE7D0']
    onlineFonts = TTFont('fonts.woff')

    # onlineFonts.saveXML('test.xml')

    uni_list = onlineFonts.getGlyphNames()[1:-1]
    temp = {}
    # 解析字体库
    for i in range(10):
        onlineGlyph = onlineFonts['glyf'][uni_list[i]]
        for j in range(10):
            baseGlyph = baseFonts['glyf'][base_fonts[j]]
            if onlineGlyph == baseGlyph:
                temp["&#x" + uni_list[i][3:].lower() + ';'] = base_nums[j]

    # 字符替换
    pat = '(' + '|'.join(temp.keys()) + ')'
    response_index = re.sub(pat, lambda x: temp[x.group()], response_index)

    # 内容提取
    index_xml = etree.HTML(response_index)
    for i in range(1, len(wrongfont_list) + 1):
        # print(i)
        info_list = index_xml.xpath('//*[@id="app"]/div[2]/div[' + str(i) + ']/div[3]//text()')
        # print(info_list)
        # info_list = index_xml.xpath('//*[@id="app"]/div[2]/div[2]/div[3]//text()')
        # print(info_list)
        if info_list[3] != None:
            a = u'票价：%s' % (info_list[3])
            print(a)
            rightfont_list.append(a)

    # 对于每组数据创建父节点movie,子节点title,rating,plot_simple
    for i in range(len(location_list)):
        Cinema = doc.createElement('cinema')
        cinemalist.appendChild(Cinema)
        # 影院名
        Cinema_name = doc.createElement('CinemaName')
        Cinema_name_text = doc.createTextNode(Cinema_Name_list[i])
        Cinema_name.appendChild(Cinema_name_text)
        Cinema.appendChild(Cinema_name)
        # 地址
        Location = doc.createElement('CinemaLocation')
        Location_text = doc.createTextNode(str(location_list[i]))
        Location.appendChild(Location_text)
        Cinema.appendChild(Location)
        # 价格
        Price = doc.createElement('price')
        Price_text = doc.createTextNode(rightfont_list[i])
        Price.appendChild(Price_text)
        Cinema.appendChild(Price)
    with open('Cinemas.xml', 'wb') as f:
        f.write(doc.toprettyxml(indent='\t', encoding='utf-8'))


try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8013))
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
    base_url = str(szBuf, 'utf-8')[8:-2]

    ok(base_url)

    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"D:\PycharmProjects\SEmovie\Cinemas.xml")
    conn.close()
    print("end of servive")
