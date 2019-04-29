package hessian.expensivest.spring;


import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Date;
import java.util.List;

public interface ExpenseRepositorySpring extends CassandraRepository<Expense, ExpensePrimaryKey> {
    List<Expense> findAll();

    List<Expense> findByKeyUser(String user);
    List<Expense> findByKeyUserAndKeyTrip(String user, String trip);

    List<Expense> findByCategory(String category);
    List<Expense> findByCategoryLike(String category);
    List<Expense> findByCategoryStartingWith(String category);

    List<Expense> findByAmountGreaterThan(double amount);

    Expense save(Expense expense);
    void deleteByKeyUserAndKeyTripAndKeyExpts(String user, String trip, Date expts);
}
