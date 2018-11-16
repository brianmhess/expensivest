package hessian.expvest.repository;

import hessian.expvest.domain.Expense;
import hessian.expvest.domain.ExpensePrimaryKey;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface ExpenseRepository extends CassandraRepository<Expense, ExpensePrimaryKey> {
    List<Expense> findByUser(String user);
    List<Expense> findByUserAndTrip(String user, String trip);

    //@AllowFiltering
    List<Expense> findByCategory(String category);
}
