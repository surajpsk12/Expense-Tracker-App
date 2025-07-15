package com.surajvanshsv.expensetracker.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.surajvanshsv.expensetracker.data.dao.ExpenseDao;
import com.surajvanshsv.expensetracker.data.model.DateConverter;
import com.surajvanshsv.expensetracker.data.model.Expense;

@Database(entities = {Expense.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class ExpenseDatabase extends RoomDatabase {

    private static ExpenseDatabase instance;

    public abstract ExpenseDao expenseDao();

    public static synchronized ExpenseDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ExpenseDatabase.class,
                            "expense_database"
                    ).fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
