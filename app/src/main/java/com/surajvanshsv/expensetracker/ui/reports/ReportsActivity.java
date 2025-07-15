package com.surajvanshsv.expensetracker.ui.reports;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.surajvanshsv.expensetracker.R;
import com.surajvanshsv.expensetracker.data.model.Expense;
import com.surajvanshsv.expensetracker.viewmodel.ExpenseViewModel;

import java.util.*;

public class ReportsActivity extends AppCompatActivity {

    private BarChart barChart;
    private Spinner spinnerMonth;
    private ExpenseViewModel viewModel;
    private List<Expense> currentExpenses = new ArrayList<>();

    private final String[] months = {
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        barChart = findViewById(R.id.barChartReport);
        spinnerMonth = findViewById(R.id.spinnerMonth);
        viewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        spinnerMonth.setAdapter(adapter);

        spinnerMonth.setSelection(Calendar.getInstance().get(Calendar.MONTH)); // default to current month

        viewModel.getAllExpenses().observe(this, expenses -> {
            currentExpenses = expenses;
            filterAndShow(spinnerMonth.getSelectedItemPosition());
        });

        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterAndShow(position);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void filterAndShow(int monthIndex) {
        Map<String, Float> categoryMap = new HashMap<>();
        Calendar cal = Calendar.getInstance();

        for (Expense e : currentExpenses) {
            cal.setTime(e.getDate());
            int expenseMonth = cal.get(Calendar.MONTH);
            if (expenseMonth == monthIndex) {
                String category = e.getCategory();
                float amount = (float) e.getAmount();
                categoryMap.put(category, categoryMap.getOrDefault(category, 0f) + amount);
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        int i = 0;
        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
            entries.add(new BarEntry(i, entry.getValue()));
            labels.add(entry.getKey());
            i++;
        }

        BarDataSet dataSet = new BarDataSet(entries, "Expenses by Category");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.9f);

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.setFitBars(true);
        barChart.animateY(1000);
        barChart.invalidate();
    }
}
