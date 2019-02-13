package com.example.pawel.weatherapp.android.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.FragmentForecastDetailBinding;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.transition.ChangeBounds;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailFragment
		extends Fragment {
	
	FragmentForecastDetailBinding binding;
	
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		binding = DataBindingUtil.inflate(
				inflater, R.layout.fragment_forecast_detail, container, false);
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
		
		int itemId = DetailFragmentArgs.fromBundle(getArguments()).getCityId();
		Log.i("ForecastID", "DetailFragment : " + itemId);
		
		Disposable disposable = MainLib.readForecastFor(itemId)
				.map(ForecastToView::new)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.map(forecastToView -> {
					DetailViewModel viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
					if ( savedInstanceState == null ) {
						viewModel.init();
					}
					viewModel.addNewForecast(forecastToView);
					return viewModel;
				})
				.subscribe(viewModel -> {
					binding.setViewModel(viewModel);
					
					binding.detailHeaderGradient.getRoot().setTransitionName(String.valueOf(itemId));
					ChangeBounds changeBounds = new ChangeBounds();
					changeBounds.setDuration(300L);
					this.setSharedElementEnterTransition(changeBounds);
					this.setSharedElementReturnTransition(changeBounds);
					
					startPostponedEnterTransition();
				});
	}
}
