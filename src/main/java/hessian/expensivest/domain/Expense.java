package hessian.expensivest.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table(value="expenses")
public class Expense implements Serializable{
    @PrimaryKey private ExpensePrimaryKey expensePrimaryKey;

    @Column("category") private String category;

    @Column("amount") private Double amount;

    @Column("comment") private String comment;

    public Expense(ExpensePrimaryKey expensePrimaryKey, String category, double amount, String comment) {
        this.expensePrimaryKey = expensePrimaryKey;
        this.category = category;
        this.amount = amount;
        this.comment = comment;
    }

    public String toString() {
        return String.format("Expense[user=%s, trip=%s, expts=%s, category=%s, amount=%s, comment=%s",
                expensePrimaryKey.getUser(), expensePrimaryKey.getTrip(), expensePrimaryKey.getExpts().toString(),
                category, amount.toString(), comment);
    }

    public ExpensePrimaryKey getExpensePrimaryKey() {
        return expensePrimaryKey;
    }

    public void setExpensePrimaryKey(ExpensePrimaryKey expensePrimaryKey) {
        this.expensePrimaryKey = expensePrimaryKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
