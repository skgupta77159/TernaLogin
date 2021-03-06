package com.example.ternalogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.ternalogin.adapter.StudentMonthAdapter;
import com.example.ternalogin.model.monModel;
import com.example.ternalogin.studentside.studentProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudMonthAttendance extends AppCompatActivity {
//this is student attendanece acitvity
    RecyclerView recyclerView;
    Toolbar toolbar;
    Button button_def;
    FirebaseDatabase database;
    DatabaseReference databaseRef, newRef;
    List<String> monthList = new ArrayList<>();
    List<monModel> studList = new ArrayList<>();
    List<String> idsList = new ArrayList<>();
    public static List<monModel> defaulterList = new ArrayList<>();
    int[] preArr = new int[100];
    int[] totArr = new int[100];
    String Subject,Year;
    int fMonth, tMonth,i;
    int j,k;
    float Percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_month_attendance);

        button_def = findViewById(R.id.button_def);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Subject = getIntent().getStringExtra("subject");
        Year = getIntent().getStringExtra("years");
        fMonth = Integer.parseInt(getIntent().getStringExtra("fMonth"));
        tMonth = Integer.parseInt(getIntent().getStringExtra("tMonth"));
        monthList.add(0,"Select Month");
        monthList.add(1,"January");
        monthList.add(2,"February");
        monthList.add(3,"March");
        monthList.add(4,"April");
        monthList.add(5,"May");
        monthList.add(6,"June");
        monthList.add(7,"July");
        monthList.add(8,"August");
        monthList.add(9,"September");
        monthList.add(10,"October");
        monthList.add(11,"November");
        monthList.add(12,"December");
        getSupportActionBar().setTitle(monthList.get(fMonth)+ " to "+ monthList.get(tMonth));
        studList.clear();
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("year");
        newRef = database.getReference("year");

        newRef.child(Year).child(Subject);
        defaulterList.clear();

        for(i=fMonth;i<=tMonth;i++){
            databaseRef.child(Year).child(Subject).child(monthList.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        j=0;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                          //  String id = (String) dataSnapshot.child("id").getValue();
                          //  String name = (String) dataSnapshot.child("name").getValue();
                          //  String roll = (String) dataSnapshot.child("roll").getValue();
                            long present = dataSnapshot.child("present").getChildrenCount();
                            long total = dataSnapshot.child("total").getChildrenCount();
                            preArr[j]= preArr[j] + (int)present;
                            totArr[j]= totArr[j] + (int)total;
                            j++;
                           // monModel monmodel = new monModel(id, name, roll, present, total);
                          //  studList.add(monmodel);
                        }
                      /*  StudentMonthAdapter studentMonthAdapter = new StudentMonthAdapter(studList, preArr, totArr);
                        recyclerView.setLayoutManager(new LinearLayoutManager(StudMonthAttendance.this));
                        studentMonthAdapter.notifyDataSetChanged();
                        recyclerView.setAdapter(studentMonthAdapter);  */
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }





        databaseRef.child(Year).child(Subject).child(monthList.get(fMonth)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     j=0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String id = (String) dataSnapshot.child("id").getValue();
                        String name = (String) dataSnapshot.child("name").getValue();
                        String roll = (String) dataSnapshot.child("roll").getValue();
                        int Present = preArr[j];
                        int Total = totArr[j];
                        Percent = (((float)Present/(float)Total)*100);
                        Percent = (float) (Math.round(Percent*100.0)/100.0);
                        // long present = dataSnapshot.child("present").getChildrenCount();
                        // long total = dataSnapshot.child("total").getChildrenCount();
                        // preArr[j]= preArr[j] + (int)present;
                        // totArr[j]= totArr[j] + (int)total;
                        j++;
                        monModel monmodel = new monModel(id, name, roll, Present, Total,Percent);
                        if(Percent<75){
                            defaulterList.add(monmodel);
                        }
                        studList.add(monmodel);
                    }
                    StudentMonthAdapter studentMonthAdapter = new StudentMonthAdapter(studList, preArr, totArr);
                    recyclerView.setLayoutManager(new LinearLayoutManager(StudMonthAttendance.this));
                    studentMonthAdapter.notifyDataSetChanged();
                    recyclerView.setAdapter(studentMonthAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        button_def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent MainIntent = new Intent(StudMonthAttendance.this, showDefaulterStudent.class);
                MainIntent.putExtra("subject", Subject);
                MainIntent.putExtra("year", Year);
                MainIntent.putExtra("monthRange", monthList.get(fMonth)+ " to "+ monthList.get(tMonth));
                startActivity(MainIntent);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}