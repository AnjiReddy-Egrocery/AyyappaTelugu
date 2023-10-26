package com.example.ayyapatelugu.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ayyapatelugu.Activity.AyyapaBooksListActivity;
import com.example.ayyapatelugu.Activity.AyyapaMandaliDetailsActivity;
import com.example.ayyapatelugu.Activity.AyyappaMandaliListActivity;
import com.example.ayyapatelugu.Activity.GuruSwamiDetailsActivity;
import com.example.ayyapatelugu.Model.BajanaManadaliListModel;
import com.example.ayyapatelugu.Model.BooksModelResult;
import com.example.ayyapatelugu.R;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AyyapamandaliAdapter extends RecyclerView.Adapter<AyyapamandaliAdapter.MyviewHolder> {
    Context mContext;
    List<BajanaManadaliListModel> listModels;



    public AyyapamandaliAdapter(AyyappaMandaliListActivity ayyappaMandaliListActivity, List<BajanaManadaliListModel> list) {
        this.mContext=ayyappaMandaliListActivity;
        this.listModels=list;

    }


    @Override
    public AyyapamandaliAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ayyapamandali_list_adapter,parent,false);
        return new AyyapamandaliAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(AyyapamandaliAdapter.MyviewHolder holder, int position) {
        BajanaManadaliListModel modal = listModels.get(position);
        String imgUrl="https://www.ayyappatelugu.com/assets/user_images/"+modal.getProfilePic();
        String name=modal.getBajanamandaliName();
        String GuruNme=modal.getNameOfGuru();
        String City=modal.getBajanamandaliCity();
        String Number = modal.getBajanamandaliMobile();
        String Email = modal.getBajanamandaliEmail();
        String discription =modal.getBajanamandaliDescription();
        holder.tvtitle.setText(name);
        holder.tvadd.setText(modal.getBajanamandaliLocation());
        Picasso.get().load(imgUrl).into(holder.image);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(mContext,AyyapaMandaliDetailsActivity.class);
                intent.putExtra("ItemName",name);
                intent.putExtra("ItemGuruName",GuruNme);
                intent.putExtra("ItemCity",City);
                intent.putExtra("ItemNumber",Number);
                intent.putExtra("ItemEmail",Email);
                intent.putExtra("imagePath",imgUrl);
                intent.putExtra("Discription",discription);
                mContext.startActivity(intent);

            }
        });


        // Glide.with(context).load(movieList.get(position).getImageUrl()).apply(RequestOptions.centerCropTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(listModels != null){
            return listModels.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle,tvadd;
        ImageView image;

        Button button;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvtitle = (TextView)itemView.findViewById(R.id.txt_name);
            tvadd = (TextView)itemView.findViewById(R.id.txt_address);
            image = (ImageView)itemView.findViewById(R.id.img);

            button=(Button)itemView.findViewById(R.id.but_mostpopular);
        }
    }
}
