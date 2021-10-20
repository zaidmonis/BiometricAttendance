package thezaxis.biometricattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewStudentActivity extends AppCompatActivity {

    private String username;
    private DatabaseReference databaseReference;
    private List<Student> students, studentsOfCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);
        username = getIntent().getStringExtra("currentUser");
        databaseReference = FirebaseDatabase.getInstance().getReference("students");
        loadData();
    }

    private void loadData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GenericTypeIndicator<HashMap<String, Student>> t = new GenericTypeIndicator<HashMap<String, Student>>() {
                };
                HashMap<String, Student> hashMap = snapshot.getValue(t);
                if (hashMap != null) {
                    students = new ArrayList<>(hashMap.values());
                }
                getStudentsOfCurrentUser(students);

                RecyclerView recyclerView = findViewById(R.id.recyclerview_student);
                Recycler_View_Adapter adapter = new Recycler_View_Adapter(studentsOfCurrentUser, getApplication());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ViewStudentActivity.this));
                recyclerView.addOnItemTouchListener( new CustomRVItemTouchListener(ViewStudentActivity.this, recyclerView, new RecyclerViewItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent intent = new Intent(ViewStudentActivity.this, StudentProfileActivity.class);
                        intent.putExtra("id", studentsOfCurrentUser.get(position).id);
                        intent.putExtra("branch", studentsOfCurrentUser.get(position).branch);
                        intent.putExtra("name", studentsOfCurrentUser.get(position).name);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewStudentActivity.this, "Unable to access data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getStudentsOfCurrentUser(List<Student> studentList) {
        studentsOfCurrentUser = new ArrayList<>();
        for (Student student: studentList) {
            if (student.username.equals(username)){
                studentsOfCurrentUser.add(student);
            }
        }
    }
}
