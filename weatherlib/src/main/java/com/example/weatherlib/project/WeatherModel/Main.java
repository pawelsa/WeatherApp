package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.MyDatabase;
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
