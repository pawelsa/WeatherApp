package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.MyDatabase;
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
    
    public String cityImageUrl;
    
    public void setCityImageUrl(String cityImageUrl) {
        this.cityImageUrl = cityImageUrl;
    }
	
	public City() {
	}
	
	public City(String name) {
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		
		if ( obj instanceof City ) {
			City other = ( City ) obj;
			result = this.name.equals(other.name);
		}
		return result;
	}
}
