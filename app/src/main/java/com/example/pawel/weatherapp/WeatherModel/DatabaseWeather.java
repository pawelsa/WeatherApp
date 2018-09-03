package com.example.pawel.weatherapp.WeatherModel;


import com.example.pawel.weatherapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class, name = "databaseWeatherTable", allFields = true)
public class DatabaseWeather extends BaseModel {

	@PrimaryKey(autoincrement = true)
	public int mainID;

	public int cityID;

	public String dt_txt;

	public double dt;
	public double temp;
	public double pressure;
	public double humidity;
	public double temp_min;
	public double temp_max;

	public int id;
	public String main;
	public String description;
	public String icon;

	public double all;
	public double speed;
	public double deg;


}
