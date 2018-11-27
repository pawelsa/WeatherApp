
package com.example.getphotoforcity.PhotoModel;

import java.util.List;

public class Candidate {

	public String name;
	public List<Photo> photos = null;

	@Override
	public String toString() {
		if (photos == null || photos.isEmpty()) {
			return "NULL";
		}
		StringBuilder all = new StringBuilder();
		for (Photo photo : photos) {
			all.append(photo.photo_reference).append("   -   ");
		}
		return all.toString();
	}
}
