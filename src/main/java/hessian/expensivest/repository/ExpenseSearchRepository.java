package hessian.expensivest.repository;


import hessian.expensivest.domain.Expense;
import hessian.expensivest.domain.ExpensePrimaryKey;
import hessian.expensivest.domain.ExpenseWithSearch;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.List;

public interface ExpenseSearchRepository extends CassandraRepository<ExpenseWithSearch, ExpensePrimaryKey> {
    List<ExpenseWithSearch> findAll();

    List<ExpenseWithSearch> findByKeyUser(String user);
    List<ExpenseWithSearch> findByKeyUserAndKeyTrip(String user, String trip);

    List<ExpenseWithSearch> findByCategory(String category);
    List<ExpenseWithSearch> findByCategoryLike(String category);
    List<ExpenseWithSearch> findByCategoryStartingWith(String category);

    List<ExpenseWithSearch> findByAmountGreaterThan(double amount);

    ExpenseWithSearch save(ExpenseWithSearch expense);
    ExpenseWithSearch save(Expense expense);
}
