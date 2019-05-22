package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.oronz.mobiclientapp.Models.RecyclerViewItem;
import com.example.oronz.mobiclientapp.R;
import com.google.android.material.chip.Chip;

import java.util.List;

public class GridViewAdapterVehicles extends RecyclerView.Adapter<GridViewAdapterVehicles.ViewHolder> {
    private List<RecyclerViewItem> items;
    private Activity activity;

    public GridViewAdapterVehicles(Activity activity, List<RecyclerViewItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.vehicle_item_grid, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewAdapterVehicles.ViewHolder viewHolder, int position) {

        viewHolder.imageView.setImageResource(items.get(position).getDrawableId());
        viewHolder.textView.setText(items.get(position).getTitle());
        viewHolder.titleview.setText(items.get(position).getName());
        viewHolder.grid_vehicle_remaining.setText(items.get(position).getAvailable());
        viewHolder.selected_car_id.setText(items.get(position).getCa_id());
        viewHolder.chipseaterview.setText(String.format("Seater: %s\nAvailable: %s", items.get(position).getTitle(), items.get(position).getAvailable()));

        viewHolder.chip.setText(items.get(position).getCa_id());
        viewHolder.chipseater.setText(items.get(position).getTitle());


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {
        Chip chip,chipseater,chipseaterview;
        private ImageView imageView;
        private TextView textView, titleview, grid_vehicle_remaining, selected_car_id, textviewtext;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.grid_vehicle_seater);
            imageView = view.findViewById(R.id.image);
            titleview = view.findViewById(R.id.grid_vehicle_title);

            grid_vehicle_remaining = view.findViewById(R.id.grid_vehicle_remaining);

            selected_car_id = view.findViewById(R.id.selected_car_id);
            textviewtext = view.findViewById(R.id.textviewtext);


            chip = view.findViewById(R.id.chip_available);

            chipseater = view.findViewById(R.id.chipseater);
            chipseaterview=view.findViewById(R.id.chipseaterview);




        }


    }


}