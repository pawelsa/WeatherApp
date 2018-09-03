package com.example.pawel.weatherapp.WeatherModel;

import com.example.pawel.weatherapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class, name = "mainTable", allFields = true)
public class Main extends BaseModel {

	@PrimaryKey
	public double temp;
	public double pressure;
	public double humidity;
	public double temp_min;
	public double temp_max;
}
