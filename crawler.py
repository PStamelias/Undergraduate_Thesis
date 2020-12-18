from flask import Flask, redirect, url_for, render_template
import psycopg2
import numpy 
import os
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
    coy=0
    if request.method == 'POST':
        sql_query = request.form['query']
        column_name_list = val_inside(sql_query)#check which column are on result
        hist_create=0
        cursor.execute(sql_query)
        searchresults = cursor.fetchall()
        coun=0
        for elem in column_name_list:
            #check if i have count to show histogram as result on html page 
            if elem.find("COUNT") !=-1:
                hist_create=1
                break
            if elem.find("COUNT") == -1:
                pos=coun
            coun+=1
        if hist_create == 1:
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
                ax.text(rect.get_x() + rect.get_width()/2., 1.05*height,s="",ha='center', va='bottom', rotation=0)
            ###################
            #Show Histogram on url            
            path="static/images/image"+str(time.time())+".png"
            for filename in os.listdir('static/images'):
                    os.remove('static/images/' + filename)
            plt.savefig(path)
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


def val_inside(my_string):
    my_list = []
    if "ID" in my_string:
        my_list.append("ID")
    if "NAME" in my_string:
        my_list.append("NAME")
    if "TEXT" in my_string:
        my_list.append("TEXT")
    if "SOUND_TAG" in my_string:
        my_list.append("SOUND_TAG")
    if "LIKES_NUMBER" in my_string:
        my_list.append("LIKES_NUMBER")
    if "COMMENTS_NUMBER" in my_string:
        my_list.append("COMMENTS_NUMBER")
    if "SHARES_NUMBER" in my_string:
        my_list.append("SHARES_NUMBER")
    if "COUNT(*)" in my_string:
    	my_list.append("COUNT(*)")
    return my_list

if __name__ == "__main__":
    app.run()
