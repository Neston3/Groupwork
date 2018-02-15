package com.marica.m_note;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
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
import com.marica.m_note.recycler.MyRecyclerScroll;
import com.marica.m_note.recycler.RecyclerHelper;
import com.marica.m_note.requestqueue.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,RecyclerHelper.RecyclerItemTouchHelperListener {

    private static final String SERVER_URL = "https://maricajr.000webhostapp.com/server_con/list.php";
    private static final String SERVER_URL_DELETE="https://maricajr.000webhostapp.com/server_con/delete.php";
    private List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NotesAdapter mAdapter;
    private TextView username,useremail;
    public CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Intent intent=new Intent(Home.this,AddNote.class);
                startActivity(intent);
                overridePendingTransition(R.anim.from_right,R.anim.to_left);

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        coordinatorLayout=findViewById(R.id.main_note);

        //call the set name and email on the nav bar
        navUserDetail(header);

        recyclerView = findViewById(R.id.recycler_view);

        mAdapter = new NotesAdapter(noteList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        //adding the scroll effect  as the floating button hides and shows
        recyclerView.setOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab
                        .getLayoutParams();
                int fabMargin = layoutParams.bottomMargin;
                fab.animate().translationY(fab.getHeight() + fabMargin).setInterpolator(
                        new AccelerateInterpolator(2)).start();
            }
        });

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerHelper(0,
                ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        //adding touch helper for swiping
       ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.
                    ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        //call the note list
        NotesView();
    }

    private void navUserDetail(View v) {
        //find elements by id
        username=v.findViewById(R.id.user_name);
        useremail=v.findViewById(R.id.avatar_email);

        //set there name
        User user= MyPreferenceManager.getInstance(getApplicationContext()).getUser();

        username.setText(user.getName());
        useremail.setText(user.getEmail());
    }


    //data saved by user to be viewed in alist view
    private void NotesView() {

       StringRequest stringRequest=new StringRequest(Request.Method.POST, SERVER_URL, new Response.Listener<String>() {
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
                    Toast.makeText(Home.this,"Server not found...try again",Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(Home.this,"Check your internet connection..",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(Home.this,"Oops...",Toast.LENGTH_SHORT).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(Home.this,"Cannot connect to the internet",Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(Home.this,"Connection Timed Out",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id==R.id.action_logout){
            logout();
        }else if (id==R.id.action_refresh){
            refresh();
        }

        return super.onOptionsItemSelected(item);
    }

    //refresh the adapter not working
    private void refresh() {

        mAdapter.notifyDataSetChanged();
    }

    private void logout() {
        MyPreferenceManager.getInstance(getApplicationContext()).logout();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_notebook) {

        } else if (id == R.id.nav_trash) {
            Intent intent=new Intent(getApplicationContext(),Trash.class);
            startActivity(intent);
            overridePendingTransition(R.anim.from_right,R.anim.to_left);

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof NotesAdapter.MyViewHolder) {
            // get the removed item name to display it in snack bar
            final String titlename = noteList.get(viewHolder.getAdapterPosition()).getNote_title();

            // backup of removed item for undo purpose
            final Note deletedItem = noteList.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, titlename + " moved to Trash..!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    mAdapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

            //checking if the snackbar is shown or not
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    if (event==Snackbar.Callback.DISMISS_EVENT_TIMEOUT){
                        //call the server to delete item

                        DeleteNote(titlename);

                    }
                }

                @Override
                public void onShown(Snackbar transientBottomBar) {
                    super.onShown(transientBottomBar);
                }
            });

        }
    }

    //delete note
    private void DeleteNote(final String titlename) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, SERVER_URL_DELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error response",Toast.LENGTH_SHORT)
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
               User user= MyPreferenceManager.getInstance(getApplicationContext()).getUser();
               int user_id=user.getId();

               Map<String, String> params=new HashMap<>();
               params.put("title",titlename);
               params.put("id",String.valueOf(user_id));

               return params;
            }
        };


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        stringRequest.setShouldCache(true);


    }

}
