package com.example.getphotoforcity;

import com.example.getphotoforcity.PhotoModel.PhotoResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCalls {
	
	@GET( "json?inputtype=textquery&fields=photos,id,name" )
	Single<PhotoResponse> getPhotoResponse(@Query( "input" ) String cityName,
	                                       @Query( "key" ) String APIKey);
}
