package my.edu.utem.ftmk.bitp3453.rooma;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportedRVAdapter extends RecyclerView.Adapter<ReportedRVAdapter.ViewHolder>{

    private ArrayList<Advertisement> advertisementArrayList;
    private Context context;
    private ReportedRVAdapter.ItemClickListener mClickListener;

    Handler mainHandler = new Handler();
    ProgressDialog progressDialog;

    // creating constructor for our adapter class
    public ReportedRVAdapter(ArrayList<Advertisement> advertisementArrayList, Context context) {
        this.advertisementArrayList = advertisementArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReportedRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ReportedRVAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.reported_ads_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportedRVAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        Advertisement advertisement = advertisementArrayList.get(position);

        holder.tvReportRef.setText("Report Ref: " + advertisement.getReportRef());
        holder.tvAdsID.setText("Ads ID: " + advertisement.getAdsID());
        holder.tvReason.setText("Reason: " + advertisement.getReason());
        holder.tvDate.setText("Date: " + advertisement.getReportDate());
        holder.tvTime.setText("Time: " + advertisement.getReportTime());

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
        private final TextView tvReportRef;
        private final TextView tvAdsID;
        private final TextView tvDate;
        private final TextView tvTime;
        private final TextView tvReason;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            tvReportRef = itemView.findViewById(R.id.tvReportRef);
            tvAdsID = itemView.findViewById(R.id.tvAdsID);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvReason = itemView.findViewById(R.id.tvReason);

            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    void setClickListener(ReportedRVAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // convenience method for getting data at click position
    public Advertisement getItem(int id) {
        return advertisementArrayList.get(id);
    }

}
