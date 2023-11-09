package com.dst.ayyapatelugu.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Activity.CalenderActivity;
import com.dst.ayyapatelugu.Model.CalenderDataResponse;
import com.dst.ayyapatelugu.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.MyViewHolder> {
    Context mContext;
    List<CalenderDataResponse.Result.Poojas> poojas;
    public CalenderAdapter(CalenderActivity calenderActivity, List<CalenderDataResponse.Result.Poojas> topicsList) {
        this.mContext=calenderActivity;
        this.poojas=topicsList;
    }

    @NonNull
    @Override
    public CalenderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderAdapter.MyViewHolder holder, int position) {
      CalenderDataResponse.Result.Poojas poojasList=poojas.get(position);

      String month=poojasList.getMonthName();
      long openDateTime=Long.parseLong(poojasList.getOpeningDate());
      String poojaName= poojasList.getPoojaName();
      long closedDateTime=Long.parseLong(poojasList.getClosingDate());

        String formattedOpenDate="";
        String formattedClosedDate="";

        if (openDateTime != 0){
            Date openDate=new Date(openDateTime * 1000);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            formattedOpenDate=dateFormat.format(openDate);
        }

        if (closedDateTime != 0){
            Date closedDate=new Date(closedDateTime * 1000);
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy",Locale.getDefault());
            formattedClosedDate=dateFormat.format(closedDate);
        }
      holder.txtYear.setText(month);
      holder.txtOpeningDate.setText(formattedOpenDate);
      holder.txtPoojaName.setText(poojaName);
      holder.txtClosingDate.setText(formattedClosedDate);

    }
    @Override
    public int getItemCount() {
        return poojas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtYear;
        TextView txtOpeningDate;
        TextView txtPoojaName;
        TextView txtClosingDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtYear=itemView.findViewById(R.id.txt_month);
            txtOpeningDate=itemView.findViewById(R.id.txt_open_date);
            txtPoojaName=itemView.findViewById(R.id.txt_pooja_name);
            txtClosingDate=itemView.findViewById(R.id.txt_close_date);

        }
    }
}
