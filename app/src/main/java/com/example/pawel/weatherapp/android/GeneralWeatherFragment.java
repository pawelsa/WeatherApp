package com.example.pawel.weatherapp.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.project.GeneralWeatherInterface;
import com.example.pawel.weatherapp.project.GeneralWeatherPresenter;
import com.example.weatherlib.project.WeatherModel.Forecast;


public class GeneralWeatherFragment extends Fragment implements GeneralWeatherInterface {
    
    private GeneralWeatherPresenter presenter;
    
    private FloatingActionButton FAB;
    private RecyclerView forecastRecyclerView;
    private MainFragmentAdapter adapter;
    
    public GeneralWeatherFragment() {
    }
    
    public static GeneralWeatherFragment newInstance() {
        return new GeneralWeatherFragment();
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        adapter = new MainFragmentAdapter();
        forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        forecastRecyclerView.setAdapter(adapter);
        presenter.addItemsToAdapter();
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new GeneralWeatherPresenter(this);
        
        forecastRecyclerView = view.findViewById(R.id.brief_forecast);
        view.findViewById(R.id.FAB_add)
                .setOnClickListener(v -> presenter.showAddLocalizationSheet(getActivity()));
        //FAB = view.findViewById(R.id.FAB_add);
    }
    
    @Override
    public void onPause() {
        super.onPause();
        Log.i("Fragment", "Pause");
    }
    
    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
        Log.i("Fragment", "Stop");
    }
    
    @Override
    public void addItemToAdapter(Forecast forecast) {
        adapter.add(forecast);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
