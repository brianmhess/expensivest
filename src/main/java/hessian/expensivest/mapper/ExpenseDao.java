package hessian.expensivest.mapper;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import com.datastax.oss.driver.api.mapper.entity.saving.NullSavingStrategy;

import java.time.Instant;

@Dao
public interface ExpenseDao {
    // Save
    @Insert
    public Expense save(Expense expense);

    @Query(value = "INSERT INTO ${keyspaceId}.${tableId}(user, trip, expts, amount, category, comment) VALUES (:user, :trip, :expts, :amount, :category, :comment)", nullSavingStrategy = NullSavingStrategy.DO_NOT_SET)
    public void save(String user, String trip, String expts, String amount, String category, String comment);


    // Delete
    @Delete
    public void delete(Expense expense);

    @Delete(entityClass = Expense.class)
    public void delete(String user, String trip, Instant expts);


    // Select
    @Query("SELECT * FROM ${keyspaceId}.${tableId}")
    public PagingIterable<Expense> findAll();

    @Query("SELECT * FROM ${keyspaceId}.${tableId} LIMIT :some")
    public PagingIterable<Expense> findSome(Integer some);

    @Select(customWhereClause = "user = :user")
    public PagingIterable<Expense> findByKeyUser(String user);

    @Select(customWhereClause = "user = :user AND trip = :trip")
    public PagingIterable<Expense> findByKeyUserAndKeyTrip(String user, String trip);

    @Select(customWhereClause = "category = :category")
    public PagingIterable<Expense> findByCategory(String category);

    @Select(customWhereClause = "amount > :amount")
    public PagingIterable<Expense> findByAmountGreaterThan(Double amount);

    @Select(customWhereClause = "category LIKE :category")
    public PagingIterable<Expense> findByCategoryLike(String category);

    @QueryProvider(providerClass = ExpenseDaoQueryProvider.class, entityHelpers = Expense.class)
    public PagingIterable<Expense> findByCategoryStartingWith(String category);


    // Sums and Counts
    @Query("SELECT Sum(amount) AS sum_val, Count(amount) AS count_val FROM ${keyspaceId}.${tableId}")
    public ExpenseSumCount sumCountGlobal();

    @Query("SELECT user, Sum(amount) AS sum_val, Count(amount) AS count_val FROM ${keyspaceId}.${tableId} GROUP BY user")
    public PagingIterable<ExpenseSumCount> sumCountByUser();

    @Query("SELECT user, trip, Sum(amount) AS sum_val, Count(amount) AS count_val FROM ${keyspaceId}.${tableId} GROUP BY user, trip")
    public PagingIterable<ExpenseSumCount> sumCountByUserAndTrip();

}
