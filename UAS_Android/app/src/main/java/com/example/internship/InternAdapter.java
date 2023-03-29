package com.example.internship;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class InternAdapter extends RecyclerView.Adapter<InternAdapter.ViewProcessHolder>{
    Context context;
    private ArrayList<User> item;
    InternAdapter.onListListener mOnListListener;

    public InternAdapter(Context context, ArrayList<User> item, InternAdapter.onListListener onListListener) {
        this.context = context;
        this.item = item;
        this.mOnListListener = onListListener;
    }

    @Override
    public InternAdapter.ViewProcessHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_intern, parent, false);
        InternAdapter.ViewProcessHolder processHolder = new InternAdapter.ViewProcessHolder(view, mOnListListener);
        return processHolder;
    }

    @Override
    public void onBindViewHolder(InternAdapter.ViewProcessHolder holder, int position) {

        final User data = item.get(position);
        holder.nama_data.setText(data.getNama());
        holder.divisi_data.setText(data.getDivisi());
        holder.email_data.setText(data.getEmail());
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public class ViewProcessHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nama_data, divisi_data, email_data;
        InternAdapter.onListListener onListListener;
        public ViewProcessHolder(@NonNull View itemView, InternAdapter.onListListener onListListener) {
            super(itemView);
            nama_data = (TextView) itemView.findViewById(R.id.name);
            divisi_data = (TextView) itemView.findViewById(R.id.div);
            email_data = (TextView) itemView.findViewById(R.id.email);
            this.onListListener = onListListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onListListener.onListClick(getAdapterPosition());
        }
    }

    public interface onListListener{
        void onListClick(int position);
    }
}
