package hessian.expensivest.mapper;

import com.datastax.driver.core.Row;
import com.datastax.oss.driver.api.mapper.annotations.Entity;

@Entity
public class ExpenseSumCount {
    private String user;
    private String trip;
    private Long count_val;
    private Double sum_val;

    public ExpenseSumCount() { }

    public ExpenseSumCount(String user, String trip, Long count_val, Double sum_val) {
        this.user = user;
        this.trip = trip;
        this.count_val = count_val;
        this.sum_val = sum_val;
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

    private <T> T tryGet(Row r, String column, Class<T> klass, T otherwise) {
        return r.getColumnDefinitions().contains(column) ? r.get(column, klass) : otherwise;
    }

    public ExpenseSumCount(Row r) {
        this.user = tryGet(r, "user", String.class, null);
        this.trip = tryGet(r, "trip", String.class, null);
        this.count_val = tryGet(r, "count_val", Long.class, null);
        this.sum_val = tryGet(r, "sum_val", Double.class, null);
    }

}
