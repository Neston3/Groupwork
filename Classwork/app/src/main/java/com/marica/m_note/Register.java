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
import com.marica.m_note.requestqueue.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button btn;
    EditText username, email, passwrd, conpasswrd;
    String Username, Email, Passwrd, Conpasswrd;
    String SERVER_URL = "https://maricajr.000webhostapp.com/server_con/register.php";
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = findViewById(R.id.edit_name);
        email = findViewById(R.id.edit_email);
        passwrd = findViewById(R.id.edit_password);
        conpasswrd = findViewById(R.id.edit_con);
        btn = findViewById(R.id.btn_submit);
        builder = new AlertDialog.Builder(Register.this);
        progressDialog = new ProgressDialog(Register.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //starl loading
                progressDialog.setMessage("Registering...");
                progressDialog.show();
                //setting uncancelable on touch the screen
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                Username = username.getText().toString();
                Email = email.getText().toString();
                Passwrd = passwrd.getText().toString();
                Conpasswrd = conpasswrd.getText().toString();

                if (Username.equals("") || Email.equals("") || Passwrd.equals("") || Conpasswrd
                        .equals("")) {
                    builder.setTitle("Something went wrong..");
                    builder.setMessage("Please check your fields.");
                    displayAlert("input error");
                    progressDialog.dismiss();
                } else {
                    if (!(Passwrd.equals(Conpasswrd))) {
                        builder.setTitle("Incorrect match");
                        builder.setMessage("Your Password don't match");
                        displayAlert("input error");
                        progressDialog.dismiss();
                    } else {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                                SERVER_URL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //give a respond to user if already exists
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code = jsonObject.getString("code");
                                    if (code.equals("reg failed")) {
                                        Toast.makeText(Register.this, "Similar user exist, check your email..", Toast.LENGTH_SHORT);
                                        progressDialog.dismiss();
                                        username.setText("");
                                        email.setText("");
                                        passwrd.setText("");
                                        conpasswrd.setText("");
                                    } else if (code.equals("reg success")) {
                                        Toast.makeText(Register.this, "Welcome", Toast.LENGTH_SHORT).show();
                                        //start new activity
                                        Intent intent = new Intent(getApplicationContext(), Home.class);
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.from_right, R.anim.to_left);
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
                            //post data if user is not found
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("name", Username);
                                params.put("email", Email);
                                params.put("password", Passwrd);

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
                        MySingleton.getInstance(Register.this).addToRequestQueue(stringRequest);
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
                    passwrd.setText("");
                    conpasswrd.setText("");
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left, R.anim.to_right);
    }
}
