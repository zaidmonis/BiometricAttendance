package thezaxis.biometricattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    EditText idText, nameText, addressText, branchText;
    Button saveButton, getFingerprintButton;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    byte[] fingerData;
    private String currentUser;
    boolean isNew = true;
    List<Student> allStudents;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
            fingerData = data != null ? data.getByteArrayExtra("fingerData") : null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        currentUser = getIntent().getStringExtra("currentUser");
        FirebaseApp.initializeApp(this);
        getAllStudents();
        prepareLayoutElements();
    }
    private void prepareLayoutElements() {
        idText = findViewById(R.id.edit_id);
        nameText = findViewById(R.id.edit_name);
        addressText = findViewById(R.id.edit_address);
        branchText = findViewById(R.id.edit_branch);
        saveButton = findViewById(R.id.submit_button);
        getFingerprintButton = findViewById(R.id.fingerprint_button);

        saveButton.setOnClickListener(view -> {
            isNew = true;
            checkIfNew(idText.getText().toString());
            addStudent(nameText.getText().toString(), idText.getText().toString(),
                    addressText.getText().toString(), branchText.getText().toString());
        });
        getFingerprintButton.setOnClickListener
                (view -> startActivityForResult(new Intent
                        (AddStudentActivity.this, ScanActivity.class), 1));
    }

    private void addStudent(String name, String id, String address, String branch) {
        if (name.isEmpty() || id.isEmpty() || address.isEmpty() || fingerData == null || fingerData.length == 0){
            Toast.makeText(AddStudentActivity.this, "Add all Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Student student = new Student(id, name, address, branch, currentUser);

        if (!isNew){
            Toast.makeText(AddStudentActivity.this, "Student ID already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("students");
        databaseReference.child(id).setValue(student).addOnSuccessListener(task -> {
            Toast.makeText(AddStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(AddStudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        });

        storageReference = FirebaseStorage.getInstance().getReference("students/" +student.id);
        storageReference.putBytes(fingerData).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(AddStudentActivity.this, "Save Image Success", Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(AddStudentActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
        });
    }

    private void getAllStudents() {
        databaseReference = FirebaseDatabase.getInstance().getReference("students");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Student> students;
                GenericTypeIndicator<HashMap<String, Student>> t = new GenericTypeIndicator<HashMap<String, Student>>() {
                };
                HashMap<String, Student> hashMap = snapshot.getValue(t);
                if (hashMap != null) {
                    allStudents = new ArrayList<>(hashMap.values());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void checkIfNew(String id) {
        for (Student student : allStudents) {
            if (student.id.equals(id)){
                isNew = false;
            }
        }
    }
}