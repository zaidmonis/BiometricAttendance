package thezaxis.biometricattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button startButton, viewStudentButton, addStudentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareLayoutElements();
    }

    private void prepareLayoutElements() {
        startButton = (Button) findViewById(R.id.start_button);
        viewStudentButton = (Button) findViewById(R.id.view_student_button);
        addStudentButton = (Button) findViewById(R.id.add_student_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ScanActivity.class));
            }
        });
        viewStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ViewStudentActivity.class));
            }
        });
        addStudentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddStudentActivity.class));
            }
        });
    }
}