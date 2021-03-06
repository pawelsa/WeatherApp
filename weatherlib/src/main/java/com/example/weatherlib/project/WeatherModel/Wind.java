package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.ForecastDB;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table( database = ForecastDB.class, name = "windTable", allFields = true )
public class Wind extends BaseModel {

	@PrimaryKey
	public double speed;

	@PrimaryKey
	public double deg;
}
