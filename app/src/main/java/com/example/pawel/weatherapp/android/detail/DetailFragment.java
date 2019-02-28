package com.example.pawel.weatherapp.android.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.FragmentForecastDetailBinding;
import com.example.pawel.weatherapp.weatherModels.ForecastToView;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.ChangeBounds;

public class DetailFragment
		extends Fragment {
	
	private FragmentForecastDetailBinding binding;
	private DetailViewModel viewModel;
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(
				inflater, R.layout.fragment_forecast_detail, container, false);
		(( AppCompatActivity ) getActivity()).setSupportActionBar(binding.tbDetailToolbar);
		return binding.getRoot();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		postponeEnterTransition();
		
		NavController controller = Navigation.findNavController(view);
		NavigationUI.setupWithNavController(view.findViewById(R.id.ctb_detail_collapse),
		                                    view.findViewById(R.id.tb_detail_toolbar),
		                                    controller);
		
		viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
		if ( savedInstanceState == null ) {
			viewModel.init();
		}
		
		int itemId = DetailFragmentArgs.fromBundle(getArguments()).getCityId();
		
		MainLib.addListener(getListener(itemId));
		MainLib.readForecastFor(itemId);
	}
	
	private ForecastsListener getListener(int itemId) {
		return new ForecastsListener() {
			@Override
			public void onSuccess(ForecastWithPhoto forecast) {
				ForecastToView forecastToView = new ForecastToView(forecast);
				viewModel.addNewForecast(forecastToView);
				
				binding.setViewModel(viewModel);
				
				binding.detailHeaderGradient.getRoot().setTransitionName(String.valueOf(itemId));
				ChangeBounds changeBounds = new ChangeBounds();
				changeBounds.setDuration(300L);
				setSharedElementEnterTransition(changeBounds);
				setSharedElementReturnTransition(changeBounds);
				
				startPostponedEnterTransition();
			}
			
			@Override
			public void onError(Throwable t) {
			
			}
			
			@Override
			public void isLoading(boolean loading) {
			
			}
			
			@Override
			public void removedForecast(ForecastWithPhoto forecast) {
			
			}
		};
	}
}
