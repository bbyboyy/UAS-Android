package com.example.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.internship.config.Config;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home1Activity extends AppCompatActivity implements ProjectAdapter.onListListener, InternAdapter.onListListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressDialog progressDialog;
    FloatingActionButton floatingActionButton;
    ArrayList<User> user = new ArrayList<>();
    ArrayList<Integer> idIntern = new ArrayList<>();

    Integer id, access;
    String url;

    private static final String TAG_ERROR = "error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home1);

        //inisialisasi bottom navigation
        //bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        //set selected nav
        bottomNavigationView.setSelectedItemId(R.id.intern);
        //navigation onclick
        //noinspection deprecation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.project:
                        Intent intent = new Intent(Home1Activity.this, HomeActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("access", access);
                        startActivity(intent);
                        return true;
                    case R.id.intern:
                        return true;
                    case R.id.logout:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        //inisialisasi semua komponen
        init();

        //inisiasi floating action button
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home1Activity.this, RegisterActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("access", access);
                startActivity(intent);
            }
        });

        //mendapat data dari halaman sebelumnya
        Intent x = getIntent();
        id = x.getIntExtra("id", 0);
        access = x.getIntExtra("access", 0);

        //membedakan antara akses admin dan non admin
        if(access == 1){
            url = Config.getDataIntern;
        }else {
            url = Config.getDataInternNonAdm;
            floatingActionButton.setVisibility(View.GONE);
        }

        //load data berupa json kedalam activity
        loadData();
    }

    private void loadData(){
        progressDialog.setMessage("Mengambil Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest arrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //mengambil data dalam bentuk array dari string
                    JSONArray arr = new JSONArray(response);

                    //mengisi setiap item dengan data yang tadi diambil
                    for (int i  = 0; i < arr.length(); i++){
                        JSONObject data = arr.getJSONObject(i);
                        User usr = new User();
                        usr.setNama(data.getString("nama"));
                        usr.setDivisi(data.getString("divisi"));
                        usr.setEmail(data.getString("email"));
                        user.add(usr);
                        idIntern.add(data.getInt("id"));
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    //JSON exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VOLLEY exception
                progressDialog.cancel();
                Log.e(TAG_ERROR, error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //masukan data yang akan di post disini berupa string
                Map<String, String> params = new HashMap<String, String>();
                params.put("iduser", id.toString());

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(arrayRequest);
    }

    @Override
    public void onListClick(int position) {
        //jika item di click, maka akan berpindah
        Intent intent = new Intent(Home1Activity.this, DetailActivityIntern.class);
        intent.putExtra("id", id);
        intent.putExtra("idintern", idIntern.get(position));
        intent.putExtra("access", access);
        startActivity(intent);
    }

    public void init(){
        //komponen inisiasi
        recyclerView = findViewById(R.id.recyclerView);
        progressDialog = new ProgressDialog(Home1Activity.this);
        layoutManager = new LinearLayoutManager(Home1Activity.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new InternAdapter(this, user, this);
        floatingActionButton = findViewById(R.id.floatingButton);
        recyclerView.setAdapter(adapter);
    }
}