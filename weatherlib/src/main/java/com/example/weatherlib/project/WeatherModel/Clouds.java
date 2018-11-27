package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.ForecastDB;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table( database = ForecastDB.class, name = "cloudsTable", allFields = true )
public class Clouds extends BaseModel {

	@PrimaryKey
	public double all;
}
