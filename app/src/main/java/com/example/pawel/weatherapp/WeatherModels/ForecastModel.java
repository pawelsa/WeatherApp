package com.example.pawel.weatherapp.WeatherModels;

import com.example.weatherlibwithcityphotos.ForecastWithPhoto;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class ForecastModel {

    private boolean downloaded = false;
    private List<HourlyWeather> weatherList;
    private City city;


    public ForecastModel() {
    }

    public ForecastModel(ForecastModel forecastModel) {
        updateModelWithNewData(forecastModel);
    }

    public ForecastModel(ForecastWithPhoto forecast) {
        this.city = new City(forecast.getCity(), forecast.getPhotoReference());
        this.weatherList = convertWeatherList(forecast.getWeatherList());
        this.downloaded = forecast.isDownloaded();
    }

    public void updateModelWithNewData(ForecastModel forecastModel) {
        this.city = forecastModel.city;
        this.weatherList = forecastModel.getWeatherList();
        this.downloaded = forecastModel.isDownloaded();
    }

    public int getCityID() {
        return this.city.getID();
    }

    public String getCityName() {
        return this.city.getName();
    }

    public String getPhotoReference() {
        return city.getPhotoReference();
    }

    public boolean isDownloaded() {
        return downloaded;
    }

    protected List<HourlyWeather> getWeatherList() {
        return weatherList;
    }

    private List<HourlyWeather> convertWeatherList(
            List<com.example.weatherlibwithcityphotos.WeatherModels.HourlyWeather> list) {

        List<HourlyWeather> newList = new ArrayList<>();
        if (list != null) {
            for (com.example.weatherlibwithcityphotos.WeatherModels.HourlyWeather item : list) {
                newList.add(new HourlyWeather(item));
            }
        }
        return newList;
    }


    @Override
    public int hashCode() {
        super.hashCode();
        return city.getID();
    }

    @Override
    public boolean equals(Object obj) {
        super.equals(obj);

        boolean result = false;
        Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);
        if (obj instanceof ForecastModel) {
            ForecastModel other = (ForecastModel) obj;
            int equalName = instance.compare(this.city.getName(), other.getCityName());
            result = this.city.getID() == other.city.getID() || equalName == 0;
        } else if (obj instanceof String) {
            String otherName = (String) obj;
            int equalName = instance.compare(this.city.getName(), otherName);
            result = equalName == 0;
        }
        return result;
    }

}
