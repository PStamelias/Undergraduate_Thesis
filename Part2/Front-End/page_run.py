from flask import Flask, redirect, url_for, render_template,request
import os.path,subprocess
from random import shuffle
from subprocess import STDOUT,PIPE
import json
import os
from itertools import zip_longest
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
    mystr=""
    for i  in range(0,len(info)):
        if info[i]==',':
            break
        mystr=mystr+info[i]
    return mystr

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


def return_creator(str):
    coun=0
    my_str=""
    for i in range(0,len(str)):
        if str[i]==',':
            coun=coun+1
            continue
        if coun==2:
            my_str=my_str+str[i]
    return my_str


def grouper(n, iterable):
    my_list=[]
    num=0
    val=0
    for k in range(0,len(iterable)):
        if val==n:
            my_list.append(iterable[k])
            num=num+1
            if num%10==0:
                val=val+1
        else:
            num=num+1
            if num%10==0:
                val=val+1
    return my_list


########################
@app.route('/')
def home():
    return render_template('Main_Page.html')



def compute_result(Query,Input):
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
    coun=0
    shuffle(data)#sort randomly the list to show in random the results
    for element in data:
        if  element:
            type_source=return_page_scrape(element)
            if type_source=="Youtube":
                record=[]
                creator=return_creator(element)
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
                published_time=""
                duration=""
                enter_on_duration=False
                for elem in lis_of_data:
                    if "link"  in elem and "watch" in elem: 
                        url=elem
                    if "publishedTime:" in elem:
                        published_time=elem
                    if "duration:" in elem and enter_on_duration==False:
                        duration=elem
                        enter_on_duration=True
                if not url:
                    continue
                duration=duration.replace('duration','')
                duration=duration.replace(' ','')
                duration=duration[1:]
                published_time=published_time.replace('publishedTime:','')
                published_time=published_time[2:]
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
                record=["Youtube",title,url,Thumb_Url,duration,published_time,creator]
                Videos_Info.append(record)
            if type_source=="TikTok":
                record=return_contentof_element(element)
                Videos_Info.append(record)
            coun=coun+1
    return Videos_Info

@app.route('/Other',methods=('GET', 'POST'))
def Other():
    name = request.args.get('name')
    text = request.args.get('text')
    sound_tag = request.args.get('sound_tag')
    likes_number = request.args.get('likes_number')
    comments_number = request.args.get('comments_number')
    shares_number = request.args.get('shares_number')
    date = request.args.get('date')
    record=[name,text,sound_tag,likes_number,comments_number,shares_number,date]
    return render_template('TikTokResult.html',record=record)





@app.route('/How_Search_works',methods=('GET', 'POST'))
def How_Search_works():
    return render_template('Search_Info.html')


@app.route('/Searching',methods=('GET', 'POST'))
def SearchResult():
    global List_Of_Result
    Query = request.form.getlist('Query')
    Query_current = request.args.getlist('Query')
    num   = request.args.get('chunck_num')
    result_list  = request.args.getlist('result_list')
    previous_val  = request.args.get('previous_val')
    print("num=",num)
    print("Query=",Query)
    print("previous_val=",previous_val)
    print("Query_current=",Query_current)
    if Query:
        print("query")
        Input = Query[0]
        records=compute_result(Query,Input)
        List_Of_Result=records
        print("----------------------------")
        print("ela")
        coun=len(records)
        sizes=coun/10
        sizes=int(sizes)
        if coun%10!=0:
            sizes=sizes+1
        print("sizes=",sizes)
        n=sizes
        sub = grouper(0,records)
        for i in range(0,len(sub)):
            print("sub[i]=",sub[i])
        print(type(sub))
        size_sub_list=10
        print("size_sub_list=",size_sub_list)
        return render_template('Result2.html',val=1,result_list=records,sub_list=sub,Search_value=Input,size=sizes,previous_val=Input,len_list=coun,size_sub_list=size_sub_list,chunks_num=sizes)
    if previous_val==Query_current[0]:
        Input = Query_current[0]
        coun=len(List_Of_Result)
        sizes=coun/10
        sizes=int(sizes)
        if coun%10!=0:
            sizes=sizes+1
        n=sizes
        sub = grouper(int(num),List_Of_Result)
        mylist=[]
        for i in range(0,len(sub)):
            mylist.append(sub[i])
        return render_template('Result2.html',val=1,result_list=List_Of_Result,sub_list=mylist,Search_value=Input,size=sizes,previous_val=Input,len_list=coun,size_sub_list=10,chunks_num=sizes)




@app.route('/About_us',methods=('GET', 'POST'))
def About_us():
    return render_template('About.html')



if __name__ == '__main__':
    app.run()