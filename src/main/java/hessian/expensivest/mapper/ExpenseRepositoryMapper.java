package hessian.expensivest.mapper;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

@Repository
public class ExpenseRepositoryMapper {
    private MappingManager mappingManager;
    private Mapper<Expense> mapper;
    private Session session;

    @Autowired
    public ExpenseRepositoryMapper(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @PostConstruct
    private void init() {
        this.mapper = mappingManager.mapper(Expense.class);
        this.mapper.setDefaultSaveOptions(Mapper.Option.saveNullFields(false));
        this.session = mappingManager.getSession();

        psFindAll = session.prepare(builtFindAll);
        psFindSome = session.prepare(builtFindSome);
        psFindByKeyUser = session.prepare(builtFindByKeyUser);
        psFindByKeyUserAndKeyTrip = session.prepare(builtFindByKeyUserAndKeyTrip);
        psFindByCategory = session.prepare(builtFindByCategory);
        psFindByAmountGreaterThan = session.prepare(builtFindByAmountGreaterThan);

        psSumCountGlobal = session.prepare(builtSumCountGlobal);
        psSumCountByUser = session.prepare(builtSumCountByUser);
        psSumCountByUserAndTrip = session.prepare(builtSumCountByUserAndTrip);

        //session.prepare("SELECT * FROM expensivest.expenses WHERE category LIKE :category");
        psFindByCategoryLike = session.prepare(builtFindByCategoryLike);
        //session.prepare("SELECT * FROM expensivest.expenses WHERE category LIKE :category");
        psFindByCategoryStartingWith = session.prepare(builtFindByCategoryStartingWith);
    }

    // Save
    public Expense save(Expense expense) {
        mapper.save(expense);
        return expense;
    }

    public String save(String user, String trip, String expts, String amount, String category, String comment) {
        SimpleDateFormat sdf = new SimpleDateFormat("y-M-d H:m:s");
        Date tdate = null;
        String retval;
        try {
            tdate = sdf.parse(expts);
        }
        catch (Exception e){
            retval = "<p>Couldn't parse date: expts = " + expts;
            return retval;
        }
        Expense e = new Expense(user, trip, tdate, Double.valueOf(amount), category, comment);
        mapper.save(e);
        retval = "";
        return retval;
    }

    // Delete
    public void delete(String user, String trip, Date expts) {
        mapper.delete(user, trip, expts);
    }

    // Find
    private static BuiltStatement builtFindAll = QueryBuilder.select().all().from("corp_finance", "expenses");
    private PreparedStatement psFindAll;
    public List<Expense> findAll() {
        BoundStatement bs = psFindAll.bind();
        return mapper.map(session.execute(bs)).all();
    }

    private BuiltStatement builtFindSome = QueryBuilder.select().all().from("corp_finance", "expenses").limit(bindMarker("lmt"));
    private PreparedStatement psFindSome;
    public List<Expense> findSome(Integer some) {
        BoundStatement bs = psFindSome.bind();
        bs.set("lmt", some, Integer.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static BuiltStatement builtFindByKeyUser = QueryBuilder.select().all().from("corp_finance", "expenses").where(eq("user", bindMarker("user")));
    private PreparedStatement psFindByKeyUser;
    public List<Expense> findByKeyUser(String user) {
        BoundStatement bs = psFindByKeyUser.bind();
        bs.set("user", user, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static BuiltStatement builtFindByKeyUserAndKeyTrip = QueryBuilder.select().all().from("corp_finance", "expenses")
            .where(eq("user", bindMarker("user"))).and(eq("trip", bindMarker("trip")));
    private PreparedStatement psFindByKeyUserAndKeyTrip;
    public List<Expense> findByKeyUserAndKeyTrip(String user, String trip) {
        BoundStatement bs = psFindByKeyUserAndKeyTrip.bind();
        bs.set("user", user, String.class);
        bs.set("trip", trip, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static BuiltStatement builtFindByCategory = QueryBuilder.select().all().from("corp_finance", "expenses")
            .where(eq("category", bindMarker("category")));
    private PreparedStatement psFindByCategory;
    public List<Expense> findByCategory(String category) {
        BoundStatement bs = psFindByCategory.bind();
        bs.set("category", category, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static BuiltStatement builtFindByAmountGreaterThan = QueryBuilder.select().all().from("corp_finance", "expenses")
            .where(gt("amount", bindMarker("amount")));
    private PreparedStatement psFindByAmountGreaterThan;
    public List<Expense> findByAmountGreaterThan(Double amount) {
        BoundStatement bs = psFindByAmountGreaterThan.bind();
        bs.set("amount", amount, Double.class);
        return mapper.map(session.execute(bs)).all();
    }

    //session.prepare("SELECT * FROM expensivest.expenses WHERE category LIKE :category");
    private static BuiltStatement builtFindByCategoryLike = QueryBuilder.select().all().from("corp_finance", "expenses")
            .where(like("category", bindMarker("category")));
    private PreparedStatement psFindByCategoryLike;
    public List<Expense> findByCategoryLike(String category) {
        BoundStatement bs = psFindByCategoryLike.bind();
        bs.set("category", category, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static BuiltStatement builtFindByCategoryStartingWith = QueryBuilder.select().all().from("corp_finance", "expenses")
            .where(like("category", bindMarker("category")));
    private PreparedStatement psFindByCategoryStartingWith;
    public List<Expense> findByCategoryStartingWith(String category) {
        BoundStatement bs = psFindByCategoryStartingWith.bind();
        bs.set("category", category+"%", String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static BuiltStatement builtSumCountGlobal = QueryBuilder.select().sum(column("amount")).as("sum_val")
            .count(column("amount")).as("count_val")
            .from("corp_finance", "expenses");
    private PreparedStatement psSumCountGlobal;
    public SumCount sumCountGlobal() {
        return new SumCount(session.execute(psSumCountGlobal.bind()).one());
    }

    private static BuiltStatement builtSumCountByUser = QueryBuilder.select().column("user")
            .sum(column("amount")).as("sum_val")
            .count(column("amount")).as("count_val")
            .from("corp_finance", "expenses")
            .groupBy(column("user"));
    private PreparedStatement psSumCountByUser;
    public List<SumCount> sumCountByUser() {
        return session.execute(psSumCountByUser.bind()).all().stream().map(x -> new SumCount(x)).collect(Collectors.toList());
    }

    private static BuiltStatement builtSumCountByUserAndTrip = QueryBuilder.select().column("user").column("trip")
            .sum(column("amount")).as("sum_val")
            .count(column("amount")).as("count_val")
            .from("corp_finance", "expenses")
            .groupBy(column("user"), column("trip"));
    private PreparedStatement psSumCountByUserAndTrip;
    public List<SumCount> sumCountByUserAndTrip() {
        return session.execute(psSumCountByUserAndTrip.bind()).all().stream().map(x -> new SumCount(x)).collect(Collectors.toList());
    }

    public class SumCount {
        private String user;
        private String trip;
        private Long count_val;
        private Double sum_val;

        public SumCount() { }

        public SumCount(String user, String trip, Long count_val, Double sum_val) {
            this.user = user;
            this.trip = trip;
            this.count_val = count_val;
            this.sum_val = sum_val;
        }

        private <T> T tryGet(Row r, String column, Class<T> klass, T otherwise) {
            return r.getColumnDefinitions().contains(column) ? r.get(column, klass) : otherwise;
        }

        public SumCount(Row r) {
            this.user = tryGet(r, "user", String.class, null);
            this.trip = tryGet(r, "trip", String.class, null);
            this.count_val = tryGet(r, "count_val", Long.class, null);
            this.sum_val = tryGet(r, "sum_val", Double.class, null);
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getTrip() {
            return trip;
        }

        public void setTrip(String trip) {
            this.trip = trip;
        }

        public Long getCount_val() {
            return count_val;
        }

        public void setCount_val(Long count_val) {
            this.count_val = count_val;
        }

        public Double getSum_val() {
            return sum_val;
        }

        public void setSum_val(Double sum_val) {
            this.sum_val = sum_val;
        }

        @Override
        public String toString() {
            return "SumCount{" +
                    "user='" + user + '\'' +
                    ", trip='" + trip + '\'' +
                    ", count_val=" + count_val +
                    ", sum_val=" + sum_val +
                    '}';
        }
    }

}
