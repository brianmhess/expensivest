package hessian.expensivest.mapper;

import com.datastax.dse.driver.api.core.DseSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.BoundStatement;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.bindMarker;


public class ExpenseDaoQueryProvider {
    private final DseSession session;
    private final EntityHelper<Expense> expenseHelper;
    private final PreparedStatement preparedStatement;

    public ExpenseDaoQueryProvider(MapperContext context, EntityHelper<Expense> sensorReadingHelper) {
        this.session = (DseSession)context.getSession();
        this.expenseHelper = sensorReadingHelper;
        this.preparedStatement = this.session.prepare(QueryBuilder.selectFrom("expensivest", "expenses").all().whereColumn("category").like(bindMarker("category")).build());
    }

    public PagingIterable<Expense> findByCategoryStartingWith(String category) {
        BoundStatement bs = preparedStatement.bind();
        bs = bs.set("category", category+"%", String.class);
        return session.execute(bs).map(expenseHelper::get);
    }
}
