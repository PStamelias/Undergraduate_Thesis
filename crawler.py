from flask import Flask, redirect, url_for,render_template
import psycopg2
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.sql import text
app = Flask(__name__,template_folder='template')
con = psycopg2.connect(database="prokopis", user="prokopis", password="123", host="127.0.0.1", port="5432")
cursor = con.cursor()
@app.route("/")
def login():
	return render_template("index.html")

if __name__ == "__main__":
	app.run()