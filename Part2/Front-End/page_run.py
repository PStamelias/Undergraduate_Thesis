from flask import Flask, redirect, url_for, render_template,request
import os.path,subprocess
from random import shuffle
from subprocess import STDOUT,PIPE
import json
import os
import requests
from youtubesearchpython import VideosSearch
app = Flask(__name__, template_folder='template')

def compile_java(java_file):
    subprocess.check_call(['javac','-cp','.:log4j-api-2.11.1.jar:lucene-7.11.2.0.jar:lucene-core-8.7.0.jar:models-7.11.2.0.jar:lucene-queries-7.3.1.jar:lucene-queryparser-7.3.1.jar:lucene-sandbox-7.3.1.jar:lucene-analyzers-common-8.2.0.jar:lucene-core-8.2.0.jar', java_file])

def execute_java(java_file,Query):
    java_class,ext = os.path.splitext(java_file)
    cmd = ['java','-cp','.:log4j-api-2.11.1.jar:lucene-7.11.2.0.jar:lucene-core-8.7.0.jar:models-7.11.2.0.jar:lucene-queries-7.3.1.jar:lucene-queryparser-7.3.1.jar:lucene-sandbox-7.3.1.jar:lucene-analyzers-common-8.2.0.jar:lucene-core-8.2.0.jar', java_class,Query]
    proc = subprocess.Popen(cmd, stdout=PIPE, stderr=STDOUT)
    stdout,stderr = proc.communicate()
    stdoute=stdout.decode()
    return stdoute

def return_page_scrape(info):
    if "Youtube"  in info: 
        return "Youtube"
    if "TikTok"  in info:
        return "TikTok"

def return_title(info,type):
    info=info.replace(type,'')
    info=info.replace(' ','')
    return info


def return_contentof_element(my_string):
    pos=0
    name=""
    text=""
    sound_Tag=""
    likes_number=""
    comments_number=""
    shares_number=""
    date=""
    for i in range(0,len(my_string)):
        if my_string[i]==" ":
            continue
        if my_string[i]==",":
            pos=pos+1
            continue
        if pos==0:
            name=name+my_string[i]
        if pos==1:
            text=text+my_string[i]
        if pos==2:
            sound_Tag=sound_Tag+my_string[i]
        if pos==3:
            likes_number=likes_number+my_string[i]
        if pos==4:
            comments_number=comments_number+my_string[i]
        if pos==5:
            shares_number=shares_number+my_string[i]
        if pos==6:
            date=date+my_string[i];
    record=["TikTok",name,text,sound_Tag,likes_number,comments_number,shares_number,date]
    return record






########################
@app.route('/')
def home():
    return render_template('Main_Page.html')


@app.route("/Search", methods=['POST'])
def Search():
    counter1=0
    Query = request.form.getlist('Query')
    Input = Query[0]
    compile_java('Search.java')
    result=execute_java('Search.java',Input)
    data=[]
    urls=[]
    thumbnails=[]
    record=""
    for ch in result:
        if ch=='~':
            data.append(record)
            record=""
            continue
        if ch=='\n':
            continue
        record=record+ch
    Videos_Info=[]
    shuffle(data)#sort randomly the list to show in random the results
    for element in data:
        if  element:
            type_source=return_page_scrape(element)
            if type_source=="Youtube":
                record=[]
                title=return_title(element,"Youtube")
                search = VideosSearch(title,limit = 1)
                json_object = json.dumps(search.result(), indent = 1)
                aList = json.loads(json_object)
                StringJson=str(aList)
                lis_of_data=[]
                stringdata=""
                for ch in StringJson:
                    if ch=="'":
                        continue
                    if ch==",":
                        lis_of_data.append(stringdata)
                        stringdata=""
                        continue
                    stringdata=stringdata+ch
                url=""
                for element in lis_of_data:
                    if "link"  in element and "watch" in element: 
                        url=element
                if not url:
                    continue
                url=url.replace('link','')
                url=url.replace(' ','')
                url=url[1:]
                #print(url)
                thumb_value=""
                begin=0
                for k in range(0,len(url)):
                    if url[k]=="=":
                        begin=1
                        continue
                    if begin==1:
                        thumb_value=thumb_value+url[k]
                Thumb_Url="https://i.ytimg.com/vi/"+thumb_value+"/maxresdefault.jpg";
                record=["Youtube",title,url,Thumb_Url]
                Videos_Info.append(record)
            if type_source=="TikTok":
                record=return_contentof_element(element)
                Videos_Info.append(record)
    return render_template('result.html',data=Videos_Info,Search_value=Input)



@app.route('/Bookdetails',methods=('GET', 'POST'))
def Bookdetails():
    name = request.args.get('name')
    text = request.args.get('text')
    sound_tag = request.args.get('sound_tag')
    likes_number = request.args.get('likes_number')
    comments_number = request.args.get('comments_number')
    shares_number = request.args.get('shares_number')
    date = request.args.get('date')
    record=[name,text,sound_tag,likes_number,comments_number,shares_number,date]
    return render_template('TikTokResult.html',record=record)

if __name__ == '__main__':
    app.run()