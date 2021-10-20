package thezaxis.biometricattendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddStudentActivity extends AppCompatActivity {
    EditText idText, nameText, addressText;
    Button saveButton, getFingerprintButton;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    byte[] fingerData;

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
        FirebaseApp.initializeApp(this);
        prepareLayoutElements();
    }
    private void prepareLayoutElements() {
        idText = findViewById(R.id.edit_id);
        nameText = findViewById(R.id.edit_name);
        addressText = findViewById(R.id.edit_address);
        saveButton = findViewById(R.id.submit_button);
        getFingerprintButton = findViewById(R.id.fingerprint_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent(nameText.getText().toString(), idText.getText().toString(), addressText.getText().toString());
            }
        });
        getFingerprintButton.setOnClickListener(view -> startActivityForResult(new Intent(AddStudentActivity.this, ScanActivity.class), 1));
    }

    private void addStudent(String name, String id, String address) {
        if (name.isEmpty() || id.isEmpty() || address.isEmpty() || fingerData == null || fingerData.length == 0){
            Toast.makeText(AddStudentActivity.this, "Add all Fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Student student = new Student(id, name, address);

        String uid = "user4";

        databaseReference = FirebaseDatabase.getInstance().getReference("students");

        // databaseReference.setValue(student);
        /* databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.setValue(student);

                // after adding this data we are showing toast message.
                Toast.makeText(AddStudentActivity.this, "data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(AddStudentActivity.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });*/


        databaseReference.setValue(student).addOnSuccessListener(task -> {
            Toast.makeText(AddStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(AddStudentActivity.this, "Failed", Toast.LENGTH_SHORT).show();
        });

        storageReference = FirebaseStorage.getInstance().getReference("students/" +student.id);
        storageReference.putBytes(fingerData).addOnSuccessListener(taskSnapshot -> {
            Toast.makeText(AddStudentActivity.this, "Save Image Success", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(AddStudentActivity.this, "Error: "+e, Toast.LENGTH_SHORT).show();
        });
    }
}