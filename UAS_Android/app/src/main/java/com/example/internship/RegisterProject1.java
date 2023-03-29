package com.example.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.internship.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterProject1 extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    ProgressDialog progressDialog;
    ArrayList<String> internName = new ArrayList<>();
    Button btn_register, btn_complete;
    EditText jobDesc;
    Spinner spinner;
    String jobdesc;
    Integer id, access, idIntern;

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_project1);

        //inisiasi semua komponen
        init();

        //mengambil data dari halaman sebelumnya
        Intent getData = getIntent();
        id = getData.getIntExtra("id", 0);
        access = getData.getIntExtra("access", 0);

        //mengeset spinner dengan nama user
        loadSpinner();

        btn_register.setOnClickListener(view -> {
            idIntern = spinner.getSelectedItemPosition() + 1;
            jobdesc = jobDesc.getText().toString();

            //check apakah internet tersedia
            if (connectivityManager.getActiveNetworkInfo() != null
                    && connectivityManager.getActiveNetworkInfo().isAvailable()
                    && connectivityManager.getActiveNetworkInfo().isConnected()) {
                checkAdd(idIntern.toString(), jobdesc);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });

        btn_complete.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterProject1.this, HomeActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("access", access);
            startActivity(intent);
            finish();
        });
    }

    private void loadSpinner(){
        progressDialog.setMessage("Adding Project ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Config.spinnerName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //mengambil data dalam bentuk array dari string
                    JSONArray arr = new JSONArray(response);

                    //mengisi array dengan nama intern
                    for (int i = 0; i < arr.length(); i++){
                        JSONObject jsonObject = arr.getJSONObject(i);
                        internName.add(jsonObject.getString("nama"));
                    }
                    //mengisi spinner dengan array yang tadi
                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, internName);
                    spinner.setAdapter(adapter);
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
        });
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    private void checkAdd(final String idIntern, final String JobDesc) {
        progressDialog.setMessage("Adding Project ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.addDetailProject, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //ambil data berupa JSON object
                    JSONObject jObj = new JSONObject(response);

                    //success disini merupakan TAG pembeda antara operasi yang sukses atau tidak
                    //jika 1 maka operasi sukses, jika 0 maka gagal
                    Integer success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    } else {
                        //disini ditampilkan message kegagalan
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                //masukan data yang akan di post disini berupa string
                Map<String, String> params = new HashMap<String, String>();
                params.put("idIntern", idIntern);
                params.put("jobDesc", JobDesc);

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    public void init(){
        //inisiasi komponen
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressDialog = new ProgressDialog(this);
        btn_complete = findViewById(R.id.proj_btn_selesai);
        btn_register = findViewById(R.id.proj_btn_register);
        jobDesc = findViewById(R.id.proj_jobdesc_text);
        spinner = findViewById(R.id.proj_name_spinner);
    }
}