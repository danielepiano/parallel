# parallel
Management for enhanced coworking spaces: palestre relazionali.

Project for **Software Engineering** course @ **Politecnico di Milano**.

### Requirements
* Java v. >= 17.0.5
* Docker v. >= 20.10.23

### Setup
1. Run database _parallel-db_*
```
   $ docker run -d --name "parallel-db" -p 5432:5432 \
    --env "POSTGRES_DB=parallel" \
    --env "POSTGRES_USER=parallel" \
    --env "POSTGRES_PASSWORD=parallel" \
    postgres
```
2. Run mail server _parallel-ms_*
```
   $ docker run -d --name "parallel-ms" -p 1080:1080 -p 1025:1025 maildev/maildev
```
3. Clone _spring-core_ repository
```
   /path/to/.../be $ git clone https://github.com/danielepiano/spring-core.git
```
4. Clone _parallel_ repository
```
   /path/to/.../be $ git clone https://github.com/danielepiano/parallel.git
```
5. Install _spring-core_ project
```
   /path/to/.../be/spring-core $ mvn clean install
```
6. Install _parallel_ project
```
   /path/to/.../be/parallel $ mvn clean install
```
7. Run _parallel_ on desired IDE

<br>
* If you change ports or environment variables, remember to update the **application.yml** file in _parallel/src/main/resources_
