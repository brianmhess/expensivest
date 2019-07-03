package hessian.expensivest.mapper;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import hessian.typeparser.AnyParser;

import java.text.ParseException;
import java.time.Instant;
import java.util.Objects;

@Entity
public class Expense {
    @PartitionKey
    private String user;

    @ClusteringColumn(1)
    private String trip;

    @ClusteringColumn(2)
    private Instant expts;

    private Double amount;

    private String category;

    private String comment;

    public Expense() { }

    public Expense(String user, String trip, Instant expts, Double amount, String category, String comment) {
        this.user = user;
        this.trip = trip;
        this.expts = expts;
        this.amount = amount;
        this.category = category;
        this.comment = comment;
    }

    @Override
    public String toString() {
        try {
            return "Expense{" +
                    "user='" + user + '\'' +
                    ", trip='" + trip + '\'' +
                    ", expts=" + AnyParser.defaultParser.format(expts, Instant.class) +
                    ", amount=" + amount +
                    ", category='" + category + '\'' +
                    ", comment='" + comment + '\'' +
                    '}';
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
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

    public Instant getExpts() {
        return expts;
    }

    public void setExpts(Instant expts) {
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
        if (!(o instanceof Expense)) return false;
        Expense that = (Expense) o;
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
