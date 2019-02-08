package com.example.pawel.weatherapp.android.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.CardMainForecastBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ForecastVH> {

    private ArrayList<ForecastToView> forecastList = new ArrayList<>();
    private OnForecastClicked listener;

    public MainAdapter() {
    }

    public MainAdapter(ArrayList<ForecastToView> forecastList) {
        this.forecastList = forecastList;
    }

    public MainAdapter(OnForecastClicked listener) {
        this.listener = listener;
    }

    public MainAdapter(ArrayList<ForecastToView> forecastList, OnForecastClicked listener) {
        this.forecastList = forecastList;
        this.listener = listener;
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

    public void setForecastList(List<ForecastToView> forecastList) {
        //TODO : Add diffUtil
        this.forecastList.clear();
        this.forecastList.addAll(forecastList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }


    interface OnForecastClicked {
        void forecastClicked(int forecastID, String cityName);
    }

    class ForecastVH extends RecyclerView.ViewHolder {

        private CardMainForecastBinding binding;

        public ForecastVH(CardMainForecastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ForecastToView viewForecast) {

            binding.setForecast(viewForecast);
            binding.clCardMain.setOnClickListener(v ->
                    listener.forecastClicked(binding.getForecast().getCityID(),
                            binding.getForecast().getCityName()));
        }
    }

}
