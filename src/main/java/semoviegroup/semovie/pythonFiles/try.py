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





name_list, id_list = find_ID("大侦探皮卡丘")
print(id_list[0])


html = open_url(
    'http://m.douban.com/movie/subject/'+id_list[0]+'/')
soup = BeautifulSoup(html, 'lxml')
doubanrating = soup.find(class_='ll rating_num').get_text()
print(doubanrating)
# http://m.douban.com/movie/subject/26835471/
