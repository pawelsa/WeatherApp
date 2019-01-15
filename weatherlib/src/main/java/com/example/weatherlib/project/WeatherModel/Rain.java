package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.ForecastDB;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


@Table( database = ForecastDB.class, name = "rainTable", allFields = true )
public class Rain
		extends BaseModel {
	
	@PrimaryKey( autoincrement = true )
	public int rainID;
	
	@SerializedName( "3h" )
	@Expose
	public double _3h;
	
}
