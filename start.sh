docker run -p 3306:3306 -d mysql-test:latest
sleep 1
mysql -uroot -prunpwd -h 127.0.0.1 -e "CREATE DATABASE runanalyst;"
cd backend
files=$(find ../backend/gpxfiles/ -name *gpx | sed 's:backend/::'  | tr '\n' ' ')
gradle run --args "$files"
mysql -uroot -prunpwd -h 127.0.0.1 runanalyst -e "CREATE TABLE users (user_id INT PRIMARY KEY AUTO_INCREMENT, email VARCHAR(100) NOT NULL, password VARCHAR(100) NOT NULL, name VARCHAR(100));"
mysql -uroot -prunpwd -h 127.0.0.1 runanalyst -e "INSERT INTO users VALUES(1, 'remy.mail@mailo.com', 'sha256$kRIHX9AlgtAGLfX2$ed8753bfb787f2689385b5a547e9ba6339911c4c7e5e8cd33c081b11ab0ed3ae', 'rp');"
cd ../frontend
source env/bin/activate
flask run
