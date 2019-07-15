package com.example.oronz.mobiclientapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.oronz.mobiclientapp.Models.ManifestDetails;
import com.example.oronz.mobiclientapp.R;

import java.util.List;

public class ManifestRecyclerViewAdapter extends RecyclerView.Adapter<ManifestRecyclerViewAdapter.ViewHolder> {
    private List<ManifestDetails> items;
    private Activity activity;

    public ManifestRecyclerViewAdapter(Activity activity, List<ManifestDetails> items) {
        this.activity = activity;
        this.items = items;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.manifest_list, viewGroup, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ManifestRecyclerViewAdapter.ViewHolder viewHolder, int position) {

        viewHolder.routeTextView.setText(items.get(position).getRoute());
        viewHolder.manifestavilableseats.setText(items.get(position).getSeats_available());
        viewHolder.manifestseats.setText(items.get(position).getTotal_seats());


    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    /**
     * View holder to display each RecylerView item
     */
    protected class ViewHolder extends RecyclerView.ViewHolder {

        private TextView routeTextView, manifestavilableseats, manifestseats;

        public ViewHolder(View view) {
            super(view);
            routeTextView = view.findViewById(R.id.routeTextView);

            manifestavilableseats = view.findViewById(R.id.manifestavilableseats);

            manifestseats = view.findViewById(R.id.manifestseats);


        }


    }

}
