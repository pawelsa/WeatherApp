package com.example.pawel.weatherapp.android;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.CardMainForecastBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
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

        CardMainForecastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.card_main_forecast, parent, false);

        return new ForecastVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastVH holder, int position) {
        holder.bind(forecastList.get(position));
    }

    public void addForecast(ForecastToView forecast) {

        if (forecastList.contains(forecast)) {
            int index = forecastList.indexOf(forecast);
            forecastList.get(index).updateModelWithNewData(forecast);
        } else {
            forecastList.add(forecast);
            notifyItemInserted(forecastList.size());
        }
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }


    class ForecastVH extends RecyclerView.ViewHolder {

        private CardMainForecastBinding binding;

        public ForecastVH(CardMainForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ForecastToView viewForecast) {

            binding.setForecast(viewForecast);
        }
    }

}
