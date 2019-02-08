package com.example.pawel.weatherapp.android.main;

import android.os.Bundle;
import android.util.Log;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

//TODO: App crashes when new city is added, because color array is not created, because there is no data

public class MainFragment extends Fragment {

    private static final String TAG = "MainFragment";

    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainFragmentViewModel viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(navigateToForecastDetail());
        }
        binding.setViewModel(viewModel);

        viewModel.getForecastList()
                .observe(this, forecastToViews -> {
                    viewModel.setForecastsToAdapter(forecastToViews);
                    if (forecastToViews.isEmpty()) {
                        MainLib.streamForecastsWithRefresh();
                    }
                });

        MainLib.addListener(forecastsListener(viewModel));

        //MainLib.downloadNewForecastFor("Pszczyna");
        //MainLib.downloadNewForecastFor("Mountain View");
        //MainLib.downloadNewForecastFor("Warszawa");

    }

    private ForecastsListener forecastsListener(MainFragmentViewModel viewModel) {
        return new ForecastsListener() {
            @Override
            public void onSuccess(ForecastWithPhoto forecast) {
                Log.d("Fragment", "Adding forecast");
                ForecastToView forecastToView = new ForecastToView(forecast);
                viewModel.addNewForecast(forecastToView);
            }

            @Override
            public void onError(Throwable t) {
                Snackbar.make(getActivity().findViewById(R.id.main_activity_fragment), t.getMessage(), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void isLoading(boolean loading) {
                Log.d(TAG, "isLoading: " + loading);
                viewModel.setLoading(loading);
            }

            @Override
            public void removedForecast(ForecastWithPhoto forecast) {
                Log.d(TAG, "removedForecast");
                ForecastToView forecastToView = new ForecastToView(forecast);
                viewModel.removeForecast(forecastToView);
            }
        };
    }

    private MainAdapter.OnForecastClicked navigateToForecastDetail() {
        return (forecastID, cityName) -> {
            MainFragmentDirections.ActionGeneralWeatherFragmentToForecastDetailFragment action =
                    MainFragmentDirections.actionGeneralWeatherFragmentToForecastDetailFragment(forecastID, cityName);
            Bundle data = new Bundle();
            data.putInt("cityId", forecastID);
            data.putString("cityName", cityName);
            Navigation.findNavController(getActivity(), R.id.main_activity_fragment)
                    .navigate(action.getActionId(), data, null);
        };
    }

}
