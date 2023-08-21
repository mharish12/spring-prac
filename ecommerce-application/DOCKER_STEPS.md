# Docker Setup Steps

## Pull the image
Pull the latest (or version) docker image
You can check the available images on: https://hub.docker.com/

```shell script
docker pull mysql:latest
docker pull postgres:latest
docker pull mongo:latest
docker pull cassandra:latest
docker pull yugabytedb/yugabyte:latest
docker pull redis:latest
docker pull mcr.microsoft.com/mssql/server
docker pull icr.io/ibm-messaging/mq:latest
docker pull webcenter/activemq:latest
```


## Check the version

```shell script
docker image inspect mysql | grep MYSQL_VERSION | uniq
docker image inspect postgres | grep PG_VERSION | uniq
docker image inspect mongo | grep MONGO_VERSION | uniq
docker image inspect cassandra | grep CASSANDRA_VERSION | uniq
docker image inspect redis | grep REDIS_VERSION | uniq
docker image inspect mcr.microsoft.com/mssql/server | grep com.microsoft.version | uniq
docker image inspect icr.io/ibm-messaging/mq | grep "LABEL version"
```

Note: For yugabyte the version info doesn't seem to be available in docker inspect


## Run
The run command will create a container and then start it.
Read more about possible options: https://docs.docker.com/engine/reference/commandline/run/

```shell script
docker run --name h12-mysql -e MYSQL_ROOT_PASSWORD=password -p 2001:3306 -d mysql:latest
docker run --name h12-cassandra -d -p 2003:9042 cassandra:latest
docker run --name h12-mongo -d -p 2004:27017 -e MONGO_INITDB_ROOT_USERNAME=h12-user -e MONGO_INITDB_ROOT_PASSWORD=h12-password mongo:latest
docker run --name h12-pgsql -d -e POSTGRES_PASSWORD=root123 -p 2006:5432 postgres:latest
docker run --name h12-yugabyte -d -p2021:7000 -p2010:9000 -p2023:5433 -p2011:9042 \
  -v ~/yb_data:/home/yugabyte/var \
  yugabytedb/yugabyte:latest bin/yugabyted start --daemon=false
docker run --name h12-redis -p 2002:6379 -d redis:latest
docker run --name h12-mssql -e "ACCEPT_EULA=Y" -e "SA_PASSWORD=mySecurePW123" -p 2008:1433 -d mcr.microsoft.com/mssql/server:latest
# IBM MQ
# 1. Create a volume
docker volume create qm1data
# 2. Run the image
docker run --env LICENSE=accept --env MQ_QMGR_NAME=QM1 --volume qm1data:/mnt/mqm --publish 1414:1414 --publish 9443:9443 --detach --env MQ_APP_USERNAME=app --env MQ_APP_PASSWORD=password --name QM1 icr.io/ibm-messaging/mq:latest

# Active MQ
# Can connect to Active Mq via http://localhost:8161/ to check queue and topic deatils.
docker run -d -p 8161:8161 -p 61616:61616 -p 5672:5672 -e ACTIVEMQ_ADMIN_LOGIN=admin -e ACTIVEMQ_ADMIN_PASSWORD=password --name h12-active-mq webcenter/activemq:latest
```

In order to stop the container use:
```shell script
docker stop <name>
```

In order to restart the container use:
```shell script
docker start <name>
```

## Cassandra

Connect to cassandra via cqlsh
```shell script
docker exec -it h12-cassandra cqlsh -p 2003
```

Create a testable data
```cassandraql
CREATE KEYSPACE h12_test WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
-- DESCRIBE keyspaces;
USE h12_test;
CREATE TABLE employee(
  id int PRIMARY KEY,
  email text,
  name text,
  phone text,
  city text
);
INSERT INTO employee (id, email, name, phone, city)
  VALUES(1, 'ram@cassandratest.com', 'Ram', '9848022338', 'Bangalore');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(2, 'robin@cassandratest.com', 'Robin', '9848022339', 'Hyderabad');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(3, 'rahman@cassandratest.com', 'Rahman', '9848022330', 'Chennai');
```

## Yugabyte

Connect to yugabyte via ycqlsh
```shell script
docker exec -it h12-yugabyte ycqlsh
```

Create a testable data
```cassandraql
CREATE KEYSPACE IF NOT EXISTS h12_test WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};
--DESCRIBE keyspaces;
USE h12_test;
CREATE TABLE employee(
  id int PRIMARY KEY,
  email text,
  name text,
  phone text,
  city text
);
INSERT INTO employee (id, email, name, phone, city)
  VALUES(1, 'ram@ycqltest.com', 'Ram', '9848022338', 'Bangalore');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(2, 'robin@ycqltest.com', 'Robin', '9848022339', 'Hyderabad');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(3, 'rahman@ycqltest.com', 'Rahman', '9848022330', 'Chennai');
```

## Postgresql

Connect to postgresql via psql
```shell script
 docker exec -it h12-pgsql psql -U postgres -h <your-non-localhost-ip> -p 2006
```
Create a testable data
```postgresql
-- DB for ORM Testing (Table will automatically create when application boot up)
CREATE DATABASE h12_test_orm;
-- DB for normal sql test with some date
CREATE DATABASE h12_test;
-- In the next step, we connect to the database before creating the table
\c h12_test
CREATE TABLE employee (
id serial PRIMARY KEY,
name varchar (50) ,
email varchar(40),
phone varchar (40) ,
city varchar(40)
);

INSERT INTO employee (id, email, name, phone, city)
  VALUES(1, 'ram@posgresqltest.com', 'Ram', '9848022338', 'Bangalore');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(2, 'robin@postgresqltest.com', 'Robin', '9848022339', 'Hyderabad');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(3, 'rahman@postgresqltest.com', 'Rahman', '9848022330', 'Chennai');
```

## Mysql

Connect to mysql via mysql
```shell script
 docker exec -it h12-mysql mysql -u root -ppassword
```
Create a testable data
```mysql
-- DB for ORM Testing (Table will automatically create when application boot up)
CREATE DATABASE h12_test_orm;
-- DB for normal sql test with some date
CREATE DATABASE h12_test;
-- In the next step, we connect to the database before creating the table
use h12_test;
CREATE TABLE employee (
id serial PRIMARY KEY,
name varchar (50) ,
email varchar(40),
phone varchar (40) ,
city varchar(40)
);

INSERT INTO employee (id, email, name, phone, city)
  VALUES(1, 'ram@mysqltest.com', 'Ram', '9848022338', 'Bangalore');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(2, 'robin@mysqltest.com', 'Robin', '9848022339', 'Hyderabad');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(3, 'rahman@mysqltest.com', 'Rahman', '9848022330', 'Chennai');
```

## Microsoft Sql (MSSql)

Connect to mssql from terminal
```shell script
docker exec -it h12-mssql /opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P mySecurePW123
```
Create a testable data
```sql
-- DB for normal sql test with some date
create database h12_test;
go
-- In the next step, we connect to the database before creating the table
use h12_test;
go

CREATE TABLE employee (
id int PRIMARY KEY,
name varchar (50),
email varchar(40),
    phone varchar (40),
city varchar(40)
);
go

INSERT INTO employee (id, email, name, phone, city)
  VALUES(1, 'ram@mssqltest.com', 'Ram', '9848022338', 'Bangalore');
go

INSERT INTO employee (id, email, name, phone, city)
  VALUES(2, 'robin@mssqltest.com', 'Robin', '9848022339', 'Hyderabad');
go

INSERT INTO employee (id, email, name, phone, city)
  VALUES(3, 'rahman@mssqltest.com', 'Rahman', '9848022330', 'Chennai');
go
```

## Ysql
Connect to ysql via ysqlsh
```shell script
docker exec -it h12-yugabyte ysqlsh
```

Create a testable data
```postgresql
CREATE DATABASE h12_test;
-- In the next step, we connect to the database before creating the table
\c h12_test
CREATE TABLE employee (
id serial PRIMARY KEY,
name varchar (50) ,
email varchar(40),
phone varchar (40) ,
city varchar(40)
);

INSERT INTO employee (id, email, name, phone, city)
  VALUES(1, 'ram@ysqltest.com', 'Ram', '9848022338', 'Bangalore');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(2, 'robin@ysqltest.com', 'Robin', '9848022339', 'Hyderabad');
INSERT INTO employee (id, email, name, phone, city)
  VALUES(3, 'rahman@ysqltest.com', 'Rahman', '9848022330', 'Chennai');
```

## Redis

Connect to redis shell
```shell script
docker exec -it h12-redis redis-cli
```

Test the connectivity
```shell
# list all the keys present in redis
keys *
# put a (key,val) pair
set testKey 123
# list  the keys again
keys *
# get the val of the test key
get testKey
# remove the test key
del testKey
# list  the keys again
keys *
# exit from the redis-cli
quit
```

## Mongo
Connect to mongo shell
```shell
docker exec -it h12-mongo mongosh -u h12-user -p h12-password
```
Test connectivity
```shell
#Create and use a databse
use h12-database
#Create collection in databse
db.createCollection("employees")
#Insert documents in collection
db.employees.insertOne({ name:"john", email:"john.doe@abc.abc", phone:"345345345", city:"test-city"})
db.employees.insertOne({ name:"doe", email:"john.doe@abc.abc", phone:"3242343423", city:"test-city"})
```

## Not Used
```shell script
docker run --name krogo-redis -p 2002:6379 -d redis:latest
docker run --name krogo-zipkin -d -p 2005:9411 openzipkin/zipkin:latest
docker run --name krogo-mssql -d -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=reallyStrongPwd123' -p 2007:1433 microsoft/mssql-server-linux
docker run --rm -d -p 2181:2181 -p 443:2008 -p 2008:2008 -p 2009:2009 \
    --env ADVERTISED_LISTENERS=PLAINTEXT://kafka:443,INTERNAL://localhost:2009 \
    --env LISTENERS=PLAINTEXT://0.0.0.0:2008,INTERNAL://0.0.0.0:2009 \
    --env SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,INTERNAL:PLAINTEXT \
    --env INTER_BROKER=INTERNAL \
    --env KAFKA_CREATE_TOPICS="test:36:1,krisgeus:12:1:compact" \
    --name krogo-kafka \
    krisgeus/docker-kafka

```

# NON-DOCKER SETUPS

## Kafka

Download latest kafka by visiting the download link on
https://kafka.apache.org/quickstart

```shell script
# Download (update to the link you copied)
wget https://ftp.wayne.edu/apache/kafka/2.6.0/kafka_2.13-2.6.0.tgz

# Unzip (the downloaded file)
tar -xvzf kafka_2.13-2.6.0.tgz

# If on mac and want to move it to some path (optional step)
mv kafka_2.13-2.6.0 ~/Applications/.

# Update the server.properties to advertise localhost as the listener
cd ~/Applications/kafka_2.13-2.6.0/config
vim server.properties
# update --> advertised.listeners=PLAINTEXT://localhost:9092

cd ~/Applications/kafka_2.13-2.6.0/

# Start the ZooKeeper service
# Note: Soon, ZooKeeper will no longer be required by Apache Kafka.
bin/zookeeper-server-start.sh config/zookeeper.properties

# Start the Kafka broker service
bin/kafka-server-start.sh config/server.properties
# OR using the below command.
# TODO: Not sure why JMX port is being changed. Does it conflict?
# JMX_PORT=8004 bin/kafka-server-start.sh config/server.properties

# Create a topic (h12-test)
bin/kafka-topics.sh --create --topic h12-test --bootstrap-server localhost:9092

# Check the created topic (h12-test)
bin/kafka-topics.sh --describe --topic h12-test --bootstrap-server localhost:9092

# To run a sample producer (h12-test)
bin/kafka-console-producer.sh --topic h12-test --bootstrap-server localhost:9092

# To run a sample consumer (h12-test)
bin/kafka-console-consumer.sh --topic h12-test --from-beginning --bootstrap-server localhost:9092
```
## CosmosDB

Open http://portal.azure.com/ and Create a resource "Azure Cosmos DB"
#### Fill up the details
```
Subscription
Resource Group
Account Name
API
Notebooks
Location
Capacity modee
Account Type
Geo-Redudency
Multi Region Writes
```
Review and create the resource
After deployment is finished we will have a connection string and resource details.
Extract AZURE_ACCOUNT_KEY, AZURE_ACCOUNT_HOST from resource details.