package com.example.pawel.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class GeneralWeatherFragment extends Fragment implements GeneralWeatherInterface {
    
    private GeneralWeatherPresenter presenter;
    
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter = new GeneralWeatherPresenter(this);
        
        forecastRecyclerView = view.findViewById(R.id.brief_forecast);
        view.findViewById(R.id.FAB_add)
                .setOnClickListener(v -> presenter.showAddLocalizationSheet(getActivity()));
    }
    
    @Override
    public void addItemToAdapter(PlaceWeatherData placeWeatherData) {
        adapter.add(placeWeatherData);
    }
    
    @Override
    public void onStop() {
        super.onStop();
        presenter.onStop();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
