package com.dst.ayyapatelugu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Activity.ViewAllTemplesActivity;
import com.dst.ayyapatelugu.Activity.ViewTempleListDetailsActivity;
import com.dst.ayyapatelugu.HomeActivity;
import com.dst.ayyapatelugu.Model.TemplesListModel;
import com.dst.ayyapatelugu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewAllTempleListAdapter extends RecyclerView.Adapter<ViewAllTempleListAdapter.MyviewHolder> {
    Context mContext;
    List<TemplesListModel> listModels;

    public ViewAllTempleListAdapter(Context context, List<TemplesListModel> templesListModels) {

        this.mContext=context;
        this.listModels=templesListModels;

    }

    @NonNull
    @Override
    public ViewAllTempleListAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.viewall_temple_list_adapter, parent, false);
        return new ViewAllTempleListAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAllTempleListAdapter.MyviewHolder holder, int position) {

        TemplesListModel templesListModel = listModels.get(position);
        String profilepic = templesListModel.getImage();
        String imageUrl = "https://www.ayyappatelugu.com/assets/temple_images/" + profilepic;
        String name = templesListModel.getTempleName();
        String tName=templesListModel.getTempleNameTelugu();
        String open=templesListModel.getOpeningTime();
        String close=templesListModel.getClosingTime();
        String location= templesListModel.getLocation();


        holder.tvTempleName.setText(name);

        // holder.tvTempleName.setText(name);
        Picasso.get().load(imageUrl).into(holder.imgTemple);

        holder.layoutAllTemples.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ViewTempleListDetailsActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("TName",tName);
                intent.putExtra("Open",open);
                intent.putExtra("Close",close);
                intent.putExtra("Location",location);
                intent.putExtra("imagePath", imageUrl);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (listModels != null) {
            return listModels.size();
        }
        return 0;

    }


    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvTempleName;
        ImageView imgTemple;
        LinearLayout layoutAllTemples;
        //Button butViewAll;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            tvTempleName = itemView.findViewById(R.id.txt_name);
            imgTemple = itemView.findViewById(R.id.image_temple);
            layoutAllTemples=itemView.findViewById(R.id.layout_seva_all);
           // butViewAll = itemView.findViewById(R.id.but_mostpopular);


        }
    }
}

