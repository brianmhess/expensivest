CREATE TABLE IF NOT EXISTS expensivest.expenses(user TEXT, trip TEXT, expts TIMESTAMP, category TEXT, amount DOUBLE, comment TEXT, PRIMARY KEY ((user), trip, expts)) WITH CLUSTERING ORDER BY (trip ASC, expts DESC);

