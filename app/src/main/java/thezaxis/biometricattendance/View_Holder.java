package thezaxis.biometricattendance;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class View_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView viewId, viewName, viewBranch;
    View_Holder(View itemView) {
        super(itemView);
        cv = itemView.findViewById(R.id.student_card);
        viewId = itemView.findViewById(R.id.view_id);
        viewName = itemView.findViewById(R.id.view_name);
        viewBranch = itemView.findViewById(R.id.view_branch);
    }
}
