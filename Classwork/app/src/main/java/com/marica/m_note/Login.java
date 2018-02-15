package com.marica.m_note;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.marica.m_note.SharedPreference.MyPreferenceManager;
import com.marica.m_note.internetCheck.InternetConnectionCheck;
import com.marica.m_note.pojoclass.User;
import com.marica.m_note.requestqueue.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private static final String SERVER_URL = "https://maricajr.000webhostapp.com/server_con/login.php";
    Button btn;
    EditText username, password;
    AlertDialog.Builder builder;
    private String Username, Password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn = findViewById(R.id.btn_signin);
        username = findViewById(R.id.edit1);
        password = findViewById(R.id.edit2);
        builder = new AlertDialog.Builder(Login.this);
        progressDialog=new ProgressDialog(Login.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show progress dialog
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                Username = username.getText().toString();
                Password = password.getText().toString();
                if (Username.equals("") || Password.equals("")) {
                    builder.setTitle("Something went wrong..");
                    builder.setMessage("Please check your fields.");
                    displayAlert("input error");
                    progressDialog.dismiss();
                } else {
                    //check if connected to the internet
                    boolean check = InternetConnectionCheck.isConnectedToInternet(Login.this);
                    if (check){
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                SERVER_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code=jsonObject.getString("code");

                                    if (!code.equals("Incorrect password")){

                                        Toast.makeText(Login.this,"Login Successful..",Toast.LENGTH_SHORT).show();
                                        username.setText("");
                                        password.setText("");

                                        //creating a new user object
                                        User user = new User(
                                                jsonObject.getInt("id"),
                                                jsonObject.getString("name"),
                                                jsonObject.getString("email")
                                        );
                                        //storing user in the sharedpreferences
                                        MyPreferenceManager.getInstance(getApplicationContext()).userLogin(user);

                                        //starting the profile activity
                                        finish();
                                        Intent intent=new Intent(getApplicationContext(),Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.from_right,R.anim.to_left);
                                        progressDialog.dismiss();

                                    }else{
                                        Toast.makeText(Login.this,"Login failed...",Toast.
                                                LENGTH_SHORT).show();
                                        username.setText("");
                                        password.setText("");
                                        progressDialog.dismiss();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", Username);
                                params.put("password", Password);

                                return params;
                            }

                            @Override
                            public RetryPolicy getRetryPolicy() {
                                setRetryPolicy(new DefaultRetryPolicy(
                                        5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                return super.getRetryPolicy();
                            }
                        };
                        MySingleton.getInstance(Login.this).addToRequestQueue(stringRequest);
                    }else {
                        Toast.makeText(Login.this,"Connect to the internet",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }

    private void displayAlert(final String s) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (s.equals("input error")) {
                    password.setText("");
                } else if (s.equals("reg success")) {
                    finish();
                } else if (s.equals("reg failed")) {
                    username.setText("");
                    password.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left,R.anim.to_right);
    }

}
