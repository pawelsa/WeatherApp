package com.example.pawel.weatherapp;

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

import com.example.pawel.weatherapp.API.RFRequest;
import com.example.pawel.weatherapp.Database.DatabaseManager;

import io.reactivex.disposables.Disposable;


public class MainFragment extends Fragment {


	Disposable getAdapterData;


	RecyclerView forecastRecyclerView;
	FloatingActionButton addFAB;
	MainFragmentAdapter adapter;

	public MainFragment() {
	}

	public static MainFragment newInstance() {
		return new MainFragment();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		adapter = new MainFragmentAdapter();
		forecastRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		forecastRecyclerView.setAdapter(adapter);

		DatabaseManager.clearDatabase_S()
				.subscribe(clear -> Log.i("Clear Database", clear.toString()));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getDataForAdapter();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {

		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		forecastRecyclerView = view.findViewById(R.id.brief_forecast);
		addFAB = view.findViewById(R.id.FAB_add);

		addFAB.setOnClickListener(v -> Log.i("FAB", "Pressed"));
	}

	@Override
	public void onStop() {
		super.onStop();

		if (getAdapterData != null && !getAdapterData.isDisposed())
			getAdapterData.dispose();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (getAdapterData != null && !getAdapterData.isDisposed())
			getAdapterData.dispose();
	}

	private void getDataForAdapter() {

		getAdapterData = RFRequest.startGettingData()
				.subscribe(
						adapter::add,
						Throwable::printStackTrace,
						() -> Log.i("Data for adapter", "Completed"));
	}
}
