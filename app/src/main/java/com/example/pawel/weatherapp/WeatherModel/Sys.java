package com.example.pawel.weatherapp.WeatherModel;

import com.example.pawel.weatherapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class, name = "sysTable", allFields = true)
public class Sys extends BaseModel {
	public int type;

	@PrimaryKey
	public int id;
	public double message;
	public String country;
	public int sunrise;
	public int sunset;
}
