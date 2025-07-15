package com.surajvanshsv.expensetracker.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.surajvanshsv.expensetracker.data.model.Expense;
import com.surajvanshsv.expensetracker.repository.ExpenseRepository;

import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository repository;
    private LiveData<List<Expense>> allExpenses;
    private LiveData<Double> totalExpense;
    private LiveData<Double> todaysExpense;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExpenseRepository(application);
        allExpenses = repository.getAllExpenses();
        totalExpense = repository.getTotalExpense();
        todaysExpense = repository.getTodaysExpense();
    }

    public void insert(Expense expense) {
        repository.insert(expense);
    }

    public void delete(Expense expense) {
        repository.delete(expense);
    }

    public LiveData<List<Expense>> getAllExpenses() {
        return allExpenses;
    }

    public LiveData<Double> getTotalExpense() {
        return totalExpense;
    }

    public LiveData<Double> getTodaysExpense() {
        return todaysExpense;
    }

    public LiveData<Double> getExpenseByCategory(String category) {
        return repository.getExpenseByCategory(category);
    }

    public LiveData<List<Expense>> filterByCategory(String category) {
        return repository.filterByCategory(category);
    }
}
