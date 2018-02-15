package com.marica.m_note;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marica.m_note.pojoclass.Note;

import static com.marica.m_note.requestqueue.MySingleton.context;

public class ViewNote extends AppCompatActivity {
    EditText note_title,note_detail;
    String notettl,notedtl;
    ImageView btndone,btnundo,btnredo,btnattach,btncamera,btnmore;
    private static final int CAMERA_REQUEST=1888;
    ImageView image;
    boolean imageFit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        note_title=findViewById(R.id.title);
        note_detail=findViewById(R.id.detail);
        btndone=findViewById(R.id.imageButton5);
        btnundo=findViewById(R.id.imageButton4);
        btnredo=findViewById(R.id.imageButton3);
        btnattach=findViewById(R.id.imageButton2);
        btncamera=findViewById(R.id.btn_camera);
        btnmore=findViewById(R.id.imageButton);
        image=findViewById(R.id.imageview);

        //getting the extras from activity
        notettl=getIntent().getStringExtra("titlekey");
        notedtl=getIntent().getStringExtra("detailkey");
        note_title.setText(notettl);
        note_detail.setText(notedtl);

        //dealing with edit button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note_title.setFocusableInTouchMode(true);
                note_detail.setFocusableInTouchMode(true);
                btnattach.setVisibility(View.VISIBLE);
                btncamera.setVisibility(View.VISIBLE);
                btndone.setVisibility(View.VISIBLE);
                btnmore.setVisibility(View.VISIBLE);
                btnredo.setVisibility(View.VISIBLE);
                btnundo.setVisibility(View.VISIBLE);
            }
        });

    }

    public void done_click(View view) {

    }

    public void attach_click(View view) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //type of file you want to access
        intent.setType("image/*");
        startActivity(intent);
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

    //open camera
    public void camera_click(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,CAMERA_REQUEST);
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
