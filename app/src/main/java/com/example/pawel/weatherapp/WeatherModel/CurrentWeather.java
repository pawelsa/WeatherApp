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
    
    @PrimaryKey(autoincrement = true)
	@Column
	public int id;
    
    public List<Weather> weatherList;

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
    
    @ForeignKey(stubbedRelationship = true)
    Forecast forecast;

	public CurrentWeather() {
	}
    
    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
    
    @OneToMany(methods = OneToMany.Method.ALL, variableName = "weatherList")
	public List<Weather> oneToManyWeathers() {
        if (weatherList == null) {
            weatherList = SQLite.select()
					.from(Weather.class)
                    .where(Weather_Table.currentWeather_id.eq(id))
					.queryList();
		}
        return weatherList;
	}

	public DatabaseWeather toDatabaseWeather(int cityID, String cityImageUrl) {
		DatabaseWeather databaseWeather = new DatabaseWeather();
		databaseWeather.dt = this.dt;
		databaseWeather.temp = this.main.temp;
		databaseWeather.pressure = this.main.pressure;
		databaseWeather.humidity = this.main.humidity;
		databaseWeather.temp_min = this.main.temp_min;
		databaseWeather.temp_max = this.main.temp_max;
		databaseWeather.id = this.id;
        databaseWeather.main = this.weatherList.get(0).main;
        databaseWeather.description = this.weatherList.get(0).description;
        databaseWeather.icon = this.weatherList.get(0).icon;
		databaseWeather.all = this.clouds.all;
		databaseWeather.speed = this.wind.speed;
		databaseWeather.deg = this.wind.deg;
		databaseWeather.dt_txt = this.dt_txt;
		databaseWeather.cityID = cityID;
		databaseWeather.cityImageUrl = cityImageUrl;

		return databaseWeather;
	}
    
    @Override
	public boolean save() {
        boolean save = super.save();
        for (Weather weather : weatherList) {
            weather.setCurrentWeather(this);
            weather.save();
        }
        return save;
    }
}
