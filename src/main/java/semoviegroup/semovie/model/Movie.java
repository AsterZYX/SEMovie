package semoviegroup.semovie.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.SortedMap;

import lombok.Data;

@Data
public class Movie {
	String movieid;// 唯一标识ID
	String state;// 正在上映 即将上映 经典影片
	String maoyanrating;// 猫眼评分
	String doubanrating;// 豆瓣评分
	String wanting;// 想看人数
	String sale;// 票房
	String runtime;// 持续时间
	String language;// 对白使用的语言
	String title;// 名称
	String poster;// 海报
	String writers;// 编剧列表
	String film_locations;// 拍摄地
	String rating_count;// 评分人数
	String plot_simple;// 剧情概要
	String year;// 拍摄年代
	String country;// 拍摄国家
	String type;// 影片类型 （大多是null 不是很懂这个字段的意义是什么）
	String release_date;// 上映时间
	String also_known_as;// 其它名称

	ArrayList<String> genres;// 分类 如：(动作/科幻/冒险)
	ArrayList<Worker> directorList;// 导演列表
	ArrayList<Worker> actorList;// 演员图像
	ArrayList<String> picList;// 电影图集

	SortedMap<Cinema, Double> maoyancinemaList;// 影院信息,价格
	SortedMap<Cinema, Double> doubancinemaList;// 影院信息,价格
	SortedMap<Cinema, Double> taoppcinemaList;// 影院信息,价格

	public String getMovieid() {
		return movieid;
	}

	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMaoyanrating() {
		return maoyanrating;
	}

	public void setMaoyanrating(String maoyanrating) {
		this.maoyanrating = maoyanrating;
	}

	public String getDoubanrating() {
		return doubanrating;
	}

	public void setDoubanrating(String doubanrating) {
		this.doubanrating = doubanrating;
	}

	public String getWanting() {
		return wanting;
	}

	public void setWanting(String wanting) {
		this.wanting = wanting;
	}

	public String getSale() {
		return sale;
	}

	public void setSale(String sale) {
		this.sale = sale;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
	}

	public String getWriters() {
		return writers;
	}

	public void setWriters(String writers) {
		this.writers = writers;
	}

	public String getFilm_locations() {
		return film_locations;
	}

	public void setFilm_locations(String film_locations) {
		this.film_locations = film_locations;
	}

	public String getRating_count() {
		return rating_count;
	}

	public void setRating_count(String rating_count) {
		this.rating_count = rating_count;
	}

	public String getPlot_simple() {
		return plot_simple;
	}

	public void setPlot_simple(String plot_simple) {
		this.plot_simple = plot_simple;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRelease_date() {
		return release_date;
	}

	public void setRelease_date(String release_date) {
		this.release_date = release_date;
	}

	public String getAlso_known_as() {
		return also_known_as;
	}

	public void setAlso_known_as(String also_known_as) {
		this.also_known_as = also_known_as;
	}

	public ArrayList<String> getGenres() {
		return genres;
	}

	public void setGenres(ArrayList<String> genres) {
		this.genres = genres;
	}

	public ArrayList<Worker> getDirectorList() {
		return directorList;
	}

	public void setDirectorList(ArrayList<Worker> directorList) {
		this.directorList = directorList;
	}

	public ArrayList<Worker> getActorList() {
		return actorList;
	}

	public void setActorList(ArrayList<Worker> actorList) {
		this.actorList = actorList;
	}

	public ArrayList<String> getPicList() {
		return picList;
	}

	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}

	public SortedMap<Cinema, Double> getMaoyancinemaList() {
		return maoyancinemaList;
	}

	public void setMaoyancinemaList(SortedMap<Cinema, Double> maoyancinemaList) {
		this.maoyancinemaList = maoyancinemaList;
	}

	public SortedMap<Cinema, Double> getDoubancinemaList() {
		return doubancinemaList;
	}

	public void setDoubancinemaList(SortedMap<Cinema, Double> doubancinemaList) {
		this.doubancinemaList = doubancinemaList;
	}

	public SortedMap<Cinema, Double> getTaoppcinemaList() {
		return taoppcinemaList;
	}

	public void setTaoppcinemaList(SortedMap<Cinema, Double> taoppcinemaList) {
		this.taoppcinemaList = taoppcinemaList;
	}

	public Movie() {
	}

	public Movie(String movieid, String state, String maoyanrating, String doubanrating, String wanting, String sale,
			String runtime, String language, String title, String poster, String writers, String film_locations,
			String rating_count, String plot_simple, String year, String country, String type, String release_date,
			String also_known_as, ArrayList<String> genres, ArrayList<Worker> directorList, ArrayList<Worker> actorList,
			ArrayList<String> picList, SortedMap<Cinema, Double> maoyancinemaList,
			SortedMap<Cinema, Double> doubancinemaList, SortedMap<Cinema, Double> taoppcinemaList) {
		super();
		this.movieid = movieid;
		this.state = state;
		this.maoyanrating = maoyanrating;
		this.doubanrating = doubanrating;
		this.wanting = wanting;
		this.sale = sale;
		this.runtime = runtime;
		this.language = language;
		this.title = title;
		this.poster = poster;
		this.writers = writers;
		this.film_locations = film_locations;
		this.rating_count = rating_count;
		this.plot_simple = plot_simple;
		this.year = year;
		this.country = country;
		this.type = type;
		this.release_date = release_date;
		this.also_known_as = also_known_as;
		this.genres = genres;
		this.directorList = directorList;
		this.actorList = actorList;
		this.picList = picList;
		this.maoyancinemaList = maoyancinemaList;
		this.doubancinemaList = doubancinemaList;
		this.taoppcinemaList = taoppcinemaList;
	}

	@Override
	public String toString() {
		return "{\"movieid\":\"" + movieid + "\",\"state\":\"" + state + "\",\"maoyanrating\":\"" + maoyanrating
				+ "\",\"doubanrating\":\"" + doubanrating + "\",\"wanting\":\"" + wanting + "\",\"sale\":\"" + sale
				+ "\",\"runtime\":\"" + runtime + "\",\"language\":\"" + language + "\",\"title\":\"" + title
				+ "\",\"poster\":\"" + poster + "\",\"writers\":\"" + writers + "\",\"film_locations\":\""
				+ film_locations + "\",\"rating_count\":\"" + rating_count + "\",\"plot_simple\":\"" + plot_simple
				+ "\",\"year\":\"" + year + "\",\"country\":\"" + country + "\",\"type\":\"" + type
				+ "\",\"release_date\":\"" + release_date + "\",\"also_known_as\":\"" + also_known_as + "\",\"genres\":"
				+ genres + ",\"directorList\":" + directorList + ",\"actorList\":" + actorList + ",\"picList\":"
				+ picList + ",\"maoyancinemaList\":" + maoyancinemaList + ",\"doubancinemaList\":" + doubancinemaList
				+ ",\"taoppcinemaList\":" + taoppcinemaList + "}";
	}

}
