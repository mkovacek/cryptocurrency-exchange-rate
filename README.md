# cryptocurrency-exchange-rate service

###About 

Service that constantly checks the currency exchange rate from Bitcoin to US-Dollar.
Stores collected data into database and provides HTTP API endpoints to get latest rate and historical rate for provided start and end date.

### Implementation

After application start-up in database is imported BTC rate for latest 30 days (for purpose of easier testing historical rate endpoint).

In background Scheduler is scrapping bitcoin rate and stores rate in database. 
Cron expression can be configurable in application properties.
Using API endpoints you can get latest bitcoin rate and historical rates from database.

####Example endpoints:
Get latest rate:
http://localhost:8080/api/v1/cryptocurrency/rate/latest/btc
 
Get historical rates from startDate to endDate
http://localhost:8080/api/v1/cryptocurrency/rate/historical/btc?startDate=2019-11-20&endDate=2019-12-10

### Instructions
Build: mvn clean install

Run: mvn spring-boot:run

Test: mvn clean test

### Technologies
Spring boot, Java 11, H2 database

![class diagram](https://i.ibb.co/mcYJmSX/Screenshot-2019-12-17-at-17-46-10.png)
