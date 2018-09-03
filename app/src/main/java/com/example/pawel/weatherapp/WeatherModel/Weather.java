package com.example.pawel.weatherapp.WeatherModel;

import com.example.pawel.weatherapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class, name = "weatherTable", allFields = true)
public class Weather extends BaseModel {

	@PrimaryKey
	public int mainID;

	public int id;
	public String main;
	public String description;
	public String icon;
}
