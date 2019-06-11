CREATE KEYSPACE IF NOT EXISTS corp_finance WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'};
CREATE TABLE IF NOT EXISTS corp_finance.expenses(user TEXT, trip TEXT, expts TIMESTAMP, category TEXT, amount DOUBLE, comment TEXT, PRIMARY KEY ((user), trip, expts)) WITH CLUSTERING ORDER BY (trip ASC, expts DESC);
CREATE SEARCH INDEX IF NOT EXISTS ON corp_finance.expenses;

