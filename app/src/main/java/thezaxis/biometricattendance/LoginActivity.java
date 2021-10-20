package thezaxis.biometricattendance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private  DatabaseReference databaseReference;
    private EditText editUsername, editPassword;
    private TextView errorText;
    private User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        initializeLayout();
    }

    private void initializeLayout() {
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        Button loginButton = findViewById(R.id.login_button);
        errorText = findViewById(R.id.error_text);

        loginButton.setOnClickListener(view -> {
            if (editUsername.getText().toString().isEmpty() || editPassword.getText().toString().isEmpty()){
                if (editUsername.getText().toString().isEmpty()){
                    editUsername.setError("Enter username");
                }
                if (editPassword.getText().toString().isEmpty()){
                    editPassword.setError("Enter password");
                }
            }
            else{
                loginUser(new User(editUsername.getText().toString(), editPassword.getText().toString()));
            }
        });
    }

    private void loginUser(User user) {
        databaseReference.child(user.username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUser = snapshot.getValue(User.class);
                if (currentUser == null){
                    errorText.setText("User doesn't exists!");
                }
                else if (currentUser.equals(user)){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("currentUser", currentUser.username);
                    startActivity(intent);
                    finish();
                }
                else {
                    errorText.setText("Incorrect password!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error; " +error, Toast.LENGTH_LONG).show();
            }
        });
    }
}