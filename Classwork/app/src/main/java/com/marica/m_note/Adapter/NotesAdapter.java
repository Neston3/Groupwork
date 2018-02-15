package com.marica.m_note.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marica.m_note.AppManager.AppManager;
import com.marica.m_note.R;
import com.marica.m_note.ViewNote;
import com.marica.m_note.pojoclass.Note;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.marica.m_note.requestqueue.MySingleton.context;

/**
 * Created by Neston on 17/12/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {
    private List<Note> notesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, timeline, preview;
        public RelativeLayout relativeLayoutnote,viewbackground;

        //find the views
        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            preview = view.findViewById(R.id.preview);
            timeline = view.findViewById(R.id.timeline);
            relativeLayoutnote=view.findViewById(R.id.foreground);
            viewbackground=view.findViewById(R.id.view_background);
        }
    }


    public NotesAdapter(List<Note> notesList) {
        this.notesList = notesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //to be checked with my pojo class
        final Note note = notesList.get(position);
        holder.title.setText(note.getNote_title());
        holder.preview.setText(note.getNote_detail());
        holder.timeline.setText(note.getCreated_at());

        //onclick listener for recyclerview
        holder.relativeLayoutnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title=note.getNote_title();
                String detail=note.getNote_detail();

                //passing extras to another activity
                Intent intent=new Intent(holder.relativeLayoutnote.getContext(),ViewNote.class);
                intent.putExtra("titlekey",title);
                intent.putExtra("detailkey",detail);
                holder.relativeLayoutnote.getContext().startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public void removeItem(int position) {
        notesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }
    public void restoreItem(Note note, int position) {
        notesList.add(position, note);
        // notify item added by position
        notifyItemInserted(position);
    }

}
