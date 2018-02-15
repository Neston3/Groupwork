package com.marica.m_note;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.marica.m_note.Adapter.SlideAdapter;
import com.marica.m_note.SharedPreference.MyPreferenceManager;


public class MainActivity extends AppCompatActivity {

    Button btn;
    private ViewPager viewPager;
    private SlideAdapter myadapter;
    LinearLayout sliderdot;
    private int dotcount;
    private ImageView[] dot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=findViewById(R.id.viewpager);
        myadapter=new SlideAdapter(MainActivity.this);
        viewPager.setAdapter(myadapter);

        //slider dots
        sliderdot=findViewById(R.id.SliderDots);
        //get dot to count list from the adapter i.e page viewer adapter
        dotcount=myadapter.getCount();
        dot=new ImageView[dotcount];
        //looping through the dot //setting them in active
        for (int i=0;i<dotcount;i++){
            dot[i]=new ImageView(getApplicationContext());
            dot[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                    ,R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.
                    LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8,0,8,0);

            sliderdot.addView(dot[i],params);
        }

        dot[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.active_dot));

        //page change listener for any the dots to be active when page is changed
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i=0;i<dotcount;i++){
                    dot[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.non_active_dot));
                }
                dot[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn=findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplication(),SignUpOption.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right,R.anim.to_left);
            }
        });


        //check if user is logged in using sharedPreference
        if(MyPreferenceManager.getInstance(getApplication()).isLoggedIn()){
            Intent intent=new Intent(getApplicationContext(),Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right,R.anim.to_left);
            Toast.makeText(getApplicationContext(),"logged in",Toast.LENGTH_SHORT).show();
        }
        else {
            //do nothing id the user is not logged in
        }


    }

    public void login(View view) {
        Intent intent =new Intent(getApplication(),Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from_right,R.anim.to_left);
    }
}
