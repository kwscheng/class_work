from flask import Flask, render_template, request
from werkzeug.utils import secure_filename
from werkzeug.datastructures import FileStorage
import subprocess
import os
app = Flask(__name__)

@app.route('/upload')
def upload_file():
   return render_template('upload.html')

@app.route('/upload', methods = ['GET', 'POST'])
def uploader():
   if request.method == 'POST':
      f = request.files['file']
      f.save(secure_filename(f.filename))
      score = subprocess.check_output(["python","./autograde.py"])
      return str(score)
      #return 'file uploaded successfully'

@app.route('/')
def welcome():
    return  'go to /upload'

if __name__ == '__main__':
   app.run(debug = True,host="0.0.0.0", port=int(os.environ.get("PORT",5000)))
