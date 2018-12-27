# README


## Notes
### Getting Started
To generate the basic pom.xml I navigated to [https://start.spring.io](https://start.spring.io)
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
CREATE KEYSPACE IF NOT EXISTS expensivest WITH replication =
    {'class': 'SimpleStrategy', 'replication_factor': '1'};
```
```
CREATE TABLE IF NOT EXISTS expensivest.expenses(
    user TEXT,
    trip TEXT,
    expts TIMESTAMP,
    category TEXT,
    amount DOUBLE,
    comment TEXT,
    PRIMARY KEY ((user), trip, expts)
) WITH CLUSTERING ORDER BY (trip ASC, expts DESC);

```

Added an additional copy of this table that we will use with Search:
```
CREATE TABLE IF NOT EXISTS expensivest.expenses_with_search(
    user TEXT,
    trip TEXT,
    expts TIMESTAMP,
    category TEXT,
    amount DOUBLE,
    comment TEXT,
    PRIMARY KEY ((user), trip, expts)
) WITH CLUSTERING ORDER BY (trip ASC, expts DESC);
CREATE SEARCH INDEX IF NOT EXISTS ON expensivest.expenses_with_search;
```

### Webpage
Navigate to `http://localhost:8222/` to see a "Hello World" message.

Navigate to `http://localhost:8222/ui/` to see a webpage interface to
Expensivest, including create, delete, and various find queries.

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

Examples:
```
curl http://localhost:8222/hello
curl http://localhost:8222/api/
curl http://localhost:8222/api/bhess
curl http://localhost:8222/api/bhess/first
curl http://localhost:8222/api/category/fun
curl http://localhost:8222/api/amount/gt/250
curl -H "Content-Type: application/json" -d '{"key": {"user":"bhess", "trip":"first", "expts":"2018-01-01T01:00:00"}, "category":"work", "amount":"10.00", "comment":"NA"}' http://localhost:8222/api/add
```

Search endpoints:
```
Endpoint                                | Return
----------------------------------------|-----------------------------------------------------
api/search/hello/                       | Prints Hello World
api/search/                             | Prints all expenses
api/search/{user}                       | Prints all expenses for "user"
api/search/{user}/{trip}                | Prints all expenses for "user" for "trip"
api/search/category/{category}          | Prints all expenses for "category"
api/search/category/starting/{category} | Prints all expenses for category starting with "category"
api/search/category/like/{category}     | Prints all expenses for category like "category"
api/search/amount/gt/{amount}           | Prints all expenses for amount greater than "amount"
```

Examples:
```
curl http://localhost:8222/api/search/hello
curl http://localhost:8222/api/search/
curl http://localhost:8222/api/search/bhess
curl http://localhost:8222/api/search/bhess/first
curl http://localhost:8222/api/search/category/fun
curl http://localhost:8222/api/search/category/starting/fu
curl http://localhost:8222/api/search/category/like/%25un
curl http://localhost:8222/api/search/amount/gt/250
```
