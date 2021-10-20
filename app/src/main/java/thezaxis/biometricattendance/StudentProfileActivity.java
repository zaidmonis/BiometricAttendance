package thezaxis.biometricattendance;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class StudentProfileActivity extends AppCompatActivity {

    private TextView nameView, branchView, idView;
    ImageView fingerprintImage;
    private String name, id, branch;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        name = getIntent().getStringExtra("name");
        branch = getIntent().getStringExtra("branch");
        id = getIntent().getStringExtra("id");
        storageReference = FirebaseStorage.getInstance().getReference("students/" + id);
        getFingerPrintImage();
        setupLayout();
    }

    private void getFingerPrintImage() {
        File localFile = null;
        try {
            localFile = File.createTempFile("thumb", "bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File finalLocalFile = localFile;
        storageReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            Bitmap bitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());

            fingerprintImage.setImageBitmap(invert(bitmap));
        }).addOnFailureListener(e -> Toast.makeText(StudentProfileActivity.this,
                "Unable to fetch image!",
                Toast.LENGTH_SHORT).show());
    }

    private void setupLayout() {
        nameView = findViewById(R.id.name_view);
        idView = findViewById(R.id.id_view);
        branchView = findViewById(R.id.branch_view);
        fingerprintImage = findViewById(R.id.thumb_imageview);
        nameView.setText(name);
        idView.setText(id);
        branchView.setText(branch);
    }

    public Bitmap invert(Bitmap src)
    {
        int height = src.getHeight();
        int width = src.getWidth();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();

        ColorMatrix matrixGrayscale = new ColorMatrix();
        matrixGrayscale.setSaturation(0);

        ColorMatrix matrixInvert = new ColorMatrix();
        matrixInvert.set(new float[]
                {
                        -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
                        0.0f, 0.0f, 0.0f, 1.0f, 0.0f
                });
        matrixInvert.preConcat(matrixGrayscale);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrixInvert);
        paint.setColorFilter(filter);

        canvas.drawBitmap(src, 0, 0, paint);
        return bitmap;
    }
}