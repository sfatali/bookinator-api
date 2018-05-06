---------------------Steps to set up the project:---------------------


1) Install Java (https://www3.ntu.edu.sg/home/ehchua/programming/howto/JDK_Howto.html)

2) Install Maven (https://maven.apache.org/install.html)

3) Install PostgreSQL database (https://www.postgresql.org/download/windows/). When setting up, make sure user
   “postgres” is assigned a password “postgres”

4) Create database called bookinator and assign user postgres as an owner

5) Import project to an IDE (but as you wish, cmd can be still sufficient)

6) In the project's root folder run "mvn flyway:migrate".
   That will run all the database migrations, setting up the db schema and initializing with data

7) If you touch something in those migrations (sql files located in bookinator\src\main\resources\db\migration),
   you might get in trouble. To add any new sql scripts, create a new migration file (e.g V3__new.sql).
   If you really need to edit existing scripts, you should drop the database and run the command from step 6 again.
   If it anything looks suspicious rebuild the project (either find the button in IDE or run "mvn clean install").

7) To run all the tests, use command "mvn test"

8) There is no separate test database. It can be added easily, but for the sake of setup simplicity it was not.
   However, all the SQL transactions taking place in DAO unit tests get rolled back upon test completion,
   so no worries here.

9) To run the project, navigate to bookinator\src\main\java\com\bookinator\api and run the BookinatorApplication.java.
   In IDE it can be done with one click on the run button. In cmd, run "mvn spring-boot:run".

10) To view dependencies or add a new one, go to pom.xml in the project root folder.


---------------------An overview of supporting technologies used in the project:---------------------


1) Spring Boot framework - whole project is based on that

2) Flyway - database migration tool

3) JUnit - for unit testing

4) MyBatis - for mapping sql with Java

