from database import database
from flask import Blueprint, redirect, render_template, request, url_for
from flask_login import login_required, current_user
from werkzeug.utils import secure_filename
from datetime import datetime
import json, os


main = Blueprint('main', __name__)


@main.route('/')
def index():
    return redirect(url_for('auth.login'))


@main.route('/home')
@login_required
def home():
    cursor = database.cursor();
    statement = "SELECT * FROM tracks ORDER BY timestamp LIMIT 10;"
    cursor.execute(statement)
    results = cursor.fetchall()
    lastRunDistance = []
    lastRunTime = []
    lastRunDate = []
    lastRunPace = []
    for (timestamp, distance, time, _) in results:
        date = datetime.fromtimestamp(timestamp)
        lastRunDate.append(date.strftime("%d/%m %H:%M"));
        lastRunDistance.append(distance)
        lastRunTime.append(time)
        # Pace: time in seconds per km
        lastRunPace.append(int(time / (distance / 1000)))
    return render_template("home.html",
        lastruns_dt = lastRunDate, lastruns_d = lastRunDistance,
        lastruns_t = lastRunTime, lastruns_p = lastRunPace)


@main.route("/upload", methods=["POST"])
@login_required
def upload():
    uploaded_files = request.files.getlist("file[]")
    dir_path = "userdata/%s" % current_user.email
    if not os.path.isdir(dir_path):
        os.mkdir(dir_path)
    for file in uploaded_files:
            file.save("%s/%s" % (dir_path, secure_filename(file.filename)))
    return redirect(url_for('main.home'))