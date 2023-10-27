package com.dst.ayyapatelugu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Activity.GuruSwamiDetailsActivity;
import com.dst.ayyapatelugu.Activity.GuruSwamiListActivity;
import com.dst.ayyapatelugu.Model.GuruSwamiModelList;


import com.dst.ayyapatelugu.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GuruSwamiListAdapter extends RecyclerView.Adapter<GuruSwamiListAdapter.MyviewHolder> {

    Context mContext;
    List<GuruSwamiModelList> listModel;

    public GuruSwamiListAdapter(GuruSwamiListActivity guruSwamiListActivity, List<GuruSwamiModelList> guruswamilist) {
        this.mContext = guruSwamiListActivity;
        this.listModel = guruswamilist;

    }


    @Override
    public GuruSwamiListAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.guruswammi_list_adapter, parent, false);
        return new GuruSwamiListAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuruSwamiListAdapter.MyviewHolder holder, int position) {
        GuruSwamiModelList modal = listModel.get(position);
        String imgUrl = "https://www.ayyappatelugu.com/assets/user_images/" + modal.getProfilePic();
        String name = modal.getGuruswamiName();
        String number = modal.getMobileNo();
        String temple = modal.getTempleName();
        String cityName = modal.getCityName();
        holder.tvtitle.setText(name);
        holder.tvaddress.setText(cityName);
        Picasso.get().load(imgUrl).into(holder.image);

        holder.butMostPopular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, GuruSwamiDetailsActivity.class);
                intent.putExtra("Name", name);
                intent.putExtra("Number", number);
                intent.putExtra("Temple", temple);
                intent.putExtra("City", cityName);
                intent.putExtra("Image", imgUrl);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        if (listModel != null) {
            return listModel.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle, tvaddress;
        ImageView image;

        Button butMostPopular;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvtitle = (TextView) itemView.findViewById(R.id.txt_name);
            tvaddress = (TextView) itemView.findViewById(R.id.txt_address);
            image = (ImageView) itemView.findViewById(R.id.img);
            butMostPopular = (Button) itemView.findViewById(R.id.but_mostpopular);
        }

    }
}
