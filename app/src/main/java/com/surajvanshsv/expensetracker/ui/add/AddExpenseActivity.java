package com.surajvanshsv.expensetracker.ui.add;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.*;
import com.surajvanshsv.expensetracker.R;
import com.surajvanshsv.expensetracker.data.model.Expense;
import com.surajvanshsv.expensetracker.viewmodel.ExpenseViewModel;

import java.text.SimpleDateFormat;
import java.util.*;

public class AddExpenseActivity extends AppCompatActivity {

    private TextInputEditText editTextTitle, editTextAmount, textViewDate;
    private MaterialAutoCompleteTextView spinnerCategory;
    private MaterialButton buttonSave, buttonCancel;
    private MaterialButton btnQuickAmount1, btnQuickAmount2, btnQuickAmount3;
    private ExpenseViewModel viewModel;
    private Date selectedDate = new Date(); // Default to today

    private int editingId = -1; // Default for new

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        // Toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAmount = findViewById(R.id.editTextAmount);
        textViewDate = findViewById(R.id.textViewDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        btnQuickAmount1 = findViewById(R.id.btnQuickAmount1);
        btnQuickAmount2 = findViewById(R.id.btnQuickAmount2);
        btnQuickAmount3 = findViewById(R.id.btnQuickAmount3);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Category dropdown
        String[] categories = {"Food", "Travel", "Bills", "Shopping", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categories);
        spinnerCategory.setAdapter(adapter);

        // Check for edit mode
        if (getIntent().hasExtra("id")) {
            editingId = getIntent().getIntExtra("id", -1);
            String title = getIntent().getStringExtra("title");
            double amount = getIntent().getDoubleExtra("amount", 0);
            String category = getIntent().getStringExtra("category");
            long dateMillis = getIntent().getLongExtra("date", new Date().getTime());
            selectedDate = new Date(dateMillis);

            editTextTitle.setText(title);
            editTextAmount.setText(String.valueOf(amount));
            spinnerCategory.setText(category, false);
        }

        updateDateText();

        // Date picker
        textViewDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTime();
                updateDateText();
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Quick amount buttons
        btnQuickAmount1.setOnClickListener(v -> editTextAmount.setText("100"));
        btnQuickAmount2.setOnClickListener(v -> editTextAmount.setText("500"));
        btnQuickAmount3.setOnClickListener(v -> editTextAmount.setText("1000"));

        // Save logic
        buttonSave.setOnClickListener(v -> {
            String title = editTextTitle.getText().toString().trim();
            String amtStr = editTextAmount.getText().toString().trim();
            String category = spinnerCategory.getText().toString().trim();

            if (title.isEmpty() || amtStr.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amtStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
                return;
            }

            Expense expense = new Expense(title, amount, category, selectedDate);

            if (editingId != -1) {
                expense.setId(editingId);
                viewModel.update(expense);
                Toast.makeText(this, "Expense updated", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.insert(expense);
                Toast.makeText(this, "Expense saved", Toast.LENGTH_SHORT).show();
            }

            finish();
        });

        // Cancel
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        textViewDate.setText(sdf.format(selectedDate));
    }
}
