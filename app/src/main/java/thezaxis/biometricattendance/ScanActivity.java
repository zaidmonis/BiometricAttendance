package thezaxis.biometricattendance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mantra.mfs100.FingerData;
import com.mantra.mfs100.MFS100;
import com.mantra.mfs100.MFS100Event;

public class ScanActivity extends AppCompatActivity implements MFS100Event {
    Button captureButton;
    ImageView imgFinger;
    FingerData fingerData;
    MFS100 mfs100;
    int mfsVer = 41, quality;
    String key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        captureButton = findViewById(R.id.capture_button);
        imgFinger = (ImageView) findViewById(R.id.finger_image);
        mfs100 = new MFS100(this);
        mfs100.SetApplicationContext(ScanActivity.this);

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InitScanner();
                StartSyncCapture();
            }
        });
    }

    public void StartSyncCapture() {
        new Thread(() -> {
            try {
                FingerData fingerData = new FingerData();
                int ret = mfs100.AutoCapture(fingerData, 10000, false);
                if (ret != 0) {
                    SetTextonuiThread(mfs100.GetErrorMsg(ret));
                } else {
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(fingerData.FingerImage(), 0, fingerData.FingerImage().length);
                    final Bitmap invertedBitmap = invert(bitmap);
                    imgFinger.post(() -> {
                        imgFinger.setImageBitmap(invertedBitmap);
                        imgFinger.refreshDrawableState();
                    });
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("fingerData", fingerData.FingerImage());
                    setResult(1, returnIntent);


                    SetTextonuiThread("Quality: " + fingerData.Quality() + " NFIQ: " + fingerData.Nfiq());
                    finish();
                }
            } catch (Exception ex) {
                SetTextonuiThread("Error: " +ex);
            }
        }).start();
    }

    public void InitScanner() {
        try {
            int ret = mfs100.Init();
            if (ret != 0) {
                SetTextonuiThread(mfs100.GetErrorMsg(ret));
            } else {
                // SetTextonuiThread("Init success");
                String info = "Serial: " + mfs100.GetDeviceInfo().SerialNo() + " Make: " + mfs100.GetDeviceInfo().Make() + " Model: " + mfs100.GetDeviceInfo().Model();
                // SetTextonuiThread(info);
            }
        } catch (Exception ex) {
            SetTextonuiThread("Init failed ,unhandled exception");
        }
    }

    private void SetTextonuiThread(final String text) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(ScanActivity.this, text, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public void OnDeviceAttached(int i, int i1, boolean b) {
        SetTextonuiThread("Device Attached");
    }

    @Override
    public void OnDeviceDetached() {

    }

    @Override
    public void OnHostCheckFailed(String s) {

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