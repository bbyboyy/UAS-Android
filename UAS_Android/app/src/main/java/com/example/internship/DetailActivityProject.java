package com.example.internship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuInflater;

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

public class DetailActivityProject extends AppCompatActivity {

    Integer id, idProj, access;
    String url;
    TextView txt_nama, txt_deskripsi, txt_start, txt_end;
    ConnectivityManager connectivityManager;
    Toolbar toolbar;
    ProgressDialog progressDialog;
    ListView listViewIntern;

    public ArrayList<InternModel> modelIntern = new ArrayList<InternModel>();

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //inisialisasi semua komponen
        init();

        //inisiasi toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mendapat data dari halaman project
        Intent x = getIntent();
        id = x.getIntExtra("id", 0);
        idProj = x.getIntExtra("idproj", -1);
        access = x.getIntExtra("access", 0);

        //membedakan antara akses admin dan non admin
        url = Config.getDataDetail;

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
                    String temp1 = "";

                    //mengisi setiap item dengan data yang tadi diambil
                    for (int i = 0; i < arr.length(); i++){
                        JSONObject data = arr.getJSONObject(i);
                        txt_nama.setText(data.getString("namaproj"));
                        //temp1 += data.getString("namaintern") + "\n Jobdesc :  " + data.getString("jobdesc") + "\n";
                        txt_deskripsi.setText(data.getString("deskripsi"));
                        txt_start.setText(data.getString("startdate"));
                        txt_end.setText(data.getString("enddate"));
                        modelIntern.add(new InternModel(data.getString("namaintern"), data.getString("divisiintern")));
                    }
                    //txt_namaIntern.setText(temp1);
                } catch (JSONException e) {
                    //JSON exception
                    e.printStackTrace();
                }
                InternListAdapter adapter = new InternListAdapter(DetailActivityProject.this, modelIntern);
                listViewIntern.setAdapter(adapter);
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
                params.put("idproj", idProj.toString());

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(arrayRequest);
    }

    private void delete(final String idIntern) {
        progressDialog.setMessage("Menghapus Data");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.deleteProject, new Response.Listener<String>() {
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
                        Intent intent = new Intent(DetailActivityProject.this, HomeActivity.class);
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
                params.put("idProj", idIntern);

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        switch (item.getItemId()){
            case R.id.reload:
                Toast.makeText(this, "Refreshing...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                //check internet apakah tersedia
                if (connectivityManager.getActiveNetworkInfo() != null
                        && connectivityManager.getActiveNetworkInfo().isAvailable()
                        && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    delete(idProj.toString());
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            case R.id.menu_edit:
                //intent ke halaman edit
                Intent intent = new Intent(DetailActivityProject.this, EditProjectActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("access", access);
                intent.putExtra("idProj", idProj);
                startActivity(intent);
        }
        return true;
    }

    private void init(){
        //komponen inisiasi
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressDialog = new ProgressDialog(DetailActivityProject.this);
        txt_nama = findViewById(R.id.textViewNamaProject);
        txt_deskripsi = findViewById(R.id.projectDescription);
        listViewIntern = findViewById(R.id.intern_listView);
        txt_start = findViewById(R.id.startDate);
        txt_end = findViewById(R.id.endDate);
    }
}