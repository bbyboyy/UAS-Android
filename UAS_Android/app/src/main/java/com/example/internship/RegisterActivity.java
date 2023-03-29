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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.internship.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    ConnectivityManager connectivityManager;
    EditText txt_division, txt_phone, txt_address, txt_fullname, txt_email, txt_username, txt_password, txt_confirm_password;
    Button btn_register;
    Spinner txt_status;
    Integer id, access;

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //inisialisasi semua komponen
        init();

        //inisialisasi array performance
        String[] performance = new String[]{
                "Choose Performance", "Bad", "Good",
                "Very Good", "Excellent"
        };

        //arraylist spinner
        ArrayList<String> spinnerArray = new ArrayList<String>(Arrays.asList(performance));

        //spinner Arrayadapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerArray){
            @Override
            public boolean isEnabled(int position) {
                //menonaktifkan pilihan pertama (choose performance)
                if(position == 0){
                    return false;
                }
                else
                    return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                //mengubah warna text spinner
                if(position == 0){
                    textView.setTextColor(Color.GRAY);
                }
                else
                    textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txt_status.setAdapter(spinnerArrayAdapter);

        //mengambil data dari halaman sebelumnya
        Intent getData = getIntent();
        id = getData.getIntExtra("id", 0);
        access = getData.getIntExtra("access", 0);

        btn_register.setOnClickListener(view -> {
            String username = txt_username.getText().toString();
            String password = txt_password.getText().toString();
            String confirm_password = txt_confirm_password.getText().toString();
            String email = txt_email.getText().toString();
            String fullname = txt_fullname.getText().toString();
            String address = txt_address.getText().toString();
            String division = txt_division.getText().toString();
            String status = txt_status.getSelectedItem().toString();
            String phone = txt_phone.getText().toString();

            //check apakah internet tersedia
            if (connectivityManager.getActiveNetworkInfo() != null
                    && connectivityManager.getActiveNetworkInfo().isAvailable()
                    && connectivityManager.getActiveNetworkInfo().isConnected()) {
                checkRegister(username, password, confirm_password, email, fullname, address, division, status, phone);
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkRegister(final String username, final String password, final String confirm_password, final String email, final String fullname, final String address, final String division, final String status, final String phone) {
        progressDialog.setMessage("Register ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.registerPhp, new Response.Listener<String>() {
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
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("access", access);
                        startActivity(intent);
                        finish();
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
        }){
            @Override
            protected Map<String, String> getParams() {
                //masukan data yang akan di post disini berupa string
                Map<String, String> params = new HashMap<String, String>();
                params.put("fullname", fullname);
                params.put("username", username);
                params.put("password", password);
                params.put("confirm_password", confirm_password);
                params.put("address", address);
                params.put("email", email);
                params.put("phone", phone);
                params.put("division", division);
                params.put("status", status);

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    public void init(){
        //inisiasi komponen
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        btn_register = (Button) findViewById(R.id.regist_btn_register);
        txt_fullname = (EditText) findViewById(R.id.regist_fullname_text);
        txt_username = (EditText) findViewById(R.id.regist_uname_text);
        txt_email = (EditText) findViewById(R.id.regist_email_text);
        txt_password = (EditText) findViewById(R.id.regist_pass_text);
        txt_confirm_password = (EditText) findViewById(R.id.regist_confpass_text);
        txt_address = (EditText) findViewById(R.id.regist_address_text);
        txt_division = (EditText) findViewById(R.id.regist_div_text);
        txt_phone = (EditText) findViewById(R.id.regist_phone_text);
        txt_status = (Spinner) findViewById(R.id.regist_status_spinner);
        progressDialog = new ProgressDialog(RegisterActivity.this);
    }
}