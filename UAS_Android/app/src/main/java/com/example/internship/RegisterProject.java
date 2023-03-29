package com.example.internship;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.internship.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterProject extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    DatePicker startDate, endDate;
    EditText projName, projDesc;
    Button btn_register;
    ProgressDialog progressDialog;
    Integer id, access;
    String ProjName, ProjDesc;

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_project);

        //inisialisasi semua komponen
        init();

        //mengambil data dari halaman sebelumnya
        Intent x = getIntent();
        id = x.getIntExtra("id", 0);
        access = x.getIntExtra("access", 0);

        btn_register.setOnClickListener(view -> {
            ProjName = projName.getText().toString();
            ProjDesc = projDesc.getText().toString();

            //format tanggal input
            SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
            //format tanggal yang akan diup ke database
            SimpleDateFormat afterformat = new SimpleDateFormat("dd MMMM yyyy");
            String S_Date = "" + startDate.getDayOfMonth();
            String S_Month = "" + startDate.getMonth();
            String E_Date = "" + endDate.getDayOfMonth();
            String E_Month = "" + endDate.getMonth();

            //menambahkan angka '0' didepan tanggal dan bulan jika hanya 1 digit
            if (startDate.getDayOfMonth() < 10){
                S_Date = "0" + startDate.getDayOfMonth();
            }
            if (startDate.getMonth() < 9){
                S_Month = "0" + (startDate.getMonth() + 1);
            }
            if (endDate.getDayOfMonth() < 10){
                E_Date = "0" + endDate.getDayOfMonth();
            }
            if (endDate.getMonth() < 9){
                E_Month = "0" + (endDate.getMonth() + 1);
            }

            String StartDate = S_Date + " " + S_Month + " " + startDate.getYear();
            String EndDate = E_Date + " " + E_Month + " " + endDate.getYear();

            Date localDate1 = null, localDate2 = null;
            try {
                //mengubah data dari string ke date
                localDate1 = format.parse(StartDate);
                localDate2 = format.parse(EndDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //compare date 1 dan date 2
            if (localDate1.compareTo(localDate2) <= 0){
                //check apakah internet tersedia
                if (connectivityManager.getActiveNetworkInfo() != null
                        && connectivityManager.getActiveNetworkInfo().isAvailable()
                        && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    checkAdd(ProjName, ProjDesc, afterformat.format(localDate1), afterformat.format(localDate2));
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Start Date harus lebih kecil dari End Date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkAdd(final String ProjName, final String ProjDesc, final String StartDate, final String EndDate) {
        progressDialog.setMessage("Adding Project ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.addProject, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //ambil data berupa JSON object
                    JSONObject jsonObject = new JSONObject(response);

                    //success disini merupakan TAG pembeda antara operasi yang sukses atau tidak
                    //jika 1 maka operasi sukses, jika 0 maka gagal
                    Integer success = jsonObject.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterProject.this, RegisterProject1.class);
                        intent.putExtra("id", id);
                        intent.putExtra("access", access);
                        startActivity(intent);
                        finish();
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
        }) {
            @Override
            protected Map<String, String> getParams() {
                //masukan data yang akan di post disini berupa string
                Map<String, String> params = new HashMap<String, String>();
                params.put("ProjName", ProjName);
                params.put("ProjDesc", ProjDesc);
                params.put("StartDate", StartDate);
                params.put("EndDate", EndDate);

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    public void init(){
        //inisiasi komponen
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressDialog = new ProgressDialog(RegisterProject.this);
        projName = findViewById(R.id.proj_projname_text);
        projDesc = findViewById(R.id.proj_desc_text);
        startDate = findViewById(R.id.proj_start_date);
        endDate = findViewById(R.id.proj_end_date);
        btn_register = findViewById(R.id.proj_btn_register);
    }
}