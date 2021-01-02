package com.example.landscape.adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landscape.activities.MainActivity;
import com.example.landscape.activities.SavedLocationsActivity;
import com.example.landscape.classes.FavouritePlace;
import com.example.landscape.R;
import java.util.List;

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.MyViewHolder> {
    Context ct;
    List<FavouritePlace> info;

    public SavedLocationAdapter(Context ct, List<FavouritePlace> info) {
        this.ct = ct;
        this.info = info;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(this.ct);
        View view = inflater.inflate(R.layout.saved_location_item,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position)
    {


        holder.imageView.setImageResource(R.drawable.list_image);

        // Change TextView texts
        holder.name.setText(info.get(position).getPlaceName());
        holder.adresss.setText( info.get(position).getPlaceAddresses()  );




        // Possible Error
        try {

holder.viewOnMap.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(ct, MainActivity.class);


MainActivity.selectedSavedLocation = true;
        intent.putExtra("placeName", info.get(position).placeName);
        intent.putExtra("placeAddresses", info.get(position).placeAddresses);
        intent.putExtra("placeLongitude",info.get(position).placeLongitude);
        intent.putExtra("placeLatitude",info.get(position).placeLatitude);





        ct.startActivity(intent);

    }
});



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount()
    {
        return info.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder
    {
        TextView name;
        TextView adresss;
      ImageView imageView;
        CardView cardview;
        Button viewOnMap;
        View blue_line;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.day);
            adresss = itemView.findViewById(R.id.tv_date);
            viewOnMap = itemView.findViewById(R.id.action_button_1);


imageView = itemView.findViewById(R.id.imageView);
            cardview = itemView.findViewById(R.id.cardview);

        }
    }

}
