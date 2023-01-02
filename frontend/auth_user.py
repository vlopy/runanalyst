from flask_login import UserMixin

class User(UserMixin):
 def __init__(self, db_id, db_email):
    self.id = db_id
    self.email = db_email

def is_active(self):
    # Here you should write whatever the code is
    # that checks the database if your user is active
    return True

def is_anonymous(self):
    return False

def is_authenticated(self):
    return True