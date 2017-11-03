# ARK Blockchain Analyzer

The project allows to analyze current ARK delegates statistics. It can be helpful for potential delegates to get some insights about attracting new voters. 

## Usage
 
1. Create a database in MySQL:

    `create database ark_analyzer;`

2. Build the project with Maven:

    `mvn clean install`

3. Run Spring Boot application:

    `java -jar target/ark-analyzer-0.1-SNAPSHOT.jar`

4. Open URL: http://localhost:8080
