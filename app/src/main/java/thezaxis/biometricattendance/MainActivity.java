package thezaxis.biometricattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button startButton, viewStudentButton, addStudentButton;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = getIntent().getStringExtra("currentUser");
        prepareLayoutElements();
    }

    private void prepareLayoutElements() {
        startButton = findViewById(R.id.start_button);
        viewStudentButton = findViewById(R.id.view_student_button);
        addStudentButton = findViewById(R.id.add_student_button);

        startButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ScanActivity.class)));

        viewStudentButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewStudentActivity.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });

        addStudentButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddStudentActivity.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });
    }
}