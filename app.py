from flask import Flask, render_template
import os
app = Flask(__name__,static_url_path='', static_folder='static',template_folder='templates')
port = int(os.environ.get("PORT", 5000))
@app.route('/')
def index():
    return render_template('index.html')

@app.route('/sender/sign-up.html')
def signup():
    return render_template('sign-up.html')

if __name__ == '__main__':
    app.run(debug=True,host='0.0.0.0',port=port)