package com.example.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.internship.config.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProjectActivity extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    DatePicker startDate, endDate;
    EditText projName, projDesc;
    Button btn_submit;
    ProgressDialog progressDialog;
    Integer id, access, idProj;
    String ProjName, ProjDesc;

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_project);

        //inisialisasi semua komponen
        init();

        //set text
        setText();

        //mengambil data dari halaman sebelumnya
        Intent getData = getIntent();
        id = getData.getIntExtra("id", 0);
        access = getData.getIntExtra("access", 0);
        idProj = getData.getIntExtra("idProj", -1);

        btn_submit.setOnClickListener(view -> {
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
                    update(ProjName, ProjDesc, afterformat.format(localDate1), afterformat.format(localDate2));
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Start Date harus lebih kecil dari End Date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setText(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.getDataDetail, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    JSONArray arr = new JSONArray(response);
                    JSONObject jsonObject = arr.getJSONObject(arr.length()-1);
                    projName.setText(jsonObject.getString("namaproj"));
                    projDesc.setText(jsonObject.getString("deskripsi"));
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
                params.put("idproj", idProj.toString());

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    private void update(final String ProjName, final String ProjDesc, final String StartDate, final String EndDate) {
        progressDialog.setMessage("Updating Project ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.editProject, new Response.Listener<String>() {
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
                        Intent intent = new Intent(EditProjectActivity.this, HomeActivity.class);
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
                params.put("idProj", idProj.toString());
                params.put("namaProj", ProjName);
                params.put("descProj", ProjDesc);
                params.put("startDate", StartDate);
                params.put("endDate", EndDate);

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    public void init(){
        //inisiasi komponen
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        progressDialog = new ProgressDialog(EditProjectActivity.this);
        projName = findViewById(R.id.edit_projname_text);
        projDesc = findViewById(R.id.edit_desc_text);
        startDate = findViewById(R.id.edit_start_date);
        endDate = findViewById(R.id.edit_end_date);
        btn_submit = findViewById(R.id.edit_btn_proj);
    }
}