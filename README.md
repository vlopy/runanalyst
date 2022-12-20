# runanalyst
Analyze GPX files to compute run stats.

# Test the code with docker
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
