package com.surajvanshsv.expensetracker.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.surajvanshsv.expensetracker.R;
import com.surajvanshsv.expensetracker.data.model.Expense;

import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Expense expense);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setExpenseList(List<Expense> list) {
        this.expenseList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense exp = expenseList.get(position);
        holder.textTitle.setText(exp.getTitle());
        holder.textAmount.setText("₹" + exp.getAmount());
        holder.textCategory.setText(exp.getCategory());
        holder.textDate.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(exp.getDate()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(exp);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle, textAmount, textCategory, textDate;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textAmount = itemView.findViewById(R.id.textAmount);
            textCategory = itemView.findViewById(R.id.textCategory);
            textDate = itemView.findViewById(R.id.textDate);
        }
    }
}
