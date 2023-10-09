package com.code.dpsi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addNoteBtn=findViewById(R.id.addbtn);
        recyclerView=findViewById(R.id.recycler_view);

        addNoteBtn.setOnClickListener((v)->startActivity(new Intent(MainActivity.this,NotesDetailsActivity.class)));
        menuBtn = findViewById(R.id.menuBtn);
        setupRecyclerView();
    }
    void showMenu(){
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menuBtn);
        popupMenu.getMenu().add("Pdfs");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Pdf"){
                   startActivity(new Intent(MainActivity.this,PdfsMainActivity.class));
                   finish();
                   return true;
                }
                return false;
            }
        });
    }
    void setupRecyclerView(){
        Query query=Utility.getCollectionReferenceForNOtes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notes> options=new FirestoreRecyclerOptions.Builder<Notes>().setQuery(query,Notes.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter= new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

}