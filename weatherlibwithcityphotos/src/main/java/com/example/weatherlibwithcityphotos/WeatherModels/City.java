package com.example.weatherlibwithcityphotos.WeatherModels;

public class City {
	
	public int ID;
	
	public String country;
	
	public String name;
	
	public String cityImageUrl;
	
	public City() {
	}
	
	public City(com.example.weatherlib.project.WeatherModel.City city) {
		this.country = city.country;
		this.name = city.name;
		this.ID = city.id;
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