package hessian.expensivest.spring;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table(value="expenses")
public class Expense implements Serializable{

    @PrimaryKey private ExpensePrimaryKey key;

    @Column("category") private String category;

    @Column("amount") private Double amount;

    @Column("comment") private String comment;

    public Expense() { }

    public Expense(String category, Double amount, String comment, ExpensePrimaryKey key) {
        this.category = category;
        this.amount = amount;
        this.comment = comment;
        this.key = key;
    }

    public Expense(Expense expense) {
        this.category = expense.getCategory();
        this.amount = expense.getAmount();
        this.comment = expense.getComment();
        this.key = expense.getKey();
    }

    @Override
    public String toString() {
        return "Expense{" +
                "category='" + category + '\'' +
                ", amount=" + amount +
                ", comment='" + comment + '\'' +
                ", key=" + key +
                '}';
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

    public ExpensePrimaryKey getKey() {
        return key;
    }

    public void setKey(ExpensePrimaryKey key) {
        this.key = key;
    }
}

