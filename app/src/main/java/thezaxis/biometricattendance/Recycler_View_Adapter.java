package thezaxis.biometricattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

    List<Student> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter(List<Student> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.viewId.setText(list.get(position).id);
        holder.viewName.setText(list.get(position).name);
        holder.viewBranch.setText(list.get(position).branch);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Student student) {
        list.add(position, student);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Student object
    public void remove(Student student) {
        int position = list.indexOf(student);
        list.remove(position);
        notifyItemRemoved(position);
    }

}