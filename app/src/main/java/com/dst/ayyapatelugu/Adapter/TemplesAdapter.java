package com.dst.ayyapatelugu.Adapter;

import android.content.Context;
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

public class TemplesAdapter extends RecyclerView.Adapter<TemplesAdapter.TempleViewHolder> {
    private List<AyyappaTempleMapDataResponse.Result> temples;

    public TemplesAdapter(List<AyyappaTempleMapDataResponse.Result> temples) {
        this.temples = temples;
    }

    @NonNull
    @Override
    public TempleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temple, parent, false);
        return new TempleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempleViewHolder holder, int position) {
        AyyappaTempleMapDataResponse.Result temple = temples.get(position);
        holder.nameTextView.setText(temple.getTempleNameTelugu());
        holder.locationTextView.setText(temple.getLocation());
    }

    @Override
    public int getItemCount() {
        return temples.size();
    }

    public void updateData(List<AyyappaTempleMapDataResponse.Result> updatedList) {
        this.temples = updatedList;
        notifyDataSetChanged(); // Notify RecyclerView to refresh its data
    }

    static class TempleViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, locationTextView;

        public TempleViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_temple_name);
            locationTextView = itemView.findViewById(R.id.text_temple_location);
        }
    }
}
