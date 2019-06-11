# -*- coding: utf-8 -*-
from snownlp import SnowNLP
import codecs
import os

#获取情感分数
# source = codecs.open('GetComments_200.csv').read()
source = open('GetComments_200.csv', 'r', encoding='GBK')
line = source.readlines()
sentimentslist = []
commentList = []
for i in line:
    s = SnowNLP(i)
    print(s.sentiments)
    #print(i)
    sentimentslist.append(s.sentiments)
    commentList.append(i)

for i in range(len(sentimentslist)):
    # Last i elements are already in place
    for j in range(0, len(sentimentslist) - i - 1):
        if sentimentslist[j] > sentimentslist[j + 1]:
            sentimentslist[j], sentimentslist[j + 1] = sentimentslist[j + 1], sentimentslist[j]
            commentList[j], commentList[j + 1] = commentList[j + 1], commentList[j]

bcount=0
gcount=0
for i in range(len(sentimentslist)):
    if sentimentslist[i] >= 0.5:
        gcount = gcount+1
    else:
        bcount = bcount + 1

file = open('badComments.txt', 'w+', encoding='utf-8')
file1 = open('goodComments.txt', 'w+', encoding='utf-8')
file.write(str(bcount)+'%%%%')

file1.write(str(gcount)+'%%%%')
#file1.write('****')
for i in range(5):
    file.write(commentList[i]+'%%%%')
    #print(commentList[i])
    #file.write()
    file1.write(commentList[len(commentList)-i-1]+'%%%%')
    #file1.write('****')

'''print(len(sentimentslist))
print(len(commentList))'''

'''#区间转换为[-0.5, 0.5]
result = []
i = 0
while i<len(sentimentslist):
    result.append(sentimentslist[i]-0.5)
    i = i + 1'''

