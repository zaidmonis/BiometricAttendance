package thezaxis.biometricattendance;

import android.view.View;

public interface RecyclerViewItemClickListener {

    public void onClick(View view, int position);
    public void onLongClick(View view, int position);
}