package hessian.expensivest.repository;

import hessian.expensivest.domain.Expense;
import hessian.expensivest.domain.ExpensePrimaryKey;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import java.util.List;

public interface ExpenseRepository extends CassandraRepository<Expense, ExpensePrimaryKey> {
    List<Expense> findAll();

    List<Expense> findByKeyUser(String user);
    List<Expense> findByKeyUserAndKeyTrip(String user, String trip);

    @AllowFiltering
    List<Expense> findByCategory(String category);

    @AllowFiltering
    List<Expense> findByAmountGreaterThan(double amount);

    Expense save(Expense expense);
}
