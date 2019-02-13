package com.example.pawel.weatherapp.android.main;

import com.example.pawel.weatherapp.ForecastToView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class ForecastDiffUtil
		extends DiffUtil.Callback {
	
	private List<ForecastToView> mOld;
	private List<ForecastToView> mNew;
	
	ForecastDiffUtil(List<ForecastToView> mOld, List<ForecastToView> mNew) {
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
		if ( mOld.get(oldItemPosition) != null
		     && ! mOld.get(oldItemPosition).getWeatherList().isEmpty()
		     && mNew.get(newItemPosition).getWeatherList() != null
		     && ! mNew.get(newItemPosition).getWeatherList().isEmpty() ) {
			result =
					mOld.get(oldItemPosition).getWeatherList().get(0).getDt() ==
					mNew.get(newItemPosition).getWeatherList().get(0).getDt();
		}
		return result;
	}
	
	@Nullable
	@Override
	public Object getChangePayload(int oldItemPosition, int newItemPosition) {
		return super.getChangePayload(oldItemPosition, newItemPosition);
	}
}
