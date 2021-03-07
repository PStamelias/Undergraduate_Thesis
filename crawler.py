from flask import Flask, redirect, url_for, render_template
from datetime import datetime as dt
import psycopg2
import numpy 
import os
from operator import itemgetter
import datefinder
import re
import time
from flask import request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import text
import matplotlib.pyplot as plt

###Connecting to Database
app = Flask(__name__, template_folder='template')
con = psycopg2.connect(database="prokopis", user="prokopis",password="123", host="127.0.0.1", port="5432")
cursor = con.cursor()
########################

@app.route('/')
def home():
    return render_template('Main.html')

@app.route("/login", methods=['POST'])
def login():
    if request.method == 'POST':
        table_name = request.form['name']
        if table_name=="TikTokVideoDataTable":
            return redirect(url_for('A'))
        if table_name=="TikTokVideoHashTagInfoTable":
           return redirect(url_for('B'))
        if table_name=="TikTokVideoWord":
           return redirect(url_for('C'))
    else:
        return render_template('Main.html')


@app.route('/A')
def A():
    return render_template('Table1.html')


@app.route('/B')
def B():
    return render_template('Table2.html')


@app.route('/C')
def C():
    return render_template('Table3.html')


@app.route('/Table1', methods=['POST'])
def Table1():
    if request.method == 'POST':
        Columns_Name=request.form['Column(s)_Name']
        Where_Condition=request.form['Where_Condition']
        Having_Condition=request.form['Having_Condition']
        Limit=request.form['LIMIT']
        Histogram = request.form.getlist('Histogram')
        DESC=request.form.getlist('DESC')
        ASC=request.form.getlist('ASC')
        NONE=request.form.getlist('NONE')
        ####Building the sql
        sql=""
        sql=sql+"SELECT "
        sql=sql+ Columns_Name
        sql=sql+ " FROM " +"TikTokVideoDataTable" 
        if Where_Condition:
            Where_Conditions=Where_Conditions_found(Where_Condition)
            sql=sql+Where_Conditions
        if Histogram:
            coun=0
            columns_number=0
            count_position=0
            hist_create=0
            column_name_list = val_inside(sql)
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
            enter1=0
            if Having_Condition:
                sql=sql+" GROUP BY "
                for col in column_name_list:
                    if "COUNT" in col:
                        continue
                    sql=sql+ col+","
                enter1=1
                sql = sql[:-1]
                Having_Condition=Having_Conditions_found(Having_Condition)
                sql=sql+Having_Condition
            if enter1==0:
                for text in column_name_list:
                    if "COUNT" in text:
                        sql=sql+" GROUP BY "
                        for col in column_name_list:
                            if "COUNT" in col:
                                continue
                            sql=sql+ col+","
                        sql = sql[:-1]
            if DESC:
                sql=sql +" ORDER BY COUNT(*) DESC "
            if ASC:
                sql=sql +" ORDER BY COUNT(*) ASC "
            if Limit:
                sql=sql + " LIMIT "+ Limit
            ##############################################
            cursor.execute(sql)
            searchresults = cursor.fetchall()
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
                coun1=0
                #if not t case of no result
                if not t:
                    return render_template('Result.html', val=sql, data=t, my_val=column_name_list)
                for g in t:
                    j1= g[pos].replace(' ','')
                    x_list.append(j1)
                    y_list.append(g[1])
                    coun1+=1
                    num.append(coun1)
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
                return render_template('hist.html', val=sql,url=path)
            elif hist_create == 1 and columns_number !=1 :#Histogram with more than 1 columns count()
                t = []#creating list of tuples
                for e in searchresults:
                    list10 = []
                    for elem in e:
                        list10.append(elem)
                    t.append(list10)
                if not t:
                    return render_template('Result.html', val=sql, data=t, my_val=column_name_list)
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
                coun8=0
                num=[]
                x_l=[]
                for i in x_list:
                    coun8+=1;
                    num.append(coun8)
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
                return render_template('hist.html', val=sql,url=path)
        else:
            column_name_list = val_inside(sql)
            enter1=0
            if Having_Condition:
                sql=sql+" GROUP BY "
                for col in column_name_list:
                    if "COUNT" in col:
                        continue
                    sql=sql+ col+","
                enter1=1
                sql = sql[:-1]
                Having_Condition=Having_Conditions_found(Having_Condition)
                sql=sql+Having_Condition
            str1=""
            if enter1==0:
                for text in column_name_list:
                    str1=text
                    if "COUNT" in text:
                        sql=sql+" GROUP BY "
                        for col in column_name_list:
                            if "COUNT" in col:
                                str1=col
                                continue
                            sql=sql+ col+","
                        sql = sql[:-1]
            if DESC:
                sql=sql +" ORDER BY " + str1+ " DESC "
            if ASC:
                sql=sql +" ORDER BY " +str1 +" ASC "
            if Limit:
                sql=sql + " LIMIT "+ Limit
            cursor.execute(sql)
            searchresults = cursor.fetchall()
            list1 = []#creating list of tuples
            for e in searchresults:
                list2 = []
                for elem in e:
                    list2.append(elem)
                list1.append(list2)
            return render_template('Result.html', val=sql, data=list1, my_val=column_name_list) 
    else:
        return render_template('Main.html')



@app.route('/Table2', methods=['POST'])
def Table2():
    if request.method == 'POST':
        Creator_Name=request.form['Creator']
        Hashtag=request.form['HashTag']
        Start_Date=request.form['Start_Date']
        End_Date=request.form['End_Date']
        ####Getting info from forms
        ###Building the sql query
        sql_query=""
        sql_query=sql_query+"SELECT DISTINCT HASHTAG, DATE,COUNT(*) FROM TikTokVideoHashTagInfoTable WHERE"
        if Creator_Name:
            sql_query=sql_query+" NAME='"+Creator_Name +"' AND "
        if Hashtag:
            sql_query=sql_query+ " HASHTAG='"+Hashtag+"'"
        if Start_Date:
            sql_query=sql_query+ " AND DATE>='"+Start_Date+"'"
        if End_Date:
            sql_query=sql_query+ " AND DATE<='"+End_Date+"'"
        sql_query=sql_query+" GROUP BY HASHTAG,DATE"
        ###########################
        column_name_list = val_inside(sql_query)#check which column are on result
        cursor.execute(sql_query)
        searchresults = cursor.fetchall()
        end_list = []#creating list of tuples
        date_list=[]
        HASHTAG_name=""
        Num_on_specific_date=[]
        cursor.execute('SELECT DISTINCT DATE FROM TikTokVideoHashTagInfoTable')
        date_result = cursor.fetchall()
        if len(searchresults)==0:
            return render_template('Main.html')
        poshashtag=column_name_list.index("HASHTAG")
        posdate=column_name_list.index("DATE")
        for k in searchresults:
            HASHTAG_name=k[poshashtag];
            date_list.append(k[posdate])
            Num_on_specific_date.append(k[2])
        Final=[]
        for i in range(0,len(date_list)):
            k=(date_list[i],Num_on_specific_date[i])
            Final.append(k)
        Final.sort()
        date_result_list=[]
        for i in date_result:
            date_result_list.append(i[0])
        date_result_list.sort()
        first=date_on_sql_query(sql_query,1,date_result_list)
        last=date_on_sql_query(sql_query,2,date_result_list)
        final_date_result=[]
        for a in date_result_list:
            date=a
            val1=compare_two_dates(date,first)
            val2=compare_two_dates(date,last)
            if val1>=0 and val2==-1:
                final_date_result.append(date)
            elif val1>=0 and val2==0:
                final_date_result.append(date)
        x1=[]
        x2=[]
        for a in final_date_result:
            date1=a
            found=0
            for b in Final:
                date2=b[0]
                if date1==date2:    
                    found=1
                    break
            if found==1:
                x1.append(b[1])
                x2.append(date1)
            else:
                k=0
                x1.append(k)
                x2.append(date1)
        plt.figure(figsize=(9, 3))
        plt.plot(x2, x1)
        plt.suptitle(HASHTAG_name)
        path="static/images/image"+str(time.time())+".png"
        for filename in os.listdir('static/images'):
            os.remove('static/images/' + filename)
        plt.xticks(rotation=90)
        plt.savefig(path, bbox_inches='tight')
        return render_template('Graphic.html', val=sql_query,url=path)
    else:
        return render_template('Main.html')


@app.route('/Table3', methods=['POST'])
def Table3():
    if request.method == 'POST':
        Creator_Name=request.form['Creator']
        Word=request.form['Word']
        Start_Date=request.form['Start_Date']
        End_Date=request.form['End_Date']
        ##Getting Info from forms
        ##Building sql query
        sql_query=""
        sql_query=sql_query+"SELECT DISTINCT WORD, DATE,COUNT(DATE) FROM TikTokVideoWord WHERE"
        if Creator_Name:
            sql_query=sql_query+" NAME='"+Creator_Name +"' AND "
        if Word:
            sql_query=sql_query+ " WORD='"+Word+"'"
        if Start_Date:
            sql_query=sql_query+ " AND DATE>='"+Start_Date+"'"
        if End_Date:
            sql_query=sql_query+ " AND DATE<='"+End_Date+"'"
        sql_query=sql_query+" GROUP BY WORD,DATE"
        column_name_list = val_inside(sql_query)#check which column are on result
        ##########################
        hist_create=0
        cursor.execute(sql_query)
        searchresults = cursor.fetchall()
        end_list = []#creating list of tuples
        WORD_NAME=""
        Num_on_specific_date=[]
        date_list=[]
        cursor.execute('SELECT DISTINCT DATE FROM TikTokVideoWord')
        date_result = cursor.fetchall()
        if len(searchresults)==0:
            return render_template('Main.html')
        posword=column_name_list.index("WORD")
        posdate=column_name_list.index("DATE")
        for k in searchresults:
            WORD_NAME=k[posword];
            date_list.append(k[posdate])
            Num_on_specific_date.append(k[2])
        #checking if on specific date  has no value
        date_list.sort()
        date_result_list=[]
        for i in date_result:
            date_result_list.append(i[0])
        date_result_list.sort()
        first=date_on_sql_query(sql_query,1,date_result_list)
        last=date_on_sql_query(sql_query,2,date_result_list)
        final_date_result=[]
        for a in date_result_list:
            date=a
            val1=compare_two_dates(date,first)
            val2=compare_two_dates(date,last)
            if val1>=0 and val2==-1:
                final_date_result.append(date)
            elif val1>=0 and val2==0:
                final_date_result.append(date)
        for g in final_date_result:
            date1=g
            found=0
            pos=0
            for k in searchresults:
                date2=k[posdate]
                val=compare_two_dates(date1,date2)
                if val==-1:
                    break
                if val==0:
                    found=1
                    break
                pos+=1
            if found==0:
                date_list.insert(pos,date1)
                Num_on_specific_date.insert(pos,0)
        plt.figure(figsize=(9, 3))
        plt.plot(date_list, Num_on_specific_date)
        plt.suptitle(WORD_NAME)
        path="static/images/image"+str(time.time())+".png"
        for filename in os.listdir('static/images'):
            os.remove('static/images/' + filename)
        plt.xticks(rotation=90)
        plt.savefig(path, bbox_inches='tight')
        return render_template('Graphic.html', val=sql_query,url=path)
    else:
        return render_template('Main.html')





def word_list(my_string):
    wordList=re.findall(r'\s|,|[^,\s]+',my_string)
    return wordList


##Function that returns the columns that have to show the query 
def val_inside(my_string):
    my_string=word_list(my_string)
    my_list = []
    if "TikTokVideoWord" in my_string:
        for keyword in my_string:
            if "FROM" == keyword:
                return my_list
            if "ID" == keyword:
                my_list.append("ID")
            if "*" == keyword:
                my_list.append("ID")
                my_list.append("NAME")
                my_list.append("DATE")
                my_list.append("WORD")
                return my_list
            if "COUNT(ID)" == keyword:
                my_list.append("COUNT(ID)")
            if "COUNT(NAME)" == keyword:
                my_list.append("COUNT(NAME)")
            if "COUNT(DATE)" == keyword:
                my_list.append("COUNT(DATE)")
            if "COUNT(WORD)" == keyword:
                my_list.append("COUNT(WORD)")
            if "DATE" == keyword:
                my_list.append("DATE")
            if "NAME" == keyword:
                my_list.append("NAME")
            if "COUNT(*)" == keyword:
                my_list.append("COUNT(*)")
            if "WORD"  == keyword:
                my_list.append("WORD")
        return my_list
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





#Function that checks dates 
def compare_two_dates(date1,date2):
    year1=date1[:4]
    year2=date2[:4]
    if year1>year2:
        return 1
    elif year1<year2:
        return -1
    month1=date1[5:7]
    month2=date2[5:7]
    if month1>month2:
        return 1
    elif month1<month2:
        return -1
    day1=date1[8:10]
    day2=date2[8:10]
    if day1>day2:
        return 1
    elif day1==day2:
        return 0
    else:
        return -1

#Function that return the date(s) given on sql query
def date_on_sql_query(sql,whichone,date_result_list):
    matches = datefinder.find_dates(sql)
    matches=list(matches)
    matches.sort()
    s=[]
    for match in matches:
        x=match.date()
        x=x.strftime('%Y/%m/%d')
        s.append(x)
    if whichone==1: 
        if ">=" in sql:
            return s[0]
        else: 
            return date_result_list[0]
    if whichone==2:
        if "<=" in sql:
            return s[-1]
        else:
            return date_result_list[-1]



###Function that returns the where conditions on sql
def Where_Conditions_found(Where):
    expression=" WHERE "
    some=""
    for i in range(0,len(Where)):
        if Where[i]==',' or Where[i]==' ':
            expression=expression+some
            some=""
        some=some+Where[i]
    expression=expression+some
    return expression

###Function that returns   the having conditions on sql 
def Having_Conditions_found(Having):
    expression=" HAVING "
    some=""
    for i in range(0,len(Having)):
        if Having[i]==',' or Having[i]==' ':
            expression=expression+some
            some=""
        some=some+Having[i]
    expression=expression+some
    return expression


if __name__ == '__main__':
    app.run()