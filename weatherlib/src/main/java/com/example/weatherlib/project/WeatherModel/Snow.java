package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.ForecastDB;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table( database = ForecastDB.class, name = "snowTable", allFields = true )
public class Snow
		extends BaseModel {
	
	@PrimaryKey( autoincrement = true )
	public int snowID;
	
	@SerializedName( "3h" )
	@Expose
	public double _3h;
}
