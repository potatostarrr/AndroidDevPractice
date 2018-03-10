package com.example.tengzhongwei.simplenote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;

public class NoteDetailActivity extends AppCompatActivity {
    EditText editNoteTextView;
    Button saveButton;
    int index;

    public void onSaveButtonClick(View view){
        if(index>-1){
            MainActivity.noteList.set(index, editNoteTextView.getText().toString());
        } else {
            MainActivity.noteList.add(editNoteTextView.getText().toString());
        }
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.tengzhongwei.simplenote", Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet("noteList", new HashSet<String>(MainActivity.noteList)).apply();
        MainActivity.arrayAdapter.notifyDataSetChanged();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        saveButton = findViewById(R.id.saveButton);
        editNoteTextView = findViewById(R.id.editNoteTextView);

        Intent intent = getIntent();
        index = intent.getIntExtra("index", -1);
        String note = "";

        if (index>-1){
            note = MainActivity.noteList.get(index);
        }

        editNoteTextView.setText(note);




    }
}
