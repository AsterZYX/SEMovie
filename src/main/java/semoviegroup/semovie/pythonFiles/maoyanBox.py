import os
import re
import time
import json
import requests
from requests.exceptions import RequestException
from xml.dom.minidom import Document

import socket
import threading
import socketserver

class maoyan():
    def __init__(self):
        self.headers = {
            'Host': 'piaofang.maoyan.com',
            'Referer': 'https://piaofang.maoyan.com/dashboard',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36 LBBROWSER',
            'X-Requested-With': 'XMLHttpRequest'
        }


    def get_page(self):
        url = 'https://box.maoyan.com/promovie/api/box/second.json'
        try:
            response = requests.get(url, self.headers)
            if response.status_code == 200:
                return response.json()
        except requests.ConnectionError as e:
            print('Error', e.args)

    def parse_page(self, json):
        if json:
            data = json.get('data')
            # 场均上座率, 场均人次, 平均票价, 票房, 票房占比, 电影名称, 上映信息（上映天数）, 座位费率, 排片场次, 排片占比, 总票房
            dimensions = ['avgSeatView', 'avgShowView', 'avgViewBox', 'boxInfo', 'boxRate', 'movieName', 'releaseInfo',
                          'seatRate', 'showInfo', 'showRate', 'sumBoxInfo']
            for index, item in enumerate(data.get('list')):
                self.piaofang = {}
                for dimension in dimensions:
                    self.piaofang[dimension] = item.get(dimension)
                yield self.piaofang



    def main(self):
        json = self.get_page()
        results = self.parse_page(json)
        os.system('cls')
        print(json.get('data')['updateInfo'])
        x_line = '-' * 155
        print(f"今日总票房: {json.get('data')['totalBox']} {json.get('data')['totalBoxUnit']}", end=f'\n{x_line}\n')
        print('电影名称', '综合票房（万）', '票房占比', '场均上座率', '场均人次', '平均票价', '排片场次', '排片占比', '累积总票房', '上映天数', sep='\t',
              end=f'\n{x_line}\n')
        res = ""
        boxDict = []
        for result in results:
            print(
                result['movieName'][:7].ljust(8),
                result['boxInfo'][:8].rjust(8),
                result['boxRate'][:8].rjust(8),
                result['avgSeatView'][:8].rjust(8),
                result['avgShowView'][:8].rjust(8),
                result['avgViewBox'][:8].rjust(8),
                result['showInfo'][:8].rjust(8),
                result['showRate'][:8].rjust(8),
                result['sumBoxInfo'][:8].rjust(8),
                result['releaseInfo'][:8],
                sep='\t', end='\n\n'
            )

            res = '{"movieName":"'+result['movieName']+'","boxInfo":"'+result['boxInfo']+'","boxRate":"'+result['boxRate']+'","avgSeatView":"'+result['avgSeatView']+'","avgShowView":"'+result['avgShowView']+'","avgViewBox":"'+result['avgViewBox']+'","showInfo":"'+result['showInfo']+'","showRate":"'+result['showRate']+'","sumBoxInfo":"'+result['sumBoxInfo']+'","releaseInfo":"'+result['releaseInfo']+'"}'
            #print(res)

            ress = {}
            ress['movieName'] = result['movieName']
            ress['boxInfo'] = result['boxInfo']
            ress['boxRate'] = result['boxRate']
            ress['avgSeatView'] = result['avgSeatView']
            ress['avgShowView'] = result['avgShowView']
            ress['avgViewBox'] = result['avgViewBox']
            ress['showInfo'] = result['showInfo']
            ress['showRate'] = result['showRate']
            ress['sumBoxInfo'] = result['sumBoxInfo']
            ress['releaseInfo'] = result['releaseInfo']
            #ress = {'movieName': result['movieName'], 'boxInfo': result['boxInfo'], 'boxRate': result['boxRate'], 'avgSeatView': result['avgSeatView'], 'avgShowView': result['avgShowView'], 'avgViewBox': result['avgViewBox'], 'showInfo': result['showInfo'], 'showRate': result['showRate'], 'sumBoxInfo': result['sumBoxInfo'], 'releaseInfo': result['releaseInfo']}
            #print(ress)
            #writeInfoToXml(ress, 'E:\\大三\\3\\应用集成原理与工具\\Pythontest1\\box.xml')
            boxDict.append(ress)

            # 创建dom文档
            doc = Document()

            # 创建根节点
            boxlist = doc.createElement('boxlist')
            # 根节点插入dom树
            doc.appendChild(boxlist)

            # 依次将box中的每一组元素提取出来，创建对应节点并插入dom树
            for v in boxDict:
                # 分离
                (movieName, boxInfo, boxRate, avgSeatView, avgShowView, avgViewBox, showInfo, showRate, sumBoxInfo,
                 releaseInfo) = (
                v['movieName'], v['boxInfo'], v['boxRate'], v['avgSeatView'], v['avgShowView'], v['avgViewBox'],
                v['showInfo'], v['showRate'], v['sumBoxInfo'], v['releaseInfo'])
                # (index, image, title, actor, time, score) = (v[0], v[1], v[2], v[3], v[4], v[5])
                boxes = doc.createElement('boxes')
                boxlist.appendChild(boxes)

                # 将排名插入<boxes>中
                # 创建节点<movieName>
                movieNamee = doc.createElement('movieName')
                # 创建<index>下的文本节点
                movieName_text = doc.createTextNode(movieName)
                # 将文本节点插入到<index>下
                movieNamee.appendChild(movieName_text)
                # 将<index>插入到父节点<scores>下
                boxes.appendChild(movieNamee)

                # 将boxInfo插入<boxes>中，处理同上
                boxInfoo = doc.createElement('boxInfo')
                boxInfo_text = doc.createTextNode(boxInfo)
                boxInfoo.appendChild(boxInfo_text)
                boxes.appendChild(boxInfoo)

                boxRatee = doc.createElement('boxRate')
                boxRate_text = doc.createTextNode(boxRate)
                boxRatee.appendChild(boxRate_text)
                boxes.appendChild(boxRatee)

                avgSeatVieww = doc.createElement('avgSeatView')
                avgSeatView_text = doc.createTextNode(avgSeatView)
                avgSeatVieww.appendChild(avgSeatView_text)
                boxes.appendChild(avgSeatVieww)

                avgShowVieww = doc.createElement('avgShowView')
                avgShowView_text = doc.createTextNode(avgShowView)
                avgShowVieww.appendChild(avgShowView_text)
                boxes.appendChild(avgShowVieww)

                avgViewBoxx = doc.createElement('avgViewBox')
                avgViewBox_text = doc.createTextNode(avgViewBox)
                avgViewBoxx.appendChild(avgViewBox_text)
                boxes.appendChild(avgViewBoxx)

                showInfoo = doc.createElement('showInfo')
                showInfo_text = doc.createTextNode(showInfo)
                showInfoo.appendChild(showInfo_text)
                boxes.appendChild(showInfoo)

                showRatee = doc.createElement('showRate')
                showRate_text = doc.createTextNode(showRate)
                showRatee.appendChild(showRate_text)
                boxes.appendChild(showRatee)

                sumBoxInfoo = doc.createElement('avgShowView')
                sumBoxInfo_text = doc.createTextNode(sumBoxInfo)
                sumBoxInfoo.appendChild(sumBoxInfo_text)
                boxes.appendChild(sumBoxInfoo)

                releaseInfoo = doc.createElement('avgViewBox')
                releaseInfo_text = doc.createTextNode(releaseInfo)
                releaseInfoo.appendChild(releaseInfo_text)
                boxes.appendChild(releaseInfoo)

            try:
                with open('box.xml', 'w', encoding='UTF-8') as fh:
                    doc.writexml(fh, indent='', addindent='\t', newl='\n', encoding='UTF-8')
                    print('写入')
            except Exception as err:
                print('错误信息：{0}'.format(err))

        #while True:

            #time.sleep(8)


if __name__ == "__main__":
    my = maoyan()
    my.main()
import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8005))
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
        conn.send(b"D:\PycharmProjects\SEmovie\Box.xml")

    conn.close()
    print("end of servive")