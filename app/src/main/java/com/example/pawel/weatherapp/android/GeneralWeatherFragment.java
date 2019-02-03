package com.example.pawel.weatherapp.android;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.WeatherModels.ForecastModel;
import com.example.pawel.weatherapp.project.GeneralWeatherInterface;
import com.example.pawel.weatherapp.project.GeneralWeatherPresenter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
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
	private ForecastListViewModel viewModel = null;
	
	
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
		
		postponeEnterTransition();
		
		forecastRecyclerView = view.findViewById(R.id.rv_forecast_list);
		FAB = view.findViewById(R.id.FAB_forecast_fab);
		refreshLayout = view.findViewById(R.id.sl_forecast_swipe);
		
		FAB.setOnClickListener(v -> presenter.showAddLocalizationSheet(getActivity()));
		refreshLayout.setOnRefreshListener(() -> presenter.refreshForecast());
		viewModel = ViewModelProviders.of(this).get(ForecastListViewModel.class);
		
		adapter = new MainFragmentAdapter(getOnClickTransitionListener());
		
		viewModel.observeWasDownloaded().observe(this, isDownloaded -> {
			if ( ! isDownloaded ) {
				presenter.addItemsToAdapter();
			}
		});
		
		viewModel.getObservableList()
				.observe(this, eForecasts -> adapter.add(eForecasts));
		
		forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		forecastRecyclerView.setAdapter(adapter);
		
		forecastRecyclerView.getViewTreeObserver()
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if ( forecastRecyclerView.getMeasuredHeight() > 0 && forecastRecyclerView.getMeasuredWidth() > 0 ) {
							forecastRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
							Log.i("PostponedTransition",
							      "globalLayoutListener w: " + forecastRecyclerView.getMeasuredWidth() + " h: " + forecastRecyclerView
									      .getMeasuredHeight());
							startPostponedEnterTransition();
						}
					}
				});
	}
	
	private MainFragmentAdapter.OnForecastItemClickListener getOnClickTransitionListener() {
		return (forecastId, mainView) -> {
			GeneralWeatherFragmentDirections.ActionGeneralWeatherFragmentToForecastDetailFragment action =
					GeneralWeatherFragmentDirections.actionGeneralWeatherFragmentToForecastDetailFragment(forecastId);
			FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder()
					.addSharedElement(mainView, mainView.getTransitionName())
					.build();
			Bundle data = new Bundle();
			data.putInt("cityId", forecastId);
			Navigation.findNavController(getView()).navigate(action.getActionId(), data, null, extras);
		};
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Log.i("Fragment", "Resume");
		presenter.onResume();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		Log.i("Fragment", "Pause");
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Log.i("Fragment", "Stop");
		presenter.onStop();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Fragment", "Destroy");
		presenter.onDestroy();
	}
	
	@Override
	public void addItemToAdapter(ForecastModel forecast) {
		if ( viewModel != null ) {
			viewModel.addForecast(forecast);
		}
	}
	
	@Override
	public void isRefreshing(boolean refresh) {
		viewModel.setDownloaded(true);
		refreshLayout.setRefreshing(refresh);
	}
	
	@Override
	public void removeForecastFromAdapter(ForecastModel forecast) {
		adapter.removeCity(forecast);
	}
	
	@Override
	public void showSnackbar(String message) {
		Snackbar snackbar = Snackbar.make(refreshLayout, message, Snackbar.LENGTH_LONG);
		snackbar.show();
	}
}
