package com.example.pawel.weatherapp.android.main;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.android.dialogs.AddLocalizationBottomSheet;
import com.example.pawel.weatherapp.databinding.FragmentMainBinding;
import com.example.pawel.weatherapp.databinding.MainCardForecastBinding;
import com.example.pawel.weatherapp.weatherModels.ForecastToView;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;
import com.example.weatherlibwithcityphotos.Units;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.transition.Explode;

// TODO: 28.02.2019 changing between C and F

public class MainFragment
		extends Fragment {
	
	private static final String TAG = "MainFragment";
	
	private FragmentMainBinding binding;
	private AddLocalizationBottomSheet bottomSheet;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
		(( AppCompatActivity ) getActivity()).setSupportActionBar(binding.tbHomeToolbar);
		setHasOptionsMenu(true);
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		setEnterTransition(new Explode());
		setExitTransition(new Explode());
		
		MainFragmentViewModel viewModel = getViewModelFromProvider(savedInstanceState);
		
		binding.setViewModel(viewModel);
		
		setViewModelObservers(viewModel);
		
		MainLib.addListener(forecastsListener(viewModel));
		
		binding.FABHomeFab.setOnClickListener(v -> {
			bottomSheet = new AddLocalizationBottomSheet();
			bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
		});
		
	}
	
	private MainFragmentViewModel getViewModelFromProvider(Bundle savedInstanceState) {
		MainFragmentViewModel viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);
		if ( savedInstanceState == null ) {
			Log.d(TAG, "onViewCreated: viewModel init");
			viewModel.init();
		}
		return viewModel;
	}
	
	private void setViewModelObservers(MainFragmentViewModel viewModel) {
		
		viewModel.getForecasts().observe(this, viewModel::setForecastsInAdapter);
		
		viewModel.getSelectedToRemove().observe(this, this::displayRemoveDialog);
		
		viewModel.getSelected().observe(this, this::navigateTo);
	}
	
	private ForecastsListener forecastsListener(MainFragmentViewModel viewModel) {
		return new ForecastsListener() {
			@Override
			public void onSuccess(ForecastWithPhoto forecast) {
			}
			
			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
				Snackbar.make(binding.clHomeMain, t.getMessage(), Snackbar.LENGTH_LONG)
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
		
		AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
		builder.setTitle(getContext().getText(R.string.dialog_remove_city_make_sure))
				.setMessage(String.format(getContext().getString(R.string.dialog_remove_city_content),
				                          cityName))
				.setPositiveButton(getContext().getString(android.R.string.ok),
				                   (dialog, which) -> MainLib.removeForecastFor(cityName, cityID))
				.setNegativeButton(getContext().getText(android.R.string.cancel), ((dialog, which) -> dialog.cancel()));
		
		builder.show();
	}
	
	private void navigateTo(MainCardForecastBinding binding) {
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
			Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.main_activity_fragment)
					.navigate(action.getActionId(), data, null, extras);
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(@NonNull Menu menu) {
		menu.clear();
		if ( MainLib.getUnits().equals("METRIC") ) {
			menu.add(0, 0, Menu.NONE, getString(R.string.fahrenheit));
			menu.findItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		} else {
			menu.add(0, 1, Menu.NONE, getString(R.string.celsius));
			menu.findItem(1).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
			case 0:
				MainLib.useUnits(Units.IMPERIAL);
				break;
			case 1:
				MainLib.useUnits(Units.METRIC);
				break;
		}
		getActivity().invalidateOptionsMenu();
		return false;
	}
}
