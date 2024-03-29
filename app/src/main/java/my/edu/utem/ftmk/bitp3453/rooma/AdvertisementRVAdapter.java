package my.edu.utem.ftmk.bitp3453.rooma;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdvertisementRVAdapter extends RecyclerView.Adapter<AdvertisementRVAdapter.ViewHolder>{

    private ArrayList<Advertisement> advertisementArrayList;
    private Context context;
    private ItemClickListener mClickListener;

    SimpleDateFormat formatter;
    Date date;
    String todayDate, todayTime;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    // creating constructor for our adapter class
    public AdvertisementRVAdapter(ArrayList<Advertisement> advertisementArrayList, Context context) {
        this.advertisementArrayList = advertisementArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public AdvertisementRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.ads_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisementRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Advertisement advertisement = advertisementArrayList.get(position);

        String houseURL = null;
        houseURL = advertisement.getHouseURL();
        Picasso.with(context).load(houseURL).into(holder.ivAdsCover);

        holder.tvTitle.setText(advertisement.getTitle());
        holder.tvRentPrice.setText("RM " + advertisement.getMonthlyRent());
        holder.tvLocation.setText(advertisement.getState() + " > " + advertisement.getCity());
        holder.tvCategory.setText(advertisement.getCategory());
        holder.tvSize.setText(advertisement.getPropertySize() + " sq. ft.");

        if(advertisement.getBedroom() != null){
            holder.tvBedroom.setText(advertisement.getBedroom() + " Bedroom");
        }
        else{
            holder.tvBedroom.setText("1 Bedroom");
        }

        if(advertisement.getBathroom() != null){
            holder.tvBathroom.setText(advertisement.getBathroom() + " Bathroom");
        }
        else{
            holder.tvBathroom.setText("1 Bathroom");
        }

        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        date = new Date();
        todayDate = (formatter.format(date)).substring(0,10);
        todayTime = (formatter.format(date)).substring(11,16);

        String dbdate = advertisement.getPostDate();
        String dbtime = advertisement.getPostTime();

        if(todayDate.equals(dbdate)){
            dbdate = "Today";
            holder.tvAdsDate.setText(dbdate + " " + dbtime);
        }
        else {
            holder.tvAdsDate.setText(dbdate + " " + dbtime);
        }

//        holder.tvAdsDate.setText(advertisement.getPostDate() + " " + advertisement.getPostTime());
        holder.tvAdsID.setText(advertisement.getAdsID());

    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return advertisementArrayList.size();
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // creating variables for our text views.
        private final ImageView ivAdsCover;
        private final TextView tvTitle;
        private final TextView tvRentPrice;
        private final TextView tvLocation;
        private final TextView tvCategory;
        private final TextView tvSize;
        private final TextView tvBedroom;
        private final TextView tvBathroom;
        private final TextView tvAdsDate;
        private final TextView tvAdsID;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            ivAdsCover = itemView.findViewById(R.id.ivAdsCover);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRentPrice = itemView.findViewById(R.id.tvRentPrice);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvSize = itemView.findViewById(R.id.tvSize);
            tvBedroom = itemView.findViewById(R.id.tvBedroom);
            tvBathroom = itemView.findViewById(R.id.tvBathroom);
            tvAdsDate = itemView.findViewById(R.id.tvAdsDate);
            tvAdsID = itemView.findViewById(R.id.tvAdsID);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    void setClickListener(AdvertisementRVAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    public Advertisement getItem(int id) {
        return advertisementArrayList.get(id);
    }

}
