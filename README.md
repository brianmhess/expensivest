# README


## Notes
### Getting Started
To generate the basic pom.xml I navigated to https://start.stpring.io
From there I filled in the following:
* Group: `hessian`
* Artifact: `expensivest`

I then added the following dependencies:
* Web
* Thymeleaf
* Actuator
* Cassandra

### Data Model
The information that we are tracking are:
1. User (text)
2. Trip (text)
3. Expense Timestamp (timestamp)
4. Category (text)
5. Amount (double)
6. Comment (text)

The table schema is:

```
CREATE KEYSPACE IF NOT EXISTS exp WITH replication =
    {'class': 'SimpleStrategy', 'replication_factor': '1'};
```
```
CREATE TABLE IF NOT EXISTS exp.expenses(
    user TEXT,
    trip TEXT,
    expts TIMESTAMP,
    category TEXT,
    amount DOUBLE,
    comment TEXT,
    PRIMARY KEY ((user), trip, expid)
) WITH CLUSTERING ORDER BY (trip ASC, expid DESC);

```

### REST Endpoints

```
Endpoint                | Return
------------------------|-----------------------------------------------------
hello/                  | Prints Hello World
api/                    | Prints all expenses
api/{user}              | Prints all expenses for "user"
api/{user}/{trip}       | Prints all expenses for "user" for "trip"
api/category/{category} | Prints all expenses for "category"
api/amount/gt/{amount}  | Prints all expenses for amount greater than "amount"
api/add                 | Adds expense based on POSTed data
```