package hessian.expensivest.spring;


import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.Date;
import java.util.List;

public interface ExpenseRepositorySpring extends CassandraRepository<Expense, ExpensePrimaryKey> {
    Expense save(Expense expense);
    void deleteByKeyUserAndKeyTripAndKeyExpts(String user, String trip, Date expts);
    List<Expense> findAll();

    List<Expense> findByKeyUser(String user);
    List<Expense> findByKeyUserAndKeyTrip(String user, String trip);

    @AllowFiltering
    List<Expense> findByCategory(String category);
    @AllowFiltering
    List<Expense> findByAmountGreaterThan(double amount);

    /* ToDo: No Support for Search in Apollo yet
    List<Expense> findByCategoryLike(String category);
    List<Expense> findByCategoryStartingWith(String category);
    */
}
