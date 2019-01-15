package com.example.pawel.weatherapp.android;

import com.example.weatherlibwithcityphotos.EForecast;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;

public class EForecastDiffList
		extends DiffUtil.Callback {
	
	private List<EForecast> mOld;
	private List<EForecast> mNew;
	
	EForecastDiffList(List<EForecast> mOld, List<EForecast> mNew) {
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
		if ( mOld.get(oldItemPosition).weatherList != null && mOld.get(oldItemPosition).weatherList.isEmpty() && mNew.get(
				newItemPosition).weatherList != null && mNew.get(newItemPosition).weatherList.isEmpty() ) {
			result =
					mOld.get(oldItemPosition).weatherList.get(0).getDt() == mNew.get(newItemPosition).weatherList.get(0)
							.getDt();
		}
		return result;
	}
}
