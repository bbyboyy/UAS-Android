package com.example.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.internship.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivityIntern extends AppCompatActivity {

    Integer id, idIntern, access;
    String url;
    TextView txt_nama, txt_email, txt_notelp, txt_alamat, txt_aboutme,
            txt_project, txt_divisi, txt_status;
    ConnectivityManager connectivityManager;
    ProgressDialog progressDialog;

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail1);

        //inisialisasi semua komponen
        init();

        //inisiasi toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mendapatkan data dari halaman intern
        Intent getData = getIntent();
        id = getData.getIntExtra("id", 0);
        idIntern = getData.getIntExtra("idintern", -1);
        access = getData.getIntExtra("access", 0);

        //membedakan akses antara admin dan non admin
        if(access == 1){
            url = Config.getDataDetailIntern;
        }else {
            url = Config.getDataDetailInternNonAdm;
        }

        //load data berupa json kedalam activity
        loadData();
    }

    private void loadData(){
        progressDialog.setMessage("Mengambil Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //mengambil data dalam bentuk array dari string
                    JSONArray arr = new JSONArray(response);
                    String temp = "";

                    //mengisi setiap item dengan data yang tadi diambil
                    for(int i = 0; i < arr.length(); i++){
                        JSONObject data = arr.getJSONObject(i);
                        txt_nama.setText(data.getString("nama"));
                        txt_divisi.setText(data.getString("divisi"));
                        txt_email.setText(data.getString("email"));
                        txt_notelp.setText(data.getString("notelp"));
                        txt_alamat.setText(data.getString("alamat"));
                        txt_aboutme.setText(data.getString("about"));
                        txt_status.setText(data.getString("performance"));
                        temp += "\n" + data.getString("namaproject");

                    }
                    txt_project.setText("Project\t: " + temp);
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
                params.put("idintern", idIntern.toString());

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    private void delete(final String idintern) {
        progressDialog.setMessage("Menghapus Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.deleteIntern, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //mengambil data dalam bentuk json object
                    JSONObject jsonObject = new JSONObject(response);

                    //success disini merupakan TAG pembeda antara operasi yang sukses atau tidak
                    //jika 1 maka operasi sukses, jika 0 maka gagal
                    Integer success = jsonObject.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(DetailActivityIntern.this, Home1Activity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("access", access);
                        finish();
                        startActivity(intent);
                    } else {
                        //disini ditampilkan message kegagalan
                        Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON exception
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //VOLLEY error
                progressDialog.cancel();
                Log.e(TAG_ERROR, error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                //masukan data yang akan di post disini berupa string
                Map<String, String> params = new HashMap<String, String>();
                params.put("idIntern", idintern);

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    //option menu edit dan delete
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        if(access == 1){
            return true;
        }else {
            return false;
        }
    }

    //set intent menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.reload:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                //check internet apakah tersedia
                if (connectivityManager.getActiveNetworkInfo() != null
                        && connectivityManager.getActiveNetworkInfo().isAvailable()
                        && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    delete(idIntern.toString());
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            case R.id.menu_edit:
                //intent ke halaman edit
                Intent intent = new Intent(DetailActivityIntern.this, EditInternActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("access", access);
                intent.putExtra("idIntern", idIntern);
                startActivity(intent);
        }
        return true;
    }

    private void init(){
        //komponen inisiasi
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressDialog = new ProgressDialog(DetailActivityIntern.this);
        txt_nama = findViewById(R.id.textViewNamaIntern);
        txt_email = findViewById(R.id.textViewEmailIntern);
        txt_notelp = findViewById(R.id.textViewNomorTelpon);
        txt_alamat = findViewById(R.id.textViewAlamatIntern);
        txt_aboutme = findViewById(R.id.textViewAboutMe);
        txt_project = findViewById(R.id.textViewNamaProjectIntern);
        txt_divisi = findViewById(R.id.textViewNamaDivisi);
        txt_status = findViewById(R.id.textViewPerformance);
    }
}