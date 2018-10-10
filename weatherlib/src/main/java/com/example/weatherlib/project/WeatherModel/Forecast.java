package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = MyDatabase.class, name = "forecastTable")
public class Forecast extends BaseModel {

	@Column
	@PrimaryKey(autoincrement = true)
	public int ID;

	public List<CurrentWeather> list;

	@ForeignKey(saveForeignKeyModel = true)
	@Column
	public City city;
	
	public String downloadURL;

	@OneToMany(methods = OneToMany.Method.ALL, variableName = "list")
	public List<CurrentWeather> oneToManyWeathers() {
		if (list == null) {
			list = SQLite.select()
					.from(CurrentWeather.class)
					.where(CurrentWeather_Table.forecast_ID.eq(this.ID))
					.queryList();
		}
		return list;
	}

	@Override
	public boolean save() {
		boolean res = super.save();
		if (list != null) {
			for (CurrentWeather s : list) {
				s.setForecast(this);
				s.save();
			}
		}
		return res;
	}
	
	public void setCityImageUrl(String url) {
		this.city.setCityImageUrl(url);
	}
	
	@Override
	public int hashCode() {
		super.hashCode();
		return ID * city.id * city.population;
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		
		if ( obj instanceof Forecast ) {
			Forecast other = ( Forecast ) obj;
			result = this.city.id == other.city.id && this.list.get(0).dt == other.list.get(0).dt;
		}
		return result;
	}
}
