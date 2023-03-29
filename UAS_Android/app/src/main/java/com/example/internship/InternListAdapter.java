package com.example.internship;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class InternListAdapter extends ArrayAdapter<InternModel> {
    private final AppCompatActivity context;
    private final ArrayList<InternModel> model;

    public InternListAdapter(AppCompatActivity context, ArrayList<InternModel> model){
        super(context, R.layout.single_intern_listview, model);
        this.model = model;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.single_intern_listview, null, true);

        TextView textViewNama = convertView.findViewById(R.id.listviewNama);
        TextView textViewDivisi = convertView.findViewById(R.id.listviewDivisi);

        InternModel x = model.get(position);

        Log.e("nama", String.valueOf(x.nama));

        textViewNama.setText(x.getNama());
        textViewDivisi.setText(x.getDivisi());
        return convertView;
    }
}
