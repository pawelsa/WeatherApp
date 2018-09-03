package com.example.pawel.weatherapp.WeatherModel;

import com.example.pawel.weatherapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;

@Table(database = MyDatabase.class, name = "currentWeatherTable")
public class CurrentWeather extends BaseModel {

	@PrimaryKey
	@Column
	public int id;

	public List<Weather> weather;

	@ForeignKey(saveForeignKeyModel = true)
	@Column
	public Main main;

	@ForeignKey(saveForeignKeyModel = true)
	@Column
	public Wind wind;

	@ForeignKey(saveForeignKeyModel = true)
	@Column
	public Clouds clouds;

	@Column
	public double dt;

	@ForeignKey(saveForeignKeyModel = true)
	@Column
	public Sys sys;

	@Column
	public String dt_txt;

	public CurrentWeather() {
	}

	@OneToMany(methods = OneToMany.Method.ALL, variableName = "weather")
	public List<Weather> oneToManyWeathers() {
		if (weather == null) {
			weather = SQLite.select()
					.from(Weather.class)
					.where(Weather_Table.mainID.eq((int) dt))
					.queryList();
		}
		return weather;
	}

	public DatabaseWeather toDatabaseWeather(int cityID) {
		DatabaseWeather databaseWeather = new DatabaseWeather();
		databaseWeather.dt = this.dt;
		databaseWeather.temp = this.main.temp;
		databaseWeather.pressure = this.main.pressure;
		databaseWeather.humidity = this.main.humidity;
		databaseWeather.temp_min = this.main.temp_min;
		databaseWeather.temp_max = this.main.temp_max;
		databaseWeather.id = this.id;
		databaseWeather.main = this.weather.get(0).main;
		databaseWeather.description = this.weather.get(0).description;
		databaseWeather.icon = this.weather.get(0).icon;
		databaseWeather.all = this.clouds.all;
		databaseWeather.speed = this.wind.speed;
		databaseWeather.deg = this.wind.deg;
		databaseWeather.dt_txt = this.dt_txt;
		databaseWeather.cityID = cityID;

		return databaseWeather;
	}

/*	@Override
	public boolean save() {
		boolean result = super.save();

		if (main!=null)
			main.save();

		if (wind!=null)
			wind.save();

		if (clouds!=null)
			clouds.save();

		if (sys!=null)
			sys.save();


		return result;
	}*/
}
