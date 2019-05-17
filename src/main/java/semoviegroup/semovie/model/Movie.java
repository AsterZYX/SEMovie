package semoviegroup.semovie.model;

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
	String doubanrating；//豆瓣评分
	
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
	
	
	
	
	public Movie(String movieid, String rating, String genres, String runtime, String language, String title,
			String poster, String writers, String film_locations, String directors, String rating_count, String actors,
			String plot_simple, String year, String country, String type, String release_date, String also_known_as,String wanting，String sale，String doubanrating) {
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
		this.wanting=wanting;
		this.sale=sale;
		this.doubanrating=doubanrating;
	}

	@Override
	public String toString() {
		return "{\"movieid\":\"" + movieid + "\",\"rating\":\"" + rating + "\",\"genres\":\"" + genres
				+ "\",\"runtime\":\"" + runtime + "\",\"language\":\"" + language + "\",\"title\":\"" + title
				+ "\",\"poster\":\"" + poster + "\",\"writers\":\"" + writers + "\",\"film_locations\":\""
				+ film_locations + "\",\"directors\":\"" + directors + "\",\"rating_count\":\"" + rating_count
				+ "\",\"actors\":\"" + actors + "\",\"plot_simple\":\"" + plot_simple + "\",\"year\":\"" + year
				+ "\",\"country\":\"" + country + "\",\"type\":\"" + type + "\",\"release_date\":\"" + release_date
				+ "\",\"also_known_as\":\"" + also_known_as + "\",\"wanting\":\"" + wanting + "\",\"sale\":\"" + sale + "\",\"doubanrating\":\"" + doubanrating + "\"}";
	}

}
