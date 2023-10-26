package com.example.ayyapatelugu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ayyapatelugu.Activity.AyyapaBooksListActivity;
import com.example.ayyapatelugu.Activity.AyyapaKarmaDetailsActivity;
import com.example.ayyapatelugu.Activity.AyyappaKaryamListActivity;
import com.example.ayyapatelugu.Model.BooksModelResult;
import com.example.ayyapatelugu.Model.KaryakaramamListModel;
import com.example.ayyapatelugu.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class AyyapakaryamListAdappter extends RecyclerView.Adapter<AyyapakaryamListAdappter.MyviewHolder> {
    Context mContext;
    List<KaryakaramamListModel> listModels;



    public AyyapakaryamListAdappter(AyyappaKaryamListActivity ayyappaKaryamListActivity, List<KaryakaramamListModel> list) {
        this.mContext=ayyappaKaryamListActivity;
        this.listModels=list;

    }


    @Override
    public AyyapakaryamListAdappter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ayyapa_list_adapter,parent,false);
        return new AyyapakaryamListAdappter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(AyyapakaryamListAdappter.MyviewHolder holder, int position) {
        KaryakaramamListModel modal = listModels.get(position);
        String imgUrl="https://www.ayyappatelugu.com/assets/activity/"+modal.getImage();
        String name=modal.getTitle();
        String discription=modal.getDescription();
        holder.tvtitle.setText(name);
        holder.tvdetails.setText(modal.getSmallDescription());
        Picasso.get().load(imgUrl).into(holder.image);

        holder.butMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AyyapaKarmaDetailsActivity.class);
                intent.putExtra("ImagePath",imgUrl);
                intent.putExtra("Name",name);
                intent.putExtra("Discription",discription);
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        if(listModels != null){
            return listModels.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle,tvdetails,butMoreDetails;
        ImageView image;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvtitle = (TextView)itemView.findViewById(R.id.txt);
            tvdetails = (TextView)itemView.findViewById(R.id.txt_details);
            image = (ImageView)itemView.findViewById(R.id.img);
            butMoreDetails=(Button)itemView.findViewById(R.id.but_more_details);
        }
    }
}
