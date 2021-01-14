from flask import Flask, redirect, url_for, render_template
import psycopg2
import numpy 
import os
import re
import time
from flask import request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import text
import matplotlib.pyplot as plt
app = Flask(__name__, template_folder='template')
con = psycopg2.connect(database="prokopis", user="prokopis",password="123", host="127.0.0.1", port="5432")
cursor = con.cursor()

@app.route("/")
def home():
    return render_template('Main.html')

@app.route("/login", methods=['POST'])
def login():
    if request.method == 'POST':
        sql_query = request.form['query']
        column_name_list = val_inside(sql_query)#check which column are on result
        hist_create=0
        cursor.execute(sql_query)
        searchresults = cursor.fetchall()
        #Graphic representation
        if Table(sql_query)=="TikTokVideoHashTagInfoTable":
            date_list=[]
            HASHTAG_name=""
            Num_on_specific_date=[]
            graphic=0
            for elem in column_name_list:
                if elem.find("COUNT") !=-1:
                    graphic=1
                    break
            if graphic==1:
                if len(searchresults)==0:
                    return render_template('Main.html')
                for k in searchresults:
                    HASHTAG_name=k[0];
                    date_list.append(k[1])
                    Num_on_specific_date.append(k[2])
                plt.figure(figsize=(9, 3))
                plt.plot(date_list, Num_on_specific_date)
                plt.suptitle(HASHTAG_name)
                path="static/images/image"+str(time.time())+".png"
                for filename in os.listdir('static/images'):
                        os.remove('static/images/' + filename)
                plt.savefig(path, bbox_inches='tight')
                return render_template('Graphic.html', val=sql_query,url=path)
            else:
                t = []#creating list of tuples
                for e in searchresults:
                    ko = []
                    for elem in e:
                        ko.append(elem)
                    t.append(ko)
            return render_template('Result.html', val=sql_query, data=t, my_val=column_name_list)
        else:
            coun=0
            columns_number=0
            count_position=0
            for elem in column_name_list:
                #check if i have count to show histogram as result on html page 
                if elem.find("COUNT") !=-1:
                    hist_create=1
                    count_position=coun
                    break
                if elem.find("COUNT") == -1:
                    pos=coun
                    columns_number+=1
                coun+=1
            if hist_create == 1 and columns_number == 1:
                t = []#creating list of tuples
                for e in searchresults:
                    ko = []
                    for elem in e:
                        ko.append(elem)
                    t.append(ko)
                x_list=[]
                y_list=[]
                num=[]
                er=0
                #if not t case of no result
                if not t:
                    return render_template('Result.html', val=sql_query, data=t, my_val=column_name_list)
                for g in t:
                    j1= g[pos].replace(' ','')
                    x_list.append(j1)
                    y_list.append(g[1])
                    er+=1
                    num.append(er)
                #Histogram creation
                fig, ax = plt.subplots()
                bar_plot = plt.bar(num,y_list,tick_label=x_list)
                for idx,rect in enumerate(bar_plot):
                    height = rect.get_height()
                    ax.text(rect.get_x() + rect.get_width()/2., 1.05*height,s="",ha='center', va='bottom', rotation='vertical')
                ###################
                plt.xticks(rotation=90)
                #Show Histogram on url            
                path="static/images/image"+str(time.time())+".png"
                for filename in os.listdir('static/images'):
                        os.remove('static/images/' + filename)
                plt.savefig(path, bbox_inches='tight')
                ######################
                return render_template('hist.html', val=sql_query,url=path)
            elif hist_create == 1 and columns_number !=1 :#Histogram with more than 1 columns count()
                t = []#creating list of tuples
                for e in searchresults:
                    ko = []
                    for elem in e:
                        ko.append(elem)
                    t.append(ko)
                if not t:
                    return render_template('Result.html', val=sql_query, data=t, my_val=column_name_list)
                y_list=[]
                for q in t:
                    y_list.append(q[count_position])
                x_list=[]
                for q in t:
                    count_for=0
                    d=[]
                    for elem in q:
                        if count_for == count_position:
                            continue
                        if isinstance(elem, str) == True:
                            elem= elem.replace(' ','')
                        d.append(elem)
                        count_for+=1
                    x_list.append(d)
                er=0
                num=[]
                x_l=[]
                for i in x_list:
                    er+=1;
                    num.append(er)
                for elem in x_list:
                    my_string=""
                    val=0
                    finish=len(elem)
                    for k in elem:
                        if val==len(elem)-1:
                            my_string=my_string+str(k)
                        else:
                            my_string=my_string+str(k)+"-"
                        val+=1
                    x_l.append(my_string)
                x_list.clear()
                for k in x_l:
                    x_list.append(k)
                x_l.clear()
                #Histogram creation
                fig, ax = plt.subplots()
                bar_plot = plt.bar(num,y_list,tick_label=x_list)
                for idx,rect in enumerate(bar_plot):
                    height = rect.get_height()
                    ax.text(rect.get_x() + rect.get_width()/2., 1.05*height,s="",ha='center', va='bottom', rotation=0)
                ###################
                plt.xticks(rotation=90)
                #Show Histogram on url            
                path="static/images/image"+str(time.time())+".png"
                for filename in os.listdir('static/images'):
                        os.remove('static/images/' + filename)
                plt.savefig(path,bbox_inches='tight')
                ######################
                return render_template('hist.html', val=sql_query,url=path)
            else:
                t = []#creating list of tuples
                for e in searchresults:
                    ko = []
                    for elem in e:
                        ko.append(elem)
                    t.append(ko)
            return render_template('Result.html', val=sql_query, data=t, my_val=column_name_list)
    else:
        return render_template('Main.html')

