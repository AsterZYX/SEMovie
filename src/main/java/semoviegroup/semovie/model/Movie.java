package semoviegroup.semovie.model;

import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class Movie {
	String movieid;// 唯一标识ID
	String rating;// 得分（有可能是-1）
	String genres;//分类 如：(动作/科幻/冒险)
	String runtime;// 持续时间
	String language;// 对白使用的语言
	String title;// 名称
	String poster;// 海报
	String writers;// 编剧列表
	String film_locations;// 拍摄地
	String directors;//导演列表
	String rating_count;// 评分人数
	String actors;// 演员列表
	String plot_simple;//剧情概要
	String year;// 拍摄年代
	String country;// 拍摄国家
	String type;// 影片类型  （大多是null 不是很懂这个字段的意义是什么）
	String release_date;//上映时间
	String also_known_as;// 其它名称
	String wanting;//想看人数
	String sale;//票房
	String doubanrating;//豆瓣评分
	
	ArrayList<String> picList;//电影图集
	ArrayList<Worker> workerPicList;//电影人员图像
	HashMap<Cinema,Double> cinemaList;//影院信息,价格
	public String getMovieid() {
		return movieid;
	}
	public void setMovieid(String movieid) {
		this.movieid = movieid;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
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
	public String getDirectors() {
		return directors;
	}
	public void setDirectors(String directors) {
		this.directors = directors;
	}
	public String getRating_count() {
		return rating_count;
	}
	public void setRating_count(String rating_count) {
		this.rating_count = rating_count;
	}
	public String getActors() {
		return actors;
	}
	public void setActors(String actors) {
		this.actors = actors;
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
	public String getDoubanrating() {
		return doubanrating;
	}
	public void setDoubanrating(String doubanrating) {
		this.doubanrating = doubanrating;
	}
	public ArrayList<String> getPicList() {
		return picList;
	}
	public void setPicList(ArrayList<String> picList) {
		this.picList = picList;
	}
	public ArrayList<Worker> getWorkerPicList() {
		return workerPicList;
	}
	public void setWorkerPicList(ArrayList<Worker> workerPicList) {
		this.workerPicList = workerPicList;
	}
	public HashMap<Cinema, Double> getCinemaList() {
		return cinemaList;
	}
	public void setCinemaList(HashMap<Cinema, Double> cinemaList) {
		this.cinemaList = cinemaList;
	}
	@Override
	public String toString() {
		return "{\"movieid\":\"" + movieid + "\",\"rating\":\"" + rating + "\",\"genres\":\"" + genres
				+ "\",\"runtime\":\"" + runtime + "\",\"language\":\"" + language + "\",\"title\":\"" + title
				+ "\",\"poster\":\"" + poster + "\",\"writers\":\"" + writers + "\",\"film_locations\":\""
				+ film_locations + "\",\"directors\":\"" + directors + "\",\"rating_count\":\"" + rating_count
				+ "\",\"actors\":\"" + actors + "\",\"plot_simple\":\"" + plot_simple + "\",\"year\":\"" + year
				+ "\",\"country\":\"" + country + "\",\"type\":\"" + type + "\",\"release_date\":\"" + release_date
				+ "\",\"also_known_as\":\"" + also_known_as + "\",\"wanting\":\"" + wanting + "\",\"sale\":\"" + sale
				+ "\",\"doubanrating\":\"" + doubanrating + "\",\"picList\":" + picList + ",\"workerPicList\":"
				+ workerPicList + ",\"cinemaList\":" + cinemaList + "}";
	}
	public Movie(String movieid, String rating, String genres, String runtime, String language, String title,
			String poster, String writers, String film_locations, String directors, String rating_count, String actors,
			String plot_simple, String year, String country, String type, String release_date, String also_known_as,
			String wanting, String sale, String doubanrating, ArrayList<String> picList,
			ArrayList<Worker> workerPicList, HashMap<Cinema, Double> cinemaList) {
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
		this.wanting = wanting;
		this.sale = sale;
		this.doubanrating = doubanrating;
		this.picList = picList;
		this.workerPicList = workerPicList;
		this.cinemaList = cinemaList;
	}
	
	
	
	 /*
	      {
            "movieid": "30803",
            "rating": "-1",
            "genres": "动作/剧情/科幻",
            "runtime": "103 min / USA:100 min",
            "language": "日语",
            "title": "哥斯拉大战基多拉国王",
            "poster": "http://v.juhe.cn/movie/img?85308",
            "writers": "大森一树",
            "film_locations": "日本",
            "directors": "大森一树",
            "rating_count": "18",
            "actors": "原田贵和子 Kiwako Harada,萨摩剑八郎 Kenpachiro Satsuma,山村聪 Sô Yamamura,Yasunori Yuge",
            "plot_simple": "一架UFO在关西地区上空出现并且降落在富士山脚。这UFO原来是时光机器，它载著来自23世纪的三位代表来到20世纪末的日本，他们的任务是防止日本在下个世纪因为哥斯拉而亡国。他们希望能回到过去，在水爆大怪兽哥斯拉还未受到放射线影响变..",
            "year": "1991",
            "country": "日本",
            "type": "null",
            "release_date": "19911214",
            "also_known_as": ""
          }
	 */

	
	
	
	
}
