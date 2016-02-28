package cs.androidnfc.adapters;

import android.animation.ObjectAnimator;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import cs.androidnfc.R;

/**
 * Created by Suhan on 2/27/16.
 */
public class WifiRecyclerViewAdapter extends RecyclerView.Adapter<WifiRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<ScanResult> mWifi;
    private final static int PROGRESS_BAR_ANIMATION_DURATION = 3000;
    private final static String ANIMATOR_PROPERTY_NAME = "progress";

    public WifiRecyclerViewAdapter(ArrayList<ScanResult> test) {
        mWifi = test;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wifi_list, parent, false);
        ViewHolder vh = new ViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(mWifi.get(position).SSID);
        ObjectAnimator animation = ObjectAnimator.ofInt(holder.mWifiLevel, ANIMATOR_PROPERTY_NAME,
                WifiManager.calculateSignalLevel(mWifi.get(position).level, 100));
        animation.setDuration(PROGRESS_BAR_ANIMATION_DURATION);
        animation.setInterpolator(new DecelerateInterpolator(1.5f));
        animation.start();

    }

    @Override
    public int getItemCount() {
        return mWifi.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ProgressBar mWifiLevel;
        public ViewHolder(View v) {
            super(v);
            mWifiLevel = (ProgressBar) v.findViewById(R.id.wifi_level);
            mTextView = (TextView) v.findViewById(R.id.wifi_name);
        }
    }

}
