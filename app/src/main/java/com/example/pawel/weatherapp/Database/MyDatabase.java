package com.example.pawel.weatherapp.Database;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION)
public class MyDatabase {

	public static final String NAME = "MyDatabaseDB";
	public static final int VERSION = 4;
}
