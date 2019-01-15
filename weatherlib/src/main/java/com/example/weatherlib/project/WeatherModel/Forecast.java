package com.example.weatherlib.project.WeatherModel;

import com.example.weatherlib.project.Database.ForecastDB;
import com.example.weatherlib.project.Main.IsDownloaded;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.OneToMany;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.text.Collator;
import java.util.List;

@Table( database = ForecastDB.class, name = "forecastTable" )
public class Forecast
		extends BaseModel
		implements IsDownloaded {
	
	@Column
	@PrimaryKey
	public int ID;
	
	public List<CurrentWeather> list;
	
	@ForeignKey( saveForeignKeyModel = true )
	@Column
	public City city;
	
	public String downloadURL;
	
	@OneToMany( methods = OneToMany.Method.ALL, variableName = "list" )
	public List<CurrentWeather> oneToManyWeathers() {
		if ( list == null ) {
			list = SQLite.select()
					.from(CurrentWeather.class)
					.where(CurrentWeather_Table.forecast_ID.eq(this.ID))
					.queryList();
		}
		return list;
	}
	
	@Override
	public boolean save() {
		
		this.ID = city.id;
		boolean res = super.save();
		if ( list != null ) {
			for ( CurrentWeather s : list ) {
				s.setForecast(this);
				s.save();
			}
		}
		return res;
	}
	
	@Override
	public boolean delete() {
		boolean delete = super.delete();
		
		this.city.delete();
		
		return delete;
	}
	
	@Override
	public boolean isDownloaded() {
		return ID >= 0;
	}
	
	@Override
	public int hashCode() {
		super.hashCode();
		return ID;
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		Collator instance = Collator.getInstance();
		instance.setStrength(Collator.NO_DECOMPOSITION);
		if ( obj instanceof Forecast ) {
			Forecast other = ( Forecast ) obj;
			int equalName = instance.compare(this.city.name, other.city.name);
			result = this.ID == other.ID || equalName == 0;
		} else if ( obj instanceof String ) {
			String otherName = ( String ) obj;
			int equalName = instance.compare(this.city.name, otherName);
			result = equalName == 0;
		}
		return result;
	}
	
	
}
