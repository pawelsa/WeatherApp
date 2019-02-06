package com.example.pawel.weatherapp.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainAdapter adapter = new MainAdapter();

        RecyclerView recyclerView = view.findViewById(R.id.rv_forecast_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        SwipeRefreshLayout refreshLayout = view.findViewById(R.id.sl_forecast_swipe);

        MainLib.addListener(new ForecastsListener() {
            @Override
            public void onSuccess(ForecastWithPhoto forecast) {
                Log.d("Fragment", "Adding forecast " + forecast.getPhotoReference());
                ForecastToView forecastToView = new ForecastToView(forecast);
                adapter.addForecast(forecastToView);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void isLoading(boolean loading) {
                refreshLayout.setRefreshing(loading);
            }

            @Override
            public void removedForecast(ForecastWithPhoto forecast) {

            }
        });

        MainLib.streamForecastsWithRefresh();


    }
}
