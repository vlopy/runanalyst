from flask import Flask, redirect, render_template, request, session, url_for
from flask_login import LoginManager
from auth import auth as auth_blueprint;
from main import main as main_blueprint;
from database import database
from auth_user import User

app = Flask(__name__)

login_manager = LoginManager()
login_manager.login_view = 'auth.login'
login_manager.init_app(app)
app.config['SECRET_KEY'] = 'secret-key-goes-here'
app.config['UPLOAD_FOLDER'] = "userdata"
app.config['MAX_CONTENT_PATH'] = 900000

app.register_blueprint(auth_blueprint)
app.register_blueprint(main_blueprint)

@login_manager.user_loader
def load_user(user_id):
    statement = "SELECT * FROM users WHERE user_id=%s" % user_id
    cursor = database.cursor();
    cursor.execute(statement)
    results = cursor.fetchall()
    if len(results) > 0:
        (db_id, db_email, db_password, db_name) = results[0]
        return User(db_id, db_email)