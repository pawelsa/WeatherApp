package com.example.pawel.weatherapp.android;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.databinding.CardLayoutBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ForecastVH> {

    private ArrayList<ForecastToView> forecastList = new ArrayList<>();

    public MainAdapter() {
    }

    public MainAdapter(ArrayList<ForecastToView> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastVH(CardLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastVH holder, int position) {
        holder.bind(forecastList.get(position));
    }

    public void addForecast(ForecastToView forecast) {

        if (forecastList.contains(forecast)) {
            int index = forecastList.indexOf(forecast);
            forecastList.set(index, forecast);
            notifyItemChanged(index);
        } else {
            forecastList.add(forecast);
            notifyItemInserted(forecastList.size() - 1);
        }
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }


    class ForecastVH extends RecyclerView.ViewHolder {

        private CardLayoutBinding binding;

        public ForecastVH(CardLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ForecastToView viewForecast) {

            binding.setForecast(viewForecast);
        }
    }

}
