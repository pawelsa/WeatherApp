package com.example.weatherlib.project.Database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database( name = ForecastDB.NAME, version = ForecastDB.VERSION )
public class ForecastDB {
	
	public static final String NAME = "ForecastDB";
	public static final int VERSION = 1;
}
