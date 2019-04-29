package hessian.expensivest.spring;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;

@PrimaryKeyClass
public class ExpensePrimaryKey implements Serializable {

    @PrimaryKeyColumn(name = "user", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String user;

    @PrimaryKeyColumn(name = "trip", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private String trip;

    @PrimaryKeyColumn(name = "expts", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    private Date expts;

    public ExpensePrimaryKey(String user, String trip, Date expts) {
        this.user = user;
        this.trip = trip;
        this.expts = expts;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getExpts() {
        return expts;
    }

    public void setExpts(Date expts) {
        this.expts = expts;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    @Override
    public String toString() {
        return "ExpensePrimaryKey{" +
                "user='" + user + '\'' +
                ", trip='" + trip + '\'' +
                ", expts=" + expts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpensePrimaryKey)) return false;

        ExpensePrimaryKey that = (ExpensePrimaryKey) o;

        if (!getUser().equals(that.getUser())) return false;
        if (!getTrip().equals(that.getTrip())) return false;
        return getExpts().equals(that.getExpts());

    }

    @Override
    public int hashCode() {
        int result = getUser().hashCode();
        result = 31 * result + getTrip().hashCode();
        result = 31 * result + getExpts().hashCode();
        return result;
    }
}
