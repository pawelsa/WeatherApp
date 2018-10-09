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

@Table( database = MyDatabase.class, name = "currentWeatherTable" )
public class CurrentWeather
		extends BaseModel {
	
	@PrimaryKey( autoincrement = true )
	@Column
	public int id;
	
	public List<Weather> weather;
	
	@ForeignKey( saveForeignKeyModel = true )
	@Column
	public Main main;
	
	@ForeignKey( saveForeignKeyModel = true )
	@Column
	public Wind wind;
	
	@ForeignKey( saveForeignKeyModel = true )
	@Column
	public Clouds clouds;
	
	@Column
	public double dt;
	
	@ForeignKey( saveForeignKeyModel = true )
	@Column
	public Sys sys;
	
	@Column
	public String dt_txt;
	
	@ForeignKey( stubbedRelationship = true )
	Forecast forecast;
	
	public CurrentWeather() {
	}
	
	public void setForecast(Forecast forecast) {
		this.forecast = forecast;
	}
	
	@OneToMany( methods = OneToMany.Method.ALL, variableName = "weather" )
	public List<Weather> oneToManyWeathers() {
		if ( weather == null ) {
			weather = SQLite.select()
					.from(Weather.class)
					.where(Weather_Table.currentWeather_id.eq(id))
					.queryList();
		}
		return weather;
	}
	
	@Override
	public boolean save() {
		boolean save = super.save();
		for ( Weather wea : weather ) {
			wea.setCurrentWeather(this);
			wea.save();
		}
		return save;
	}
}
