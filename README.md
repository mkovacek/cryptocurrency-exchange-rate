# cryptocurrency-exchange-rate service

### About 

Service that constantly checks the currency exchange rate from Bitcoin to US-Dollar.
Stores collected data into database and provides HTTP API endpoints to get latest rate and historical rate for provided start and end date.

#### Requirements
The check period has to be configurable

The service has a HTTP-API with the following endpoints:

Get latest rate

Get historical rates from startDate to endDate

### Implementation

After application start-up, in database are imported BTC rates for latest 30 days (for purpose of easier testing historical rate endpoint).

In background Scheduler is scrapping bitcoin rate and stores rate in database. 
Cron expression can be configurable in application properties.
Using API endpoints you can get latest bitcoin rate and historical rates from database.

Class diagram

![class diagram](https://i.ibb.co/mcYJmSX/Screenshot-2019-12-17-at-17-46-10.png)

Technologies

Spring boot, Java 11, H2 database

### Example Endpoints:
Get latest rate:
http://localhost:8080/api/v1/cryptocurrency/rate/latest/btc
 
Get historical rates from startDate to endDate
http://localhost:8080/api/v1/cryptocurrency/rate/historical/btc?startDate=2019-11-20&endDate=2019-12-10

### Instructions
Build: mvn clean install

Run: mvn spring-boot:run

Test: mvn clean test

### Why like this (my approach to the task)

Regarding technologies I decided to use what is on your tech radar list so Spring boot 2 and Java 11. Database (H2) is not appropiate and I have use it just for simplicity of running and testing this application (even though I could prepare some docker to run more appropiate database). 

For collecting latest cryptocurrency price I have decide to use scrapping library beacuse I thought that you use that kind of approach in daily business.

For easier testing of historical rate API endpoint I have decided to import some sample data using by other exchange rate service.

Requierment was only for BTC and USD but during implementation I had on my mind that this should be easy reusable / extendable  for other cryptocurrencies and exchange rates (eth, xrp, euros, ...)

Even though code is not perfect or there are some code smells/bugs, my focus was to prove Java skills, code organization and structuring. For real production ready that kind of service it would need a lot of more things to be considered and implemented.
