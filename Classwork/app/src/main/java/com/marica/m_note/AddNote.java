package com.marica.m_note;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.marica.m_note.SharedPreference.MyPreferenceManager;
import com.marica.m_note.pojoclass.User;
import com.marica.m_note.requestqueue.MySingleton;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {
    Spinner spinners;
    ArrayAdapter<CharSequence> adapter;
    EditText notetitle,notedetail;
    String SERVER_URL = "https://maricajr.000webhostapp.com/server_con/save.php";
    String notes_title,notes_detail;
    private static final int CAMERA_REQUEST=1888;
    ImageView image;
    boolean imageFit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save your notes when clicked

            }
        });
        //find elements by id
        spinners=findViewById(R.id.spinner);
        notetitle=findViewById(R.id.note_title);
        notedetail=findViewById(R.id.note_details);
        image=findViewById(R.id.imageview);

        //working with spinner
        adapter=ArrayAdapter.createFromResource(this,R.array.notebook_name,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinners.setAdapter(adapter);
        spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.from_left,R.anim.to_right);
    }

    //onphoto taken view it activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==CAMERA_REQUEST && resultCode== Activity.RESULT_OK){
            Bitmap photo= (Bitmap) data.getExtras().get("data");
            image.setVisibility(View.VISIBLE);
            image.setImageBitmap(photo);
        }
    }

    //open the camera
    public void camera_click(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST);
    }

    public void attach_click(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //type of file you want to access
        intent.setType("image/*");
        startActivity(intent);
    }

    //on notes done save
    public void done_click(View view) {
        //text to string
        notes_title=notetitle.getText().toString();
        notes_detail=notedetail.getText().toString();
        if (!notes_title.isEmpty() && !notes_detail.isEmpty()){
            //send data to server
            StringRequest stringRequest= new StringRequest(Request.Method.POST, SERVER_URL
                    ,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Intent intent=new Intent(AddNote.this,Home.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.from_left,R.anim.to_right);
                    Toast.makeText(AddNote.this,"Saved",Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(),"error.......",Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
             }) {
                    //post data if user is not found
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        //getting the current user
                        User user= MyPreferenceManager.getInstance(AddNote.this).getUser();
                        int id=user.getId();

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("title", notes_title);
                        params.put("detail", notes_detail);
                        params.put("id", String.valueOf(id));

                        return params;
                    }
                 }
                ;
                MySingleton.getInstance(AddNote.this).addToRequestQueue(stringRequest);
        }else {
            Intent intent=new Intent(AddNote.this,Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_left,R.anim.to_right);
            Toast.makeText(this,"Cannot save an empty note",Toast.LENGTH_SHORT).show();
        }
    }

    //on image click adjust it
    public void imageView(View view) {
        if (imageFit){
            imageFit=false;
            image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            image.setAdjustViewBounds(true);
        }else{
            imageFit=true;
            image.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    }
}
