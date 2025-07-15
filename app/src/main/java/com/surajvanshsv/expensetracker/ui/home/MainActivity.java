package com.surajvanshsv.expensetracker.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.surajvanshsv.expensetracker.R;
import com.surajvanshsv.expensetracker.data.model.Expense;
import com.surajvanshsv.expensetracker.ui.add.AddExpenseActivity;
import com.surajvanshsv.expensetracker.ui.reports.ReportsActivity;
import com.surajvanshsv.expensetracker.viewmodel.ExpenseViewModel;

import java.text.NumberFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private ExpenseViewModel viewModel;
    private TextView textTotalExpense, textTodayExpense;
    private RecyclerView recyclerExpenses;
    private ExpenseAdapter adapter;
    private FloatingActionButton fabAdd;
    private PieChart pieChart;
    private BarChart barChart;
    private MaterialButton btnToggleChart, btnViewAll;
    private MaterialCardView cardQuickAdd, cardCategories, cardReports;
    private ProgressBar progressBudget;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // UI references
        textTotalExpense = findViewById(R.id.textTotalExpense);
        textTodayExpense = findViewById(R.id.textTodayExpense);
        recyclerExpenses = findViewById(R.id.recyclerExpenses);
        fabAdd = findViewById(R.id.fabAddExpense);
        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.barChart);
        btnToggleChart = findViewById(R.id.btnToggleChart);
        btnViewAll = findViewById(R.id.btnViewAll);
        cardQuickAdd = findViewById(R.id.cardQuickAdd);
        cardCategories = findViewById(R.id.cardCategories);
        cardReports = findViewById(R.id.cardReports);
        progressBudget = findViewById(R.id.progressBudget);
        emptyState = findViewById(R.id.emptyState);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // RecyclerView
        adapter = new ExpenseAdapter();
        recyclerExpenses.setLayoutManager(new LinearLayoutManager(this));
        recyclerExpenses.setAdapter(adapter);

        // Observe data
        viewModel.getAllExpenses().observe(this, expenses -> {
            adapter.setExpenseList(expenses);
            updatePieChart(expenses);
            updateBarChart(expenses);
            emptyState.setVisibility(expenses.isEmpty() ? View.VISIBLE : View.GONE);

            adapter.setOnItemClickListener(expense -> {
                Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("id", expense.getId());
                intent.putExtra("title", expense.getTitle());
                intent.putExtra("amount", expense.getAmount());
                intent.putExtra("category", expense.getCategory());
                intent.putExtra("date", expense.getDate().getTime());
                startActivity(intent);
            });
        });

        viewModel.getTotalExpense().observe(this, total -> {
            textTotalExpense.setText(formatAmount(total));
        });

        viewModel.getTodaysExpense().observe(this, today -> {
            textTodayExpense.setText(formatAmount(today));
        });

        // FAB
        fabAdd.setOnClickListener(v -> startActivity(new Intent(this, AddExpenseActivity.class)));

        // Quick actions
        cardQuickAdd.setOnClickListener(v -> startActivity(new Intent(this, AddExpenseActivity.class)));
        cardReports.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReportsActivity.class);
            startActivity(intent);
        });
        cardCategories.setOnClickListener(v -> {
            // You can update this later with actual category management screen
            Toast.makeText(this, "Categories screen coming soon", Toast.LENGTH_SHORT).show();
        });

        // Toggle chart button
        btnToggleChart.setOnClickListener(v -> {
            if (pieChart.getVisibility() == View.VISIBLE) {
                pieChart.setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                btnToggleChart.setText("Bar Chart");
            } else {
                barChart.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
                btnToggleChart.setText("Pie Chart");
            }
        });

        // View all
        btnViewAll.setOnClickListener(v -> {
            Toast.makeText(this, "View all expenses coming soon", Toast.LENGTH_SHORT).show();
        });

        // Edge-to-edge system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String formatAmount(Double amt) {
        if (amt == null) amt = 0.0;
        return NumberFormat.getCurrencyInstance(new Locale("en", "IN")).format(amt);
    }

    private void updatePieChart(List<Expense> expenses) {
        Map<String, Float> categoryTotals = new HashMap<>();
        for (Expense e : expenses) {
            String category = e.getCategory();
            float amount = (float) e.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + amount);
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Category Wise");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(getResources().getColor(android.R.color.white));

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(getResources().getColor(android.R.color.black));
        pieChart.setCenterText("Expenses");
        pieChart.setCenterTextSize(16f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }

    private void updateBarChart(List<Expense> expenses) {
        Map<String, Float> categoryTotals = new LinkedHashMap<>();
        for (Expense e : expenses) {
            String category = e.getCategory();
            float amount = (float) e.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0f) + amount);
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, Float> entry : categoryTotals.entrySet()) {
            entries.add(new BarEntry(index, entry.getValue()));
            labels.add(entry.getKey());
            index++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Category Wise");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setValueTextSize(14f);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisRight().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
