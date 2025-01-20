package com.dst.ayyapatelugu.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dst.ayyapatelugu.Model.AyyappaTempleMapDataResponse;
import com.dst.ayyapatelugu.Model.TempleMapDataResponse;
import com.dst.ayyapatelugu.R;

import java.util.List;

public class TemplesMapAdapter extends RecyclerView.Adapter<TemplesMapAdapter.TempleViewHolder>{
    private List<TempleMapDataResponse.Result> temples;
    public TemplesMapAdapter(List<TempleMapDataResponse.Result> nearbyTemples) {

        temples = nearbyTemples;
    }

    @NonNull
    @Override
    public TemplesMapAdapter.TempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map_temple, parent, false);
        return new TemplesMapAdapter.TempleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplesMapAdapter.TempleViewHolder holder, int position) {
        TempleMapDataResponse.Result temple = temples.get(position);
        holder.nameTextView.setText(temple.getTempleNameTelugu());
        holder.locationTextView.setText(temple.getLocation());
    }

    @Override
    public int getItemCount() {
        return temples.size();
    }

    public void updateData(List<TempleMapDataResponse.Result> updatedList) {
        this.temples = updatedList;
        notifyDataSetChanged(); // Notify RecyclerView to refresh its data
    }


    public class TempleViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, locationTextView;
        public TempleViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_temple_name);
            locationTextView = itemView.findViewById(R.id.text_temple_location);
        }
    }
}
