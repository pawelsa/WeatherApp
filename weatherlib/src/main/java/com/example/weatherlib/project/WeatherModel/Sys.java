package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.MyDatabase;
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
