from flask import Flask, redirect, url_for, render_template,request
import os.path,subprocess
from subprocess import STDOUT,PIPE
app = Flask(__name__, template_folder='template')



def compile_java(java_file):
    subprocess.check_call(['javac','-cp','.:log4j-api-2.11.1.jar:lucene-7.11.2.0.jar:lucene-core-8.7.0.jar:models-7.11.2.0.jar:lucene-queries-7.3.1.jar:lucene-queryparser-7.3.1.jar:lucene-sandbox-7.3.1.jar:lucene-analyzers-common-8.2.0.jar:lucene-core-8.2.0.jar', java_file])


def execute_java(java_file,type,Query):
    java_class,ext = os.path.splitext(java_file)
    cmd = ['java','-cp','.:log4j-api-2.11.1.jar:lucene-7.11.2.0.jar:lucene-core-8.7.0.jar:models-7.11.2.0.jar:lucene-queries-7.3.1.jar:lucene-queryparser-7.3.1.jar:lucene-sandbox-7.3.1.jar:lucene-analyzers-common-8.2.0.jar:lucene-core-8.2.0.jar', java_class,type,Query]
    proc = subprocess.Popen(cmd, stdout=PIPE, stderr=STDOUT)
    stdout,stderr = proc.communicate()
    stdoute=stdout.decode()
    return stdoute

########################
@app.route('/')
def home():
    return render_template('Main_Page.html')


@app.route("/login", methods=['POST'])
def login():
    if request.method == 'POST':
        compile_java('Search.java')
        result=""
        value_id = request.form.getlist('ID')
        value_name = request.form.getlist('Name')
        value_SoundTag = request.form.getlist('Sound_Tag')
        value_Date = request.form.getlist('Date')
        Query=request.form['Query']
        if value_id==['edit']:
            result=execute_java('Search.java','id',Query)
            return render_template('result.html', val1="ID",val2=Query,val3=result)
        elif value_name==['edit']:
            result=execute_java('Search.java','name',Query)
            return render_template('result.html', val1="Name",val2=Query,val3=result)
        else:
            return render_template('Main_Page.html')
    else:
        return render_template('Main_Page.html')


if __name__ == '__main__':
    app.run()