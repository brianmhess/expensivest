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

CREATE SEARCH INDEX IF NOT EXISTS ON expensivest.expenses;
```

### Webpage
Navigate to `http://localhost:8222/` to see a "Hello World" message.

Navigate to `http://localhost:8222/web` to see a webpage interface to
Expensivest, including create, delete, and various find queries.

### REST Endpoints with Spring Data Cassandra
Navigate to `http://localhost:8222/api` to see the list of endpoints
```
Endpoint                                  | Return
------------------------------------------|-----------------------------------------------------
hello                                     | Prints Hello World
api/user?user={user}                      | Prints all expenses for "user"
api/user_trip?user={user}&trip={trip}     | Prints all expenses for "user" for "trip"
api/category?category={category}          | Prints all expenses for "category"
api/category/like?category={category}     | Prints all expenses for category like "category"
api/category/starting?category={category} | Prints all expenses for category starting with "category"
api/amount/gt?amount={amount}             | Prints all expenses for amount greater than "amount"
api/add                                   | Adds expense based on POSTed data
api/delete                                | Deletes expense based on POSTed data
```

Examples:
```
curl http://localhost:8222/hello
curl http://localhost:8222/api
curl http://localhost:8222/api/user?user=bhess
curl http://localhost:8222/api/user_trip?user=bhess&trip=first
curl http://localhost:8222/api/category?category=fun
curl http://localhost:8222/api/category/like?category=fu%
curl http://localhost:8222/api/category/starting?category=f
curl http://localhost:8222/api/amount/gt?amount=250
curl -H "Content-Type: application/json" -d '{"key": {"user":"bhess", "trip":"first", "expts":"2018-01-01T01:00:00"}, "category":"work", "amount":"10.00", "comment":"NA"}' http://localhost:8222/api/add
```

### REST Endpoints with DSE Object Mapper
Navigate to `http://localhost:8222/dse` to see the list of endpoints
```
Endpoint                                  | Return
------------------------------------------|-----------------------------------------------------
hello                                     | Prints Hello World
dse/user?user={user}                      | Prints all expenses for "user"
dse/user_trip?user={user}&trip={trip}     | Prints all expenses for "user" for "trip"
dse/category?category={category}          | Prints all expenses for "category"
dse/category/like?category={category}     | Prints all expenses for category like "category"
dse/category/starting?category={category} | Prints all expenses for category starting with "category"
dse/amount/gt?amount={amount}             | Prints all expenses for amount greater than "amount"
dse/add                                   | Adds expense based on POSTed data
dse/delete                                | Deletes expense based on POSTed data
dse/sum_count/global                      | Global sum and count for expenses
dse/sum_count/user                        | Sum and count of expenses grouped by user
dse/sum_count/user_and_trip               | Sum and count of expenses grouped by user and trip
```

Examples:
```
curl http://localhost:8222/dse/hello
curl http://localhost:8222/dse
curl http://localhost:8222/dse/user?user=bhess
curl http://localhost:8222/dse/user_trip?user=bhess&trip=first
curl http://localhost:8222/dse/category?category=fun
curl http://localhost:8222/dse/category/like?category=fu%
curl http://localhost:8222/dse/category/starting?category=f
curl http://localhost:8222/dse/amount/gt?amount=250
curl -H "Content-Type: application/json" -d '{user":"bhess", "trip":"first", "expts":"2018-01-01 01:00:00", "category":"work", "amount":"10.00", "comment":"NA"}' http://localhost:8222/dse/add
curl http://localhost:8222/dse/sum_count/global
curl http://localhost:8222/dse/sum_count/user
curl http://localhost:8222/dse/sum_count/user_and_trip

```
