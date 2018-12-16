package com.example.pawel.weatherapp.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.project.GeneralWeatherInterface;
import com.example.pawel.weatherapp.project.GeneralWeatherPresenter;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlibwithcityphotos.EForecast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


public class GeneralWeatherFragment
		extends Fragment
		implements GeneralWeatherInterface {
	
	private GeneralWeatherPresenter presenter;
	
	private FloatingActionButton FAB;
	private SwipeRefreshLayout refreshLayout;
	private RecyclerView forecastRecyclerView;
	private MainFragmentAdapter adapter;
	
	public GeneralWeatherFragment() {
	}
	
	public static GeneralWeatherFragment newInstance() {
		return new GeneralWeatherFragment();
	}
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		presenter = new GeneralWeatherPresenter(this);
		
		forecastRecyclerView = view.findViewById(R.id.rv_forecast_list);
		FAB = view.findViewById(R.id.FAB_forecast_fab);
		FAB.setOnClickListener(v -> presenter.showAddLocalizationSheet(getActivity()));
		refreshLayout = view.findViewById(R.id.sl_forecast_swipe);
		refreshLayout.setOnRefreshListener(() -> presenter.refreshForecast());
		
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
	public void onResume() {
		super.onResume();
		presenter.onResume();
		Log.i("Fragment", "Resume");
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
	public void onDestroy() {
		super.onDestroy();
		presenter.onDestroy();
	}
	
	@Override
	public void addItemToAdapter(EForecast forecast) {
		adapter.add(forecast);
	}
	
	@Override
	public void isRefreshing(boolean refresh) {
		refreshLayout.setRefreshing(refresh);
	}
    
    @Override
    public void removeForecastFromAdapter(Forecast forecast) {
        adapter.removeCity(forecast);
    }
    
    @Override
    public void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(refreshLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
