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

### Create your cluster
In Apollo create a new cluster.  Set the name of the keyspace as `expensivest`.  You do not need to use
that name, but if you change it, make sure to change the `dse.keyspace` value in `application.properties`.

You will need to add your credentials zip file to the project.  Add that
zip file as `creds.zip` in the `resources/` directory of the project.  If you 
want you can change the name of the zip file in the resource, but if you do, make
sure to change the value of `apollo.credentials` in `application.properties`, and make
sure to prepend a `/` to the file name.  For example, if the file you add is named
`creds-mycluster1.zip` then the value for `apollo.credentials` would be `/creds-mycluster1.zip`.

You must also set the `dse.username` and `dse.password` properties
in `application.properties`.

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

You can change the port for the application by changing the `server.port` property
in `application.properties`.

### REST Endpoints with DSE Object Mapper
The endpoints defined are at `http://localhost:8222/` followed by:
```
Endpoint                                  | Return
------------------------------------------|-----------------------------------------------------
dse/hello                                 | Prints Hello World
dse/user?user={user}                      | Prints all expenses for "user"
dse/user_trip?user={user}&trip={trip}     | Prints all expenses for "user" for "trip"
dse/category?category={category}          | Prints all expenses for "category"
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
curl http://localhost:8222/dse/amount/gt?amount=250
curl -H "Content-Type: application/json" -d '{user":"bhess", "trip":"first", "expts":"2018-01-01 01:00:00", "category":"work", "amount":"10.00", "comment":"NA"}' http://localhost:8222/dse/add
curl http://localhost:8222/dse/sum_count/global
curl http://localhost:8222/dse/sum_count/user
curl http://localhost:8222/dse/sum_count/user_and_trip
```

### Webpage
There is a simple web interface with forms at 
```
http://localhost:8222/
```
This has a set of forms to interact in a simple way with the REST APIs.

### Actuator
Actuator has been set for this project.  There is also a health indicator
showing the state of DSE at
```
http://localhost:8222/actuator
```
and
```
http://localhost:8222/actuator/health
```
