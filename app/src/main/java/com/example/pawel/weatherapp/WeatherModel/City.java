package com.example.pawel.weatherapp.WeatherModel;

import com.example.pawel.weatherapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class, name = "cityName", allFields = true)
public class City extends BaseModel {

	@PrimaryKey
	public int id;
	public String country;

	public int population;
	public String name;


}
