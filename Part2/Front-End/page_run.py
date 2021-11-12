from flask import Flask, redirect, url_for, render_template,request
import os.path,subprocess
from random import shuffle
from subprocess import STDOUT,PIPE
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


########################
@app.route('/')
def home():
    return render_template('Main_Page.html')


@app.route("/Search", methods=['POST'])
def Search():
    Query = request.form.getlist('Query')
    Input = Query[0]
    compile_java('Search.java')
    result=execute_java('Search.java',Input)
    data=[]
    record=""
    for ch in result:
        if ch=='~':
            data.append(record)
            record=""
            continue
        if ch=='\n':
            continue
        record=record+ch
    shuffle(data)#sort randomly the list to show in random the results
    return render_template('result.html',Search_value=Input,list=data)


if __name__ == '__main__':
    app.run()