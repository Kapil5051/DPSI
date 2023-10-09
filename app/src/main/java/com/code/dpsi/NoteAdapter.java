package com.code.dpsi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter <Notes,NoteAdapter.NoteViewHolder> {
    Context context;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Notes> options,Context context) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Notes notes) {
        holder.titleText.setText(notes.title);
        holder.contentText.setText(notes.content);
        holder.timestamp.setText(Utility.timestampToString(notes.timestamp));
        holder.itemView.setOnClickListener((v)->{
            Intent intent=new Intent(context,NotesDetailsActivity.class);
            intent.putExtra("title", notes.title);
            intent.putExtra("content", notes.content);
            String docId=this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item,parent,false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView titleText,contentText,timestamp;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText=itemView.findViewById(R.id.notes_title_text);
            contentText=itemView.findViewById(R.id.content_title_text);
            timestamp=itemView.findViewById(R.id.timestamp_text);
        }
    }
}
