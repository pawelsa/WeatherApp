package com.example.pawel.weatherapp.android.main;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.FragmentMainBinding;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.transition.Explode;

//TODO: Fixed* - App crashes when new city is added, because color array is not created, because there is no data
//TODO: When not downloaded, the "Not downloaded" text does not appear

public class MainFragment
		extends Fragment {
	
	private static final String TAG = "MainFragment";
	
	private FragmentMainBinding binding;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(
				inflater, R.layout.fragment_main, container, false);
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setEnterTransition(new Explode());
		setExitTransition(new Explode());
		
		MainFragmentViewModel viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);
		if ( savedInstanceState == null ) {
			Log.d(TAG, "onViewCreated: viewModel init");
			viewModel.init();
		}
		
		binding.setViewModel(viewModel);
		binding.rvHomeList.setItemAnimator(new DefaultItemAnimator());
		
		viewModel.getForecasts().observe(this, new Observer<List<ForecastToView>>() {
			@Override
			public void onChanged(List<ForecastToView> forecastToViewList) {
				viewModel.setForecastsInAdapter(forecastToViewList);
			}
		});
		
		viewModel.getSelectedToRemove().observe(this, this::displayRemoveDialog);
		
		viewModel.getSelected().observe(this, binding -> {
			if ( binding != null ) {
				ForecastToView forecastToView = binding.getForecast();
				
				MainFragmentDirections.ActionGeneralWeatherFragmentToForecastDetailFragment action =
						MainFragmentDirections.actionGeneralWeatherFragmentToForecastDetailFragment(forecastToView.getCityID(),
						                                                                            forecastToView.getCityName());
				ViewCompat.setTransitionName(binding.getRoot(), String.valueOf(forecastToView.getCityID()));
				
				FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
						.addSharedElement(binding.getRoot(), binding.getRoot().getTransitionName())
						.build();
				Bundle data = new Bundle();
				data.putInt("cityId", forecastToView.getCityID());
				data.putString("cityName", forecastToView.getCityName());
				Navigation.findNavController(getActivity(), R.id.main_activity_fragment)
						.navigate(action.getActionId(), data, null, extras);
			}
		});
		MainLib.addListener(forecastsListener(viewModel));
		
		MainLib.downloadNewForecastFor("Pszczyna");
		MainLib.downloadNewForecastFor("Mountain View");
		MainLib.downloadNewForecastFor("Warszawa");
		
	}
	
	private ForecastsListener forecastsListener(MainFragmentViewModel viewModel) {
		return new ForecastsListener() {
			@Override
			public void onSuccess(ForecastWithPhoto forecast) {
			}
			
			@Override
			public void onError(Throwable t) {
				Snackbar.make(getActivity().findViewById(R.id.cl_home_main), t.getMessage(), Snackbar.LENGTH_LONG)
						.show();
			}
			
			@Override
			public void isLoading(boolean loading) {
				Log.d(TAG, "isLoading: " + loading);
				viewModel.setLoading(loading);
			}
			
			@Override
			public void removedForecast(ForecastWithPhoto forecast) {
			}
		};
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy: ");
	}
	
	private void displayRemoveDialog(Pair<String, Integer> pair) {
		String cityName = pair.first;
		int cityID = pair.second;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle(getContext().getText(R.string.dialog_remove_city_make_sure))
				.setMessage(String.format(getContext().getString(R.string.dialog_remove_city_content),
				                          cityName))
				.setPositiveButton(getContext().getString(android.R.string.ok),
				                   (dialog, which) -> MainLib.removeForecastFor(cityName, cityID))
				.setNegativeButton(getContext().getText(android.R.string.cancel), ((dialog, which) -> dialog.cancel()));
		
		builder.show();
	}
	
}
