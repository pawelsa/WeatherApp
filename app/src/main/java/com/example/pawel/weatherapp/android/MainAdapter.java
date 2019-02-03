package com.example.pawel.weatherapp.android;

import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.databinding.CardLayoutBinding;
import com.example.pawel.weatherapp.databinding.CardLayoutNoBinding;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ForecastVH> {


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public ForecastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastVH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    class ForecastVH extends RecyclerView.ViewHolder {

        CardLayoutNoBinding noBinding;
        CardLayoutBinding binding;

        public ForecastVH(@NonNull View itemView) {
            super(itemView);
        }

        void bind(){

        }
    }

}
