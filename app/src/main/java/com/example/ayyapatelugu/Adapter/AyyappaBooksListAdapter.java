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

import androidx.recyclerview.widget.RecyclerView;

import com.example.ayyapatelugu.Activity.AyyapaBooksListActivity;
import com.example.ayyapatelugu.Activity.AyyappaBooksDetailsActivity;
import com.example.ayyapatelugu.Activity.AyyappaPetamListActivity;
import com.example.ayyapatelugu.Model.BooksModelResult;
import com.example.ayyapatelugu.Model.decoratormodelResult;
import com.example.ayyapatelugu.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

public class AyyappaBooksListAdapter extends RecyclerView.Adapter<AyyappaBooksListAdapter.MyviewHolder> {
    Context mContext;
    List<BooksModelResult> bookList;



    public AyyappaBooksListAdapter(AyyapaBooksListActivity ayyapaBooksListActivity, List<BooksModelResult> books) {
        this.mContext=ayyapaBooksListActivity;
        this.bookList=books;

    }


    @Override
    public AyyappaBooksListAdapter.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.ayyapabooks_list_adapter,parent,false);
        return new AyyappaBooksListAdapter.MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(AyyappaBooksListAdapter.MyviewHolder holder, int position) {
        BooksModelResult modal = bookList.get(position);
        String imgUrl="https://www.ayyappatelugu.com/assets/bookimage/"+modal.getImage();
        String name= modal.getName();
        String price= modal.getPrice();
        String author= modal.getAuthor();
        String pages=modal.getPages();
        String published= modal.getPublishedOn();
        holder.tvtitle.setText(name);
        holder.tvprice.setText(price);
        Picasso.get().load(imgUrl).into(holder.image);

        holder.butViewdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,AyyappaBooksDetailsActivity.class);
                intent.putExtra("Name",name);
                intent.putExtra("Price",price);
                intent.putExtra("Author",author);
                intent.putExtra("Pages",pages);
                intent.putExtra("Published",published);
                intent.putExtra("ImageAuth",imgUrl);
                mContext.startActivity(intent);
            }
        });


        // Glide.with(context).load(movieList.get(position).getImageUrl()).apply(RequestOptions.centerCropTransform()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        if(bookList != null){
            return bookList.size();
        }
        return 0;

    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle,tvprice;
        ImageView image;

        Button butViewdetails;

        public MyviewHolder(View itemView) {
            super(itemView);
            tvtitle = (TextView)itemView.findViewById(R.id.txtname);
            tvprice = (TextView)itemView.findViewById(R.id.txt_price);
            image = (ImageView)itemView.findViewById(R.id.img_detail);
            butViewdetails=(Button)itemView.findViewById(R.id.but_view_details);

        }
    }
}
