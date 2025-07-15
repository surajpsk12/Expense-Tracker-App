package com.surajvanshsv.expensetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.surajvanshsv.expensetracker.data.model.Expense;

import java.util.List;
@Dao
public interface ExpenseDao {

    @Insert
    void insert(Expense expense);

    @Delete
    void delete(Expense expense);

    @androidx.room.Update
    void update(Expense expense);

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    LiveData<List<Expense>> getAllExpenses();

    @Query("SELECT SUM(amount) FROM expenses")
    LiveData<Double> getTotalExpense();

    @Query("SELECT SUM(amount) FROM expenses WHERE date(date / 1000, 'unixepoch') = date('now')")
    LiveData<Double> getTodaysExpense();

    @Query("SELECT SUM(amount) FROM expenses WHERE category = :category")
    LiveData<Double> getExpenseByCategory(String category);

    @Query("SELECT * FROM expenses WHERE category = :category ORDER BY date DESC")
    LiveData<List<Expense>> filterByCategory(String category);

    // ✅ Filter expenses by month and year
    @Query("SELECT * FROM expenses WHERE strftime('%m', date / 1000, 'unixepoch') = :month AND strftime('%Y', date / 1000, 'unixepoch') = :year ORDER BY date DESC")
    LiveData<List<Expense>> filterByMonth(String month, String year);
}
