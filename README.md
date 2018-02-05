# Kata Bank Account

Requirements :

- Write some code to create a bank application and to withdraw/deposit a valid amount of money in/from the account

- Write some code to transfer a specified amount of money from one bank account (the payer) to another (the payee)

- Write some code to keep a record of the transfer for both bank accounts in a transaction history

- Write some code to query a bank account's transaction history for any bank transfers to or from a specific account



### Prerequisites

Things to be installed to be able to run the kata :
```
- Java 8
- Maven
- Mysql

```


### DataBase configuration

Data base config file is located in :
/src/main/resources/application.properties
```
- Config your Database url, username and password.
- Config the spring.jpa.hibernate.ddl-auto ( create , update, none ...).
- Config the port to be used for the api.
```


## Running the api

Run the app with this maven command :
```
mvn spring-boot:run
```
