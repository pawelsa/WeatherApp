package com.example.pawel.weatherapp.android;


import android.os.Bundle;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.FragmentForecastDetailBinding;
import com.example.weatherlibwithcityphotos.EForecast;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class ForecastDetailFragment
		extends Fragment {
	private FragmentForecastDetailBinding binding;
	private EForecast forecast;
	
	public ForecastDetailFragment() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forecast_detail, container, false);
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		postponeEnterTransition();
		
		
		int itemId = ForecastDetailFragmentArgs.fromBundle(getArguments()).getCityId();
		MainLib.readForecastFor(itemId)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(eForecast -> {
					forecast = eForecast;
					binding.setForecast(forecast);
					view.findViewById(R.id.cl_detail_main).setTransitionName(String.valueOf(itemId));
					ChangeBounds changeBounds = new ChangeBounds();
					changeBounds.setDuration(200L);
					this.setSharedElementEnterTransition(changeBounds);
					this.setSharedElementReturnTransition(changeBounds);
					
					binding.rvDetailWeathers.setLayoutManager(new LinearLayoutManager(this.getContext(),
					                                                                  LinearLayoutManager.HORIZONTAL,
					                                                                  false));
					binding.rvDetailWeathers.setHasFixedSize(true);
					
					RecyclerView.Adapter adapter =
							new WeatherItemAdapter(forecast.weatherList,
							                       id -> binding.getForecast().setDisplayValue(forecast.weatherList
									                                                                   .get(id)));
					binding.rvDetailWeathers.setAdapter(adapter);
					startPostponedEnterTransition();
				});
		
		
	}
}
