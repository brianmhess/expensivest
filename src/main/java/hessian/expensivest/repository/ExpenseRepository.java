package hessian.expensivest.repository;

import hessian.expensivest.domain.Expense;
import hessian.expensivest.domain.ExpensePrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface ExpenseRepository extends CassandraRepository<Expense, ExpensePrimaryKey> {
    List<Expense> findByUser(String user);
    List<Expense> findByUserAndTrip(String user, String trip);

    //@AllowFiltering
    List<Expense> findByCategory(String category);
}
