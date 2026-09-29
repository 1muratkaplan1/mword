package com.mke.mword.quiz_recyclerview;

import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.*;
import androidx.recyclerview.widget.*;

import com.mke.mword.*;


//The adapters View Holder
public class View_Holder extends RecyclerView.ViewHolder {

    CardView cv;
    TextView title;
    TextView description;
//    ImageView imageView;

    View_Holder(View itemView) {
        super(itemView);
        cv = (CardView) itemView.findViewById(R.id.cardView);
        title = (TextView) itemView.findViewById(R.id.quiz_view_opt);
        description = (TextView) itemView.findViewById(R.id.quiz_view_opt_sentencences);
//        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

}
