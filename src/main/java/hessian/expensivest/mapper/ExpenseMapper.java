package hessian.expensivest.mapper;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

@Mapper
public interface ExpenseMapper {
    @DaoFactory
    ExpenseDao expenseDao(@DaoKeyspace String keyspace, @DaoTable String table);
}
