package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.ForecastDB;
import com.example.weatherlib.project.Main.IsDownloaded;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table( database = ForecastDB.class, name = "cityName", allFields = true )
public class City
		extends BaseModel
		implements IsDownloaded {
	
	@PrimaryKey
	public int id;
	public String country;
	
	public int population;
	
	@PrimaryKey
	public String name;
	
	public City() {
	}
	
	public City(String name) {
		this.name = name;
	}
	
	@Override
	public boolean isDownloaded() {
		return country != null;
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		
		if ( obj instanceof City ) {
			City other = ( City ) obj;
			result = this.name.equals(other.name);
		}
		return result;
	}
}
