package hessian.expensivest.domain;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import hessian.typeparser.AnyParser;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;

@Table(name ="expenses", keyspace = "expensivest")
public class ExpenseWithMapper {
    @PartitionKey(0)
    @Column
    private String user;

    @PartitionKey(1)
    @Column
    private String trip;

    @ClusteringColumn(0)
    @Column
    private Date expts;

    @Column
    private Double amount;

    @Column
    private String category;

    @Column
    private String comment;

    public ExpenseWithMapper() { }

    public ExpenseWithMapper(String user, String trip, Date expts, Double amount, String category, String comment) {
        this.user = user;
        this.trip = trip;
        this.expts = expts;
        this.amount = amount;
        this.category = category;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "ExpenseWithMapper{" +
                "user='" + user + '\'' +
                ", trip='" + trip + '\'' +
                ", expts=" + expts +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", comment='" + comment + '\'' +
                '}';
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

    public Date getExpts() {
        return expts;
    }

    public void setExpts(Date expts) {
        this.expts = expts;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseWithMapper)) return false;
        ExpenseWithMapper that = (ExpenseWithMapper) o;
        return Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getTrip(), that.getTrip()) &&
                Objects.equals(getExpts(), that.getExpts()) &&
                Objects.equals(getAmount(), that.getAmount()) &&
                Objects.equals(getCategory(), that.getCategory()) &&
                Objects.equals(getComment(), that.getComment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getTrip(), getExpts(), getAmount(), getCategory(), getComment());
    }
}
