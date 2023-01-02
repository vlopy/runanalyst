from flask import Blueprint, flash, redirect, render_template, request, url_for
from flask_login import current_user, login_user, login_required, logout_user
from werkzeug.security import generate_password_hash, check_password_hash
from database import database
from auth_user import User

auth = Blueprint('auth', __name__)


@auth.route('/login')
def login():
    if current_user.is_authenticated:
        return redirect(url_for('main.home'))
    return render_template('login.html')


@auth.route('/login', methods=['POST'])
def login_post():
    email = request.form.get('email')
    password = request.form.get('password')
    # Check the password
    cursor = database.cursor();
    statement = "SELECT * FROM users WHERE email='%s'" % email
    cursor.execute(statement)
    results = cursor.fetchall()
    if len(results) > 0:
        (db_id, db_email, db_password, db_name) = results[0]
        if not check_password_hash(db_password, password):
            flash('Please check your login details and try again.')
            return redirect(url_for('auth.login'))
        user = User(db_id, db_email)
        login_user(user, True)
        return redirect(url_for('main.home'))
    else:
        flash('Please check your login details and try again.')
        return redirect(url_for('auth.login'))


@auth.route('/signup')
def signup():
    if current_user.is_authenticated:
        return redirect(url_for('main.home'))
    return render_template('signup.html')


@auth.route('/signup', methods=['POST'])
def signup_post():
    email = request.form.get('email')
    name = request.form.get('name')
    password = request.form.get('password')
    cursor = database.cursor();
    try:
        statement = "SELECT * FROM users WHERE email='%s'" % email
        cursor.execute(statement)
        results = cursor.fetchall()
        if len(results) == 1:
            # email already exists, send the user to the login page
            cursor.close()
            return redirect(url_for('auth.login'))
        # Create the user in the database
        statement = "INSERT INTO users (email, password, name) VALUES ('%s', '%s', '%s')" % (email, generate_password_hash(password, method='sha256'), name)
        cursor.execute(statement)
        database.commit()
    except:
        print("Error with the database management")
    cursor.close()
    return redirect(url_for('auth.login'))


@auth.route('/logout')
@login_required
def logout():
    logout_user()
    flash("Logged out.")
    return redirect(url_for("main.index"))