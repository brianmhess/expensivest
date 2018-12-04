package hessian.expensivest.repository;

import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;
import com.datastax.driver.mapping.annotations.Table;
import hessian.expensivest.domain.ExpenseWithMapper;
import com.datastax.driver.mapping.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ExpenseWithMapperRepository {
    private MappingManager mappingManager;
    private Mapper<ExpenseWithMapper> mapper;
    private Session session;
    //private SumCountAccessor sumCountAccessor;

    @Autowired
    public ExpenseWithMapperRepository(MappingManager mappingManager) {
        this.mappingManager = mappingManager;
    }

    @PostConstruct
    private void init() {
        this.mapper = mappingManager.mapper(ExpenseWithMapper.class);
        this.session = mappingManager.getSession();

        psFindAll = session.prepare(cqlFindAll);
        psFindSome = session.prepare(cqlFindSome);
        psFindByKeyUser = session.prepare(cqlFindByKeyUser);
        psFindByKeyUserAndKeyTrip = session.prepare(cqlFindByKeyUserAndKeyTrip);
        psFindByCategory = session.prepare(cqlFindByCategory);
        psFindByAmountGreaterThan = session.prepare(cqlFindByAmountGreaterThan);

        //sumCountAccessor = mappingManager.createAccessor(SumCountAccessor.class);
        psSumCountGlobal = session.prepare(cqlSumCountGlobal);
        psSumCountByUser = session.prepare(cqlSumCountByUser);
        psSumCountByUserAndTrip = session.prepare(cqlSumCountByUserAndTrip);
    }

    private static String cqlFindAll = "SELECT * FROM expensivest.expenses";
    private PreparedStatement psFindAll;
    public List<ExpenseWithMapper> findAll() {
        BoundStatement bs = psFindAll.bind();
        return mapper.map(session.execute(bs)).all();
    }

    private static String cqlFindSome = "SELECT * FROM expensivest.expenses LIMIT ?";
    private PreparedStatement psFindSome;
    public List<ExpenseWithMapper> findSome(Integer some) {
        BoundStatement bs = psFindSome.bind();
        bs.set(0, some, Integer.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static String cqlFindByKeyUser = "SELECT * FROM expensivest.expenses WHERE user = ?";
    private PreparedStatement psFindByKeyUser;
    public List<ExpenseWithMapper> findByKeyUser(String user) {
        BoundStatement bs = psFindByKeyUser.bind();
        bs.set(0, user, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static String cqlFindByKeyUserAndKeyTrip = "SELECT * FROM expensivest.expenses WHERE user = ? AND trip = ?";
    private PreparedStatement psFindByKeyUserAndKeyTrip;
    public List<ExpenseWithMapper> findByKeyUserAndKeyTrip(String user, String trip) {
        BoundStatement bs = psFindByKeyUserAndKeyTrip.bind();
        bs.set(0, user, String.class);
        bs.set(1, trip, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static String cqlFindByCategory = "SELECT * FROM expensivest.expenses WHERE category = ? ALLOW FILTERING";
    private PreparedStatement psFindByCategory;
    public List<ExpenseWithMapper> findByCategory(String category) {
        BoundStatement bs = psFindByCategory.bind();
        bs.set(0, category, String.class);
        return mapper.map(session.execute(bs)).all();
    }

    private static String cqlFindByAmountGreaterThan = "SELECT * FROM expensivest.expenses WHERE amount > ? ALLOW FILTERING";
    private PreparedStatement psFindByAmountGreaterThan;
    public List<ExpenseWithMapper> findByAmountGreaterThan(Double amount) {
        BoundStatement bs = psFindByAmountGreaterThan.bind();
        bs.set(0, amount, Double.class);
        return mapper.map(session.execute(bs)).all();
    }

    public ExpenseWithMapper save(ExpenseWithMapper expenseWithMapper) {
        mapper.save(expenseWithMapper);
        return expenseWithMapper;
    }

    /*
    @Accessor
    public interface SumCountAccessor {
        @Query("SELECT SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses")
        SumCount sumCountGlobal();

        @Query("SELECT user, SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses GROUP BY user")
        Result<SumCount> sumCountByUser();

        @Query("SELECT user, trip, SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses GROUP BY user, trip")
        Result<SumCount> sumCountByUserAndTrip();
    }
    */

    private static String cqlSumCountGlobal = "SELECT SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses";
    private PreparedStatement psSumCountGlobal;
    public SumCount sumCountGlobal() {
        //return sumCountAccessor.sumCountGlobal();
        return new SumCount(session.execute(psSumCountGlobal.bind()).one());
    }

    private static String cqlSumCountByUser = "SELECT user, SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses GROUP BY user";
    private PreparedStatement psSumCountByUser;
    public List<SumCount> sumCountByUser() {
        //return sumCountAccessor.sumCountByUser().forEach(x -> new SumCount(x)).all();
        return session.execute(psSumCountByUser.bind()).all().stream().map(x -> new SumCount(x)).collect(Collectors.toList());
    }

    private static String cqlSumCountByUserAndTrip = "SELECT user, trip, SUM(amount) AS sum_val, COUNT(amount) AS count_val FROM expensivest.expenses GROUP BY user, trip";
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

        private <T> T tryGet(Row r, String column, Class<T> klass) {
            return r.getColumnDefinitions().contains(column) ? r.get(column, klass) : null;
        }

        public SumCount(Row r) {
            this.user = tryGet(r, "user", String.class);
            this.trip = tryGet(r, "trip", String.class);
            this.count_val = tryGet(r, "count_val", Long.class);
            this.sum_val = tryGet(r, "sum_val", Double.class);
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
