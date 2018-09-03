package com.example.pawel.weatherapp.Database;


import com.example.pawel.weatherapp.WeatherModel.City;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = MyDatabase.class, name = "placesTable")
public class Places extends BaseModel {

	@PrimaryKey
	@Column
	public int cityID;

	@Column
	public String name;

	public Places() {
	}

	public Places(City city) {

		this.name = city.name;
		this.cityID = city.id;
	}

	@Override
	public boolean equals(Object obj) {
		super.equals(obj);

		boolean result = false;

		if (obj instanceof Places) {
			Places other = (Places) obj;
			result = this.name.equals(other.name);
		}
		return result;
	}
}
