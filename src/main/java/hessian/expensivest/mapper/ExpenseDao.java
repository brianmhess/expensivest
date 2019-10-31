package hessian.expensivest.mapper;

import com.datastax.dse.driver.api.mapper.reactive.MappedReactiveResultSet;
import com.datastax.oss.driver.api.mapper.annotations.*;

import java.time.Instant;

@Dao
public interface ExpenseDao {
    // Save
    @Insert
    public Expense save(Expense expense);

    // Delete
    @Delete
    public void delete(Expense expense);

    @Delete(entityClass = Expense.class)
    public void delete(String user, String trip, Instant expts);


    // Select
    @Query("SELECT * FROM ${keyspaceId}.${tableId}")
    public MappedReactiveResultSet<Expense> findAll();

    @Query("SELECT * FROM ${keyspaceId}.${tableId} LIMIT :some")
    public MappedReactiveResultSet<Expense> findSome(@CqlName("some") Integer some);

    @Select(customWhereClause = "user = :user")
    public MappedReactiveResultSet<Expense> findByKeyUser(@CqlName("user") String user);

    @Select(customWhereClause = "user = :user AND trip = :trip")
    public MappedReactiveResultSet<Expense> findByKeyUserAndKeyTrip(@CqlName("user") String user, @CqlName("trip") String trip);

    @Select(customWhereClause = "category = :category")
    public MappedReactiveResultSet<Expense> findByCategory(@CqlName("category") String category);

    @Select(customWhereClause = "amount > :amount")
    public MappedReactiveResultSet<Expense> findByAmountGreaterThan(@CqlName("amount") Double amount);

    @Select(customWhereClause = "category LIKE :category")
    public MappedReactiveResultSet<Expense> findByCategoryLike(@CqlName("category") String category);

    @QueryProvider(providerClass = ExpenseDaoQueryProvider.class, entityHelpers = Expense.class)
    public MappedReactiveResultSet<Expense> findByCategoryStartingWith(String category);


    // Sums and Counts
    @Query("SELECT Sum(amount) AS sum_val, Count(amount) AS count_val FROM ${keyspaceId}.${tableId}")
    public ExpenseSumCount sumCountGlobal();

    @Query("SELECT user, Sum(amount) AS sum_val, Count(amount) AS count_val FROM ${keyspaceId}.${tableId} GROUP BY user")
    public MappedReactiveResultSet<ExpenseSumCount> sumCountByUser();

    @Query("SELECT user, trip, Sum(amount) AS sum_val, Count(amount) AS count_val FROM ${keyspaceId}.${tableId} GROUP BY user, trip")
    public MappedReactiveResultSet<ExpenseSumCount> sumCountByUserAndTrip();

}
