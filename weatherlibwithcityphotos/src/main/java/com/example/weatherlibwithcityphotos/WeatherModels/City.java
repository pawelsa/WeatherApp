package com.example.weatherlibwithcityphotos.WeatherModels;

public class City {
	
	private int ID;
	
	private String country;
	
	private String name;
	
	private String photoReference;
	
	public City(com.example.weatherlib.project.WeatherModel.City city) {
		this.country = city.country;
		this.name = city.name;
		this.ID = city.id;
	}
	
	public City(com.example.weatherlib.project.WeatherModel.City city, String photoReference) {
		this(city);
		this.photoReference = photoReference;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPhotoReference() {
		return photoReference;
	}
	
	public void setPhotoReference(String photoReference) {
		this.photoReference = photoReference;
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