package com.surajvanshsv.expensetracker.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "expenses")
public class Expense {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private double amount;
    private String category;

    @TypeConverters(DateConverter.class)
    private Date date;

    // Constructor
    public Expense(String title, double amount, String category, Date date) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }
}
