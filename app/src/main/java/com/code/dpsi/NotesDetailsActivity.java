package com.code.dpsi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NotesDetailsActivity extends AppCompatActivity {
    EditText titleEditText,contentText;
    ImageButton saveNoteBtn;
    TextView page_title;
    String title,content,docId;
    boolean isEditMode=false;
    TextView deleteNoteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_details);
        titleEditText=findViewById(R.id.notes_title_text);
        contentText=findViewById(R.id.content_title_text);
        saveNoteBtn=findViewById(R.id.saveNoteBtn);
        page_title=findViewById(R.id.page_title);
        deleteNoteBtn=findViewById(R.id.delete_note_btn);
        //receive data
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docId=getIntent().getStringExtra("docId");
        if(docId!=null && !docId.isEmpty()){
            isEditMode=true;
        };

        titleEditText.setText(title);
        contentText.setText(content);
        if (isEditMode){
            page_title.setText("Edit your name ");
            deleteNoteBtn.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v)->saveNote());
        deleteNoteBtn.setOnClickListener((v)-> deleteNoteFromFirebase());


    }
    void saveNote(){
        String noteTitle= titleEditText.getText().toString();
        String noteContent= contentText.getText().toString();
        if(noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is needed");
            return;
        }
      Notes notes= new Notes();
        notes.setTitle(noteTitle);
        notes.setContent(noteContent);
        notes.setTimestamp(Timestamp.now());
      saveNoteToFirebase(notes);
    }
    void saveNoteToFirebase(Notes notes){
        DocumentReference documentReference ;
        if(isEditMode){
            // update note
            documentReference=Utility.getCollectionReferenceForNOtes().document(docId);
        }else {
            // create a note.
            documentReference=Utility.getCollectionReferenceForNOtes().document();
        }

        documentReference.set(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //notes is added
                    Utility.showToast(NotesDetailsActivity.this,"Note added successfully");
                    finish();
                }else {
                    //not is not added
                    Utility.showToast(NotesDetailsActivity.this,"Fail while adding notes");
                }
            }
        });
    }
    void deleteNoteFromFirebase(){
        DocumentReference documentReference ;

            documentReference=Utility.getCollectionReferenceForNOtes().document(docId);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //notes is deleted
                    Utility.showToast(NotesDetailsActivity.this,"Note deleted successfully");
                    finish();
                }else {
                    //not is not added
                    Utility.showToast(NotesDetailsActivity.this,"Fail while deleting notes");
                }
            }
        });
    }
}