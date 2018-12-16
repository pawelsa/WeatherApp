package com.example.weatherlib.project;

import android.location.Location;

import com.example.weatherlib.project.Database.ForecastDB;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;


@Table(database = ForecastDB.class, name = "testLocation", allFields = true)
public class TestLocation extends BaseModel {
    
    @PrimaryKey(autoincrement = true)
    public int IDs;
    public double latitude;
    public double longitude;
    
    public TestLocation() {
    }
    
    public TestLocation(Location location) {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }
    
}
