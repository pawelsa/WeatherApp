package com.example.weatherlib.project.Tools;

import java.util.List;

import io.reactivex.exceptions.CompositeException;

public class ExceptionTester {
	
	
	public static boolean matchesException(Throwable throwable, Class<?> cls) {
		if ( throwable instanceof CompositeException ) {
			List<Throwable> throwableList = (( CompositeException ) throwable).getExceptions();
			for ( Throwable t : throwableList ) {
				if ( cls.isInstance(t) ) {
					return true;
				}
			}
		} else {
			return cls.isInstance(throwable);
		}
		return false;
	}
}
