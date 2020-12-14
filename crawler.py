from flask import Flask, redirect, url_for,render_template
import psycopg2
from flask import request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import text
app = Flask(__name__,template_folder='template')
con = psycopg2.connect(database="prokopis", user="prokopis", password="123", host="127.0.0.1", port="5432")
cursor = con.cursor()
@app.route("/")
def home():
		return render_template('index.html')
@app.route("/login", methods=['POST'])
def login():
	 if request.method == 'POST':
	 	sql_query=request.form['query']
	 	print(sql_query)
	 	return render_template('index.html')                                                                    
	 else:
	 	return render_template('index.html')
if __name__ == "__main__":
	app.run()