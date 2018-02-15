package com.marica.m_note;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.marica.m_note.Adapter.NotesAdapter;
import com.marica.m_note.SharedPreference.MyPreferenceManager;
import com.marica.m_note.pojoclass.Note;
import com.marica.m_note.pojoclass.User;
import com.marica.m_note.requestqueue.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trash extends AppCompatActivity {

    private static final String SERVER_URL_DELETE_LIST="https://maricajr.000webhostapp.com/server_con/delete_list.php";
    private List<Note> noteList = new ArrayList<>();
    private NotesAdapter mAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new NotesAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        DeleteList();
    }

    private void DeleteList() {

        StringRequest stringRequest=new StringRequest(Request.Method.POST, SERVER_URL_DELETE_LIST,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray=new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        String title=jsonObject.getString("title");
                        String detail=jsonObject.getString("detail");
                        String created_at= jsonObject.getString("create_at");
                        String updated_at= jsonObject.getString("update_at");

                        Note note=new Note(title,detail,created_at,updated_at);
                        noteList.add(note);

                        mAdapter.notifyDataSetChanged();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                    Toast.makeText(Trash.this,"Server not found...try again",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Trash.this,"Check your internet connection..",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Trash.this,"Oops...",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Trash.this,"Cannot connect to the internet",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Trash.this,"Connection Timed Out",Toast.LENGTH_SHORT).show();
                }

            }
        }){
            //get current user id
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                User user= MyPreferenceManager.getInstance(getApplicationContext()).getUser();
                int id=user.getId();

                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(id));

                return params;
            }
        };

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        stringRequest.setShouldCache(true);

    }

}
