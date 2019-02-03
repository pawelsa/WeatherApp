package com.example.pawel.weatherapp.android;

import com.example.pawel.weatherapp.WeatherModels.ForecastModel;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class EForecastDiffList
		extends DiffUtil.Callback {
	
	private List<ForecastModel> mOld;
	private List<ForecastModel> mNew;
	
	EForecastDiffList(List<ForecastModel> mOld, List<ForecastModel> mNew) {
		this.mOld = mOld;
		this.mNew = mNew;
	}
	
	@Override
	public int getOldListSize() {
		return mOld != null ? mOld.size() : 0;
	}
	
	@Override
	public int getNewListSize() {
		return mNew != null ? mNew.size() : 0;
	}
	
	@Override
	public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
		return mOld.get(oldItemPosition).equals(mNew.get(newItemPosition));
	}
	
	@Override
	public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
		boolean result = false;
		if ( mOld.get(oldItemPosition) != null && ! mOld.get(oldItemPosition).getWeatherList().isEmpty()
		     && mNew.get(newItemPosition).getWeatherList() != null
		     && ! mNew.get(newItemPosition).getWeatherList().isEmpty() ) {
			result =
					mOld.get(oldItemPosition).getWeatherList().get(0).getDt() == mNew.get(newItemPosition)
							.getWeatherList()
							.get(0)
							.getDt();
		}
		return result;
	}
}
