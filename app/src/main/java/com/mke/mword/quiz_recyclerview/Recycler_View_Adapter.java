package com.mke.mword.quiz_recyclerview;

import static com.mke.mword.Database.Constants.MESSAGE_EXAM_VIEW_SET_ANSWER;
import static com.mke.mword.Utils.Utils.SENDMESSAGEEXAMSCREENWINDOW;
import static com.mke.mword.quizScreen.getAnswerNo;

import android.content.Context;
import android.graphics.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.*;

import com.mke.mword.*;

import java.util.Collections;
import java.util.List;


public class Recycler_View_Adapter extends RecyclerView.Adapter<View_Holder> {

    List<Data> list = Collections.emptyList();
    Context context;

    public Recycler_View_Adapter(List<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }
    int row_index=-1;
    boolean bOnClick =false;
    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.title.setText(list.get(position).opt);
        holder.description.setText(list.get(position).description);
//        holder.imageView.setImageResource(list.get(position).imageId);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bOnClick =true;
                int nSelect= getAnswerNo();
                if (nSelect !=-1 && position==nSelect) {
                    holder.itemView.setBackgroundColor(Color.parseColor("#567845"));
                    row_index = -1;
                }else{
                    holder.itemView.setBackgroundColor(Color.RED);
                    row_index = nSelect;
                }
                SENDMESSAGEEXAMSCREENWINDOW(MESSAGE_EXAM_VIEW_SET_ANSWER, position);
                notifyDataSetChanged();
            }
        });
        if (bOnClick){
            holder.itemView.setClickable(false);
            holder.itemView.setFocusable(false);
        }
        if (row_index != -1 && row_index == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#567845"));
        }
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView
    public void insert(int position, Data data) {
        list.add(position, data);
        notifyItemInserted(position);
    }
    // Remove a RecyclerView item containing the Data object
    public void remove(Data data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

}
