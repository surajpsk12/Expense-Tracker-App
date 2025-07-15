package com.surajvanshsv.expensetracker.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.surajvanshsv.expensetracker.data.dao.ExpenseDao;
import com.surajvanshsv.expensetracker.data.db.ExpenseDatabase;
import com.surajvanshsv.expensetracker.data.model.Expense;

import java.util.List;

public class ExpenseRepository {
    private ExpenseDao expenseDao;

    public ExpenseRepository(Application application) {
        ExpenseDatabase db = ExpenseDatabase.getInstance(application);
        expenseDao = db.expenseDao();
    }

    public void insert(Expense expense) {
        new Thread(() -> expenseDao.insert(expense)).start();
    }

    public void delete(Expense expense) {
        new Thread(() -> expenseDao.delete(expense)).start();
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return expenseDao.getAllExpenses();
    }

    public LiveData<Double> getTotalExpense() {
        return expenseDao.getTotalExpense();
    }

    public LiveData<Double> getTodaysExpense() {
        return expenseDao.getTodaysExpense();
    }

    public LiveData<Double> getExpenseByCategory(String category) {
        return expenseDao.getExpenseByCategory(category);
    }

    public LiveData<List<Expense>> filterByCategory(String category) {
        return expenseDao.filterByCategory(category);
    }
}
