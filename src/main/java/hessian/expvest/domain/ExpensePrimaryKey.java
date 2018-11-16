package hessian.expvest.domain;

import com.datastax.driver.core.DataType;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.io.Serializable;
import java.util.Date;

@PrimaryKeyClass
public class ExpensePrimaryKey implements Serializable {
    @PrimaryKeyColumn(name = "user", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    //@CassandraType(type = DataType.Name.TEXT)
    private String user;

    @PrimaryKeyColumn(name = "trip", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    //@CassandraType(type = DataType.Name.TEXT)
    private String trip;

    @PrimaryKeyColumn(name = "expts", ordinal = 2, type = PrimaryKeyType.CLUSTERED)
    //@CassandraType(type = DataType.Name.TIMESTAMP)
    private Date expts;


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
}