def word_list(my_string):
    wordList=re.findall(r'\s|,|[^,\s]+',my_string)
    return wordList

def val_inside(my_string):
    my_string=word_list(my_string)
    my_list = []
    if "TikTokVideoDataTable" in my_string:
        for keyword in my_string:
            if "FROM" == keyword:
                return my_list
            elif "*" == keyword:
                my_list.append("ID")
                my_list.append("NAME")
                my_list.append("TEXT")
                my_list.append("SOUND_TAG")
                my_list.append("LIKES_NUMBER")
                my_list.append("COMMENTS_NUMBER")
                my_list.append("SHARES_NUMBER")
                my_list.append("DATE")
                return my_list;
            elif "ID" == keyword:
                my_list.append("ID")
            elif "DATE" == keyword:
                my_list.append("DATE")
            elif "NAME" == keyword:
                my_list.append("NAME")
            elif "TEXT" == keyword:
                my_list.append("TEXT")
            elif "SOUND_TAG" == keyword:
                my_list.append("SOUND_TAG")
            elif "LIKES_NUMBER" == keyword:
                my_list.append("LIKES_NUMBER")
            elif "COMMENTS_NUMBER" == keyword:
                my_list.append("COMMENTS_NUMBER")
            elif "SHARES_NUMBER" == keyword:
                my_list.append("SHARES_NUMBER")
            elif "COUNT(*)" == keyword:
                my_list.append("COUNT(*)")
            elif "COUNT" == keyword:
                my_list.append("COUNT")
            elif "COUNT(ID)" == keyword:
                my_list.append("COUNT(ID)")
            elif "COUNT(NAME)" == keyword:
                my_list.append("COUNT(NAME)")
            elif "COUNT(TEXT)" == keyword:
                my_list.append("COUNT(TEXT)")
            elif "COUNT(SOUND_TAG)" == keyword:
                my_list.append("COUNT(SOUND_TAG)")
            elif "COUNT(LIKES_NUMBER)" == keyword:
                my_list.append("COUNT(LIKES_NUMBER)")
            elif "COUNT(COMMENTS_NUMBER)" == keyword:
                my_list.append("COUNT(COMMENTS_NUMBER)")
            elif "COUNT(SHARES_NUMBER)" == keyword:
                my_list.append("COUNT(SHARES_NUMBER)")
        return my_list
    if "TikTokVideoHashTagInfoTable" in my_string:
        for keyword in my_string:
            if "FROM" == keyword:
                return my_list
            if "ID" == keyword:
                my_list.append("ID")
            if "*" == keyword:
                my_list.append("ID")
                my_list.append("NAME")
                my_list.append("DATE")
                my_list.append("HASHTAG")
                return my_list
            if "COUNT(ID)" == keyword:
                my_list.append("COUNT(ID)")
            if "COUNT(NAME)" == keyword:
                my_list.append("COUNT(NAME)")
            if "COUNT(DATE)" == keyword:
                my_list.append("COUNT(DATE)")
            if "DATE" == keyword:
                my_list.append("DATE")
            if "NAME" == keyword:
                my_list.append("NAME")
            if "COUNT(*)" == keyword:
                my_list.append("COUNT(*)")
            if "HASHTAG"  == keyword:
                my_list.append("HASHTAG")
        return my_list

def Table(sql):
    if "TikTokVideoHashTagInfoTable" in sql:
        return "TikTokVideoHashTagInfoTable"
    if "TikTokVideoDataTable" in sql:
        return "TikTokVideoDataTable"

if __name__ == "__main__":
    app.run()
