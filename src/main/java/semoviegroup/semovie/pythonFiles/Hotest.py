import requests
from urllib.parse import urlencode
import json
from xml.dom.minidom import Document


def get_movie_page(start_number):
    data = {
        'type': 'movie',
        'tag': '热门',
        'sort': 'recommend',
        'page_limit': 4,
        'page_start': start_number
    }
    url = 'https://movie.douban.com/j/search_subjects?' + urlencode(data)
    try:
        response = requests.get(url)
        if response.status_code == 200:
            # print(response.text)
            return response.text
    except Exception:
        print('请求出错！')
        return None


def parse_index_movie(html):
    movie = json.loads(html)
    result = []
    if movie and 'subjects' in movie.keys():
        for item in movie.get('subjects'):
            film = {
                'rate': item.get('rate'),
                'title': item.get('title'),
                'url': item.get('url'),
                'cover': item.get('cover')
            }
            result.append(film)
            print(result)
    try:
        # 写入xml文件中
        doc = Document()
        # 创建根节点movielist
        movielist = doc.createElement('movielist')
        # 根节点插入dom树
        doc.appendChild(movielist)

        # 对于每组数据创建父节点movie,子节点title,rating,plot_simple
        for i in range(len(result)):
            print(result[i].get('rate'))
            movie = doc.createElement('movie')
            movielist.appendChild(movie)
            # 片名
            Title = doc.createElement('title')
            Title_text = doc.createTextNode(result[i].get('title'))
            Title.appendChild(Title_text)
            movie.appendChild(Title)
            # 评分
            Rating = doc.createElement('rating')
            Rating_text = doc.createTextNode(result[i].get('rate'))
            Rating.appendChild(Rating_text)
            movie.appendChild(Rating)
            # 海报
            Cover = doc.createElement('cover')
            Cover_text = doc.createTextNode(result[i].get('cover'))
            Cover.appendChild(Cover_text)
            movie.appendChild(Cover)
            # 地址
            # Url = doc.createElement('url')
            # Url_text = doc.createTextNode(url_list[i])
            # Url.appendChild(Url_text)
            # movie.appendChild(Url)
        with open('Hot.xml', 'wb') as f:
            f.write(doc.toprettyxml(indent='\t', encoding='utf-8'))

    except Exception:
        print('保存出错', film)
        pass


def main():
    for i in range(1):
        html = get_movie_page(i*4)
        parse_index_movie(html)


if __name__ == '__main__':
    main()

import socket

try:
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM);
    print("create socket succ!")

    sock.bind(('localhost', 8012))
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
    main()
    if "0" == szBuf:
        conn.send(b"exit")
    else:
        conn.send(b"result saved at D:\PycharmProjects\SEmovie\Hot.xml")

    conn.close()
    print("end of servive")
