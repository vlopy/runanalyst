# runanalyst
Analyze GPX files to compute run stats.

# Test the backend with docker (Java + MySQL)
- Build the container
```
cd docker
docker build -t mysql-test .
```
- Start the container
```
docker run -p 3306:3306 -d mysql-test:latest
mysql -uroot -prunpwd -h 127.0.0.1
```
- Connect to the mariadb server
```
mysql -uroot -prunpwd -h 127.0.0.1
```
- Create the database
```
CREATE DATABASE runanalyst;
```
- Analyze the GPX files

# Test the frontend (Python)
- Install Python tools
```
sudo apt install -y python3-venv
```
- Create a virtual environment
```
mkdir env
cd env
python3 -m venv env
```
- Activate your virtual environment
```
source env/bin/activate
# To deactivate the environment, run 'deactivate'
```
- Install flask
```
pip install flask
```

