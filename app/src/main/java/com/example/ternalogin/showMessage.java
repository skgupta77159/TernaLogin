package com.example.ternalogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.ternalogin.adapter.ShowMessageAdapter;
import com.example.ternalogin.model.msgModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class showMessage extends AppCompatActivity {

    RecyclerView recyclerView;
    FirebaseDatabase database;
    DatabaseReference msgRef;
    Toolbar toolbar;
    String studentId;
    List<msgModel> msgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);

        studentId = getIntent().getStringExtra("id");
        recyclerView = findViewById(R.id.msg_recycler_view);
        toolbar = findViewById(R.id.at_mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Messages");

        database = FirebaseDatabase.getInstance();
        msgRef = FirebaseDatabase.getInstance().getReference("Message");
        msgList.clear();

        msgRef.child(studentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    msgModel msgmodel = dataSnapshot.getValue(msgModel.class);
                    msgList.add(msgmodel);
                }
                ShowMessageAdapter showMessageAdapter = new ShowMessageAdapter(msgList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(showMessage.this);
                linearLayoutManager.setReverseLayout(true);
                linearLayoutManager.setStackFromEnd(true);
                recyclerView.setLayoutManager(linearLayoutManager);
                showMessageAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(showMessageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}