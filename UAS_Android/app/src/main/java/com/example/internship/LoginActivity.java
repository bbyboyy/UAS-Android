package com.example.internship;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.internship.config.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ConnectivityManager connectivityManager;
    ProgressDialog progressDialog;
    EditText txt_username, txt_password;
    Button btn_login;
    String username, password;

    private static final String TAG_ERROR = "error";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //inisiasi semua komponen
        init();

        btn_login.setOnClickListener(view -> {
            username = txt_username.getText().toString();
            password = txt_password.getText().toString();

            //check apakah username dan password penuh
            if (username.trim().length() > 0 && password.trim().length() > 0) {
                //check apakah ada internet
                if (connectivityManager.getActiveNetworkInfo() != null
                        && connectivityManager.getActiveNetworkInfo().isAvailable()
                        && connectivityManager.getActiveNetworkInfo().isConnected()) {
                    checkLogin(username, password);
                } else {
                    Toast.makeText(getApplicationContext() ,"No Internet Connection", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext() ,"Username dan Password tidak boleh kosong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void checkLogin(final String username, final String password) {
        progressDialog.setMessage("Logging in ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.loginPhp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.cancel();
                try {
                    //mengambil data dalam bentuk array dari string
                    JSONObject jObj = new JSONObject(response);

                    //success disini merupakan TAG pembeda antara operasi yang sukses atau tidak
                    //jika 1 maka operasi sukses, jika 0 maka gagal
                    Integer success = jObj.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        String username = jObj.getString("username");
                        Integer id = jObj.getInt("id");
                        Integer access = jObj.getInt("access");
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("id", id);
                        intent.putExtra("access", access);
                        finish();
                        startActivity(intent);
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
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };
        //menambahkan ke request queue untuk dipost ke alamat php yang dituju
        Controller.getInstance().addToRequestQueue(stringRequest);
    }

    public void init(){
        //komponen inisiasi
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        btn_login = findViewById(R.id.login_btn_login);
        txt_username = findViewById(R.id.login_uname_text);
        txt_password = findViewById(R.id.login_pass_text);
        progressDialog = new ProgressDialog(LoginActivity.this);
    }
}