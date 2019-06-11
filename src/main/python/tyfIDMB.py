# -*- coding: utf-8 -*-
import jieba
import sys
import matplotlib.pyplot as plt
from wordcloud import WordCloud
import codecs
from snownlp import SnowNLP
import operator
import socket

# 打开本体TXT文件
text = codecs.open('GetComments_200.csv').read()

# 结巴分词 cut_all=True 设置为精准模式
wordlist = jieba.cut(text, cut_all=False)

dict = {}

# 使用空格连接 进行中文分词
wl_space_split = " ".join(wordlist)
for i in wl_space_split.split(" "):
    if i in dict.keys():
        dict[i] = dict[i] + 1
    else:
        dict[i] = 1

high = []
count = 0
for i in sorted(dict.items(), key=operator.itemgetter(1), reverse=True):
    if len(i[0]) >= 2 and count < 10:
        high.append(i[0])
        count = count + 1
    if count >= 10:
        break
print(high)  # 10个关键词
f2 = open('tyfResult.txt', 'w', encoding="utf-8")
for i in high:
    f2.write(i)
    f2.write(' ')

# 对分词后的文本生成
# font = r'C:\\Windows\\fonts\\SourceHanSansCN-Regular.otf'

# my_wordcloud = WordCloud().generate(wl_space_split)
#
# my_wordcloud.to_file("cloudpic.jpg")

s = SnowNLP(text)
print(u"\n输出关键句子:")
for k in s.summary(1):
    print(k)
    f2.write(k)
f2.close()