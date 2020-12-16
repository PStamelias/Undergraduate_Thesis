from flask import Flask, redirect, url_for, render_template
import psycopg2
from flask import request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import text
app = Flask(__name__, template_folder='template')
con = psycopg2.connect(database="prokopis", user="prokopis",password="123", host="127.0.0.1", port="5432")
cursor = con.cursor()

@app.route("/")
def home():
    return render_template('index.html')

@app.route("/login", methods=['POST'])
def login():
    if request.method == 'POST':
        sql_query = request.form['query']
        column_name_list = val_inside(sql_query)#check which column are on result
        cursor.execute(sql_query)
        searchresults = cursor.fetchall()
        t = []#creating list of tuples
        for e in searchresults:
            ko = []
            for elem in e:
                ko.append(elem)
            t.append(ko)
        return render_template('de.html', val=sql_query, data=t, my_val=column_name_list)
    else:
        return render_template('index.html')


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
    return my_list


if __name__ == "__main__":
    app.run()
