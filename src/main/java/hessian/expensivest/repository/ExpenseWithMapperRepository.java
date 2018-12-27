package hessian.expensivest.repository;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.MappingManager;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import hessian.expensivest.domain.ExpenseWithMapper;
import com.datastax.driver.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.datastax.driver.core.querybuilder.QueryBuilder.*;

@Repository
public class ExpenseWithMapperRepository {
    private MappingManager mappingManager;
    private Mapper<ExpenseWithMapper> mapper;
    private Session session;

    @Autowired
    public ExpenseWithMapperRepository(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @PostConstruct
    private void init() {
        this.mapper = mappingManager.mapper(ExpenseWithMapper.class);
        this.session = mappingManager.getSession();

        psFindAll = session.prepare(builtFindAll); //session.prepare(cqlFindAll);
        psFindSome = session.prepare(builtFindSome); // session.prepare(cqlFindSome);
        psFindByKeyUser = session.prepare(builtFindByKeyUser); // session.prepare(cqlFindByKeyUser);
        psFindByKeyUserAndKeyTrip = session.prepare(builtFindByKeyUserAndKeyTrip); // session.prepare(cqlFindByKeyUserAndKeyTrip);
        psFindByCategory = session.prepare(builtFindByCategory); // session.prepare(cqlFindByCategory);
        psFindByAmountGreaterThan = session.prepare(builtFindByAmountGreaterThan); // session.prepare(cqlFindByAmountGreaterThan);

        psSumCountGlobal = session.prepare(builtSumCountGlobal); // session.prepare(cqlSumCountGlobal);
        psSumCountByUser = session.prepare(builtSumCountByUser); // session.prepare(cqlSumCountByUser);
        psSumCountByUserAndTrip = session.prepare(builtSumCountByUserAndTrip); // session.prepare(cqlSumCountByUserAndTrip);
    }

    // Save
    public ExpenseWithMapper save(ExpenseWithMapper expenseWithMapper) {
        mapper.save(expenseWithMapper);
        return expenseWithMapper;
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
        ExpenseWithMapper e = new ExpenseWithMapper(user, trip, tdate, Double.valueOf(amount), category, comment);
        mapper.save(e);
        retval = "";
        return retval;
    }

    // Delete
    public void delete(String user, String trip, Date expts) {
        mapper.delete(user, trip, expts);
        ExpenseWithMapper e = new ExpenseWithMapper();
    }

    //private static String cqlFindAll = "SELECT * FROM expensivest.expenses";
    private static BuiltStatement builtFindAll = QueryBuilder.select().all().from("expensivest", "expenses");
    private PreparedStatement psFindAll;
    public List<ExpenseWithMapper> findAll() {
        BoundStatement bs = psFindAll.bind();
        return mapper.map(session.execute(bs)).all();
    }

    //private static String cqlFindSome = "SELECT * FROM expensivest.expenses LIMIT ?";
    //private static String cqlFindSome = "SELECT * FROM expensivest.expenses LIMIT :limit";
    private BuiltStatement builtFindSome = QueryBuilder.select().all().from("expensivest", "expenses").limit(bindMarker("lmt"));
    private PreparedStatement psFindSome;
    public List<ExpenseWithMapper> findSome(Integer some) {
        BoundStatement bs = psFindSome.bind();
        //bs.set(0, some, Integer.class);
        bs.set("lmt", some, Integer.class);
        return mapper.map(session.execute(bs)).all();
    }

    //private static String cqlFindByKeyUser = "SELECT * FROM expensivest.expenses WHERE user = ?";
    //private static String cqlFindByKeyUser = "SELECT * FROM expensivest.expenses WHERE user = :user";
    private static BuiltStatement builtFindByKeyUser = QueryBuilder.select().all().from("expensivest", "expenses").where(eq("user", bindMarker("user")));
    private PreparedStatement psFindByKeyUser;
    public List<ExpenseWithMapper> findByKeyUser(String user) {
        BoundStatement bs = psFindByKeyUser.bind();
        bs.set("user", user, String.class);
        //bs.set(0, user, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    //private static String cqlFindByKeyUserAndKeyTrip = "SELECT * FROM expensivest.expenses WHERE user = ? AND trip = ?";
    //private static String cqlFindByKeyUserAndKeyTrip = "SELECT * FROM expensivest.expenses WHERE user = :user AND trip = :trip";
    private static BuiltStatement builtFindByKeyUserAndKeyTrip = QueryBuilder.select().all().from("expensivest", "expenses")
            .where(eq("user", bindMarker("user"))).and(eq("trip", bindMarker("trip")));
    private PreparedStatement psFindByKeyUserAndKeyTrip;
    public List<ExpenseWithMapper> findByKeyUserAndKeyTrip(String user, String trip) {
        BoundStatement bs = psFindByKeyUserAndKeyTrip.bind();
        bs.set("user", user, String.class);
        bs.set("trip", trip, String.class);
        //bs.set(0, user, String.class);
        //bs.set(1, trip, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    //private static String cqlFindByCategory = "SELECT * FROM expensivest.expenses WHERE category = ? ALLOW FILTERING";
    //private static String cqlFindByCategory = "SELECT * FROM expensivest.expenses WHERE category = :category ALLOW FILTERING";
    private static BuiltStatement builtFindByCategory = QueryBuilder.select().all().from("expensivest", "expenses")
            .where(eq("category", bindMarker("category"))).allowFiltering();
    private PreparedStatement psFindByCategory;
    public List<ExpenseWithMapper> findByCategory(String category) {
        BoundStatement bs = psFindByCategory.bind();
        bs.set("category", category, String.class);
        //bs.set(0, category, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    //private static String cqlFindByAmountGreaterThan = "SELECT * FROM expensivest.expenses WHERE amount > ? ALLOW FILTERING";
    //private static String cqlFindByAmountGreaterThan = "SELECT * FROM expensivest.expenses WHERE amount > :amount ALLOW FILTERING";
    private static BuiltStatement builtFindByAmountGreaterThan = QueryBuilder.select().all().from("expensivest", "expenses")
            .where(gt("amount", bindMarker("amount"))).allowFiltering();
    private PreparedStatement psFindByAmountGreaterThan;
    public List<ExpenseWithMapper> findByAmountGreaterThan(Double amount) {
        BoundStatement bs = psFindByAmountGreaterThan.bind();
        bs.set("amount", amount, Double.class);
        //bs.set(0, amount, Double.class);
        return mapper.map(session.execute(bs)).all();
    }

    //private static String cqlSumCountGlobal = "SELECT SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses";
    private static BuiltStatement builtSumCountGlobal = QueryBuilder.select().sum(column("amount")).as("sum_val")
            .count(column("amount")).as("count_val")
            .from("expensivest", "expenses");
    private PreparedStatement psSumCountGlobal;
    public SumCount sumCountGlobal() {
        return new SumCount(session.execute(psSumCountGlobal.bind()).one());
    }

    //private static String cqlSumCountByUser = "SELECT user, SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses GROUP BY user";
    private static BuiltStatement builtSumCountByUser = QueryBuilder.select().column("user")
            .sum(column("amount")).as("sum_val")
            .count(column("amount")).as("count_val")
            .from("expensivest", "expenses")
            .groupBy(column("user"));
    private PreparedStatement psSumCountByUser;
    public List<SumCount> sumCountByUser() {
        return session.execute(psSumCountByUser.bind()).all().stream().map(x -> new SumCount(x)).collect(Collectors.toList());
    }

    //private static String cqlSumCountByUserAndTrip = "SELECT user, trip, SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses GROUP BY user, trip";
    private static BuiltStatement builtSumCountByUserAndTrip = QueryBuilder.select().column("user").column("trip")
            .sum(column("amount")).as("sum_val")
            .count(column("amount")).as("count_val")
            .from("expensivest", "expenses")
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
