package com.example.weatherlib.project.Database;

import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table( database = MyDatabase.class, name = "citySaveTable", allFields = true )
public class CitySave
		extends BaseModel {
	
	@PrimaryKey
	public String cityName;
	public boolean downloaded = false;
	
	public CitySave() {
	}
	
	public CitySave(String cityName) {
		this.cityName = cityName;
	}
	
	public CitySave(String cityName, boolean downloaded) {
		this.cityName = cityName;
		this.downloaded = downloaded;
	}
}
