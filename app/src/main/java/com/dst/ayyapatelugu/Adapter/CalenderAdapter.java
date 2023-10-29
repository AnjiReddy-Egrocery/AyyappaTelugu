package com.dst.ayyapatelugu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Activity.CalenderActivity;
import com.dst.ayyapatelugu.Model.CalenderDataResponse;
import com.dst.ayyapatelugu.R;

import java.util.List;

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
      String openDate=poojasList.getOpeningDate();
      String poojaName= poojasList.getPoojaName();
      String closedDate=poojasList.getClosingDate();

      holder.txtYear.setText(month);
      holder.txtOpeningDate.setText(openDate);
      holder.txtPoojaName.setText(poojaName);
      holder.txtClosingDate.setText(closedDate);

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
