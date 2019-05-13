package semoviegroup.semovie.model;

import lombok.Data;

@Data
public class Movie {
	String movieid;
	String rating;
	String genres;
	String runtime;
	String language;
	String title;
	String poster;
	String writers;
	String film_locations;
	String directors;
	String rating_count;
	String actors;
	String plot_simple;
	String year;
	String country;
	String type;
	String release_date;
	String also_known_as;

	public Movie(String movieid, String rating, String genres, String runtime, String language, String title,
			String poster, String writers, String film_locations, String directors, String rating_count, String actors,
			String plot_simple, String year, String country, String type, String release_date, String also_known_as) {
		super();
		this.movieid = movieid;
		this.rating = rating;
		this.genres = genres;
		this.runtime = runtime;
		this.language = language;
		this.title = title;
		this.poster = poster;
		this.writers = writers;
		this.film_locations = film_locations;
		this.directors = directors;
		this.rating_count = rating_count;
		this.actors = actors;
		this.plot_simple = plot_simple;
		this.year = year;
		this.country = country;
		this.type = type;
		this.release_date = release_date;
		this.also_known_as = also_known_as;
	}

	@Override
	public String toString() {
		return "{\"movieid\":\"" + movieid + "\",\"rating\":\"" + rating + "\",\"genres\":\"" + genres
				+ "\",\"runtime\":\"" + runtime + "\",\"language\":\"" + language + "\",\"title\":\"" + title
				+ "\",\"poster\":\"" + poster + "\",\"writers\":\"" + writers + "\",\"film_locations\":\""
				+ film_locations + "\",\"directors\":\"" + directors + "\",\"rating_count\":\"" + rating_count
				+ "\",\"actors\":\"" + actors + "\",\"plot_simple\":\"" + plot_simple + "\",\"year\":\"" + year
				+ "\",\"country\":\"" + country + "\",\"type\":\"" + type + "\",\"release_date\":\"" + release_date
				+ "\",\"also_known_as\":\"" + also_known_as + "\"}";
	}

}
