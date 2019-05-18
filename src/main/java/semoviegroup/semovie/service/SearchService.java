package semoviegroup.semovie.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;
import semoviegroup.semovie.model.Cinema;
import semoviegroup.semovie.model.Movie;
import semoviegroup.semovie.model.Worker;
import semoviegroup.semovie.vo.ResultVO;

@Service
public class SearchService {
	public static final String DEF_CHATSET = "UTF-8";
	public static final int DEF_CONN_TIMEOUT = 30000;
	public static final int DEF_READ_TIMEOUT = 30000;
	public static String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";

	// 配置您申请的KEY
	public static final String APPKEY = "71842a983a7368e42147b4898976001d";

	public static void main(String[] args) {
		SearchService ss = new SearchService();
		ss.searchMovieById("248172", "复仇者联盟4：终局之战");
	}

	// 1.按关键字检索影片信息
	// showType=state catId=type sourceId=region yearId=time sortId=sort
	// size=30
	public ResultVO<List<Movie>> abstractsearch(String state, String type, String region, String time, String sort,
			Integer size, Integer currentPage) {
		String URL = "https://maoyan.com/films?showType=" + state + "&catId=" + type + "&sourceId=" + region
				+ "&yearId=" + time + "&sortId=" + sort + "&offset=" + (currentPage - 1) * size;
		SearchClient sc = new SearchClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			String loc = sc.search(state, type, region, time, sort, size, currentPage);
			SAXReader reader = new SAXReader();
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();
				/*
				 * List<Attribute> attributeList = child.attributes(); for (Attribute attr :
				 * attributeList) { System.out.println(attr.getName() + ": " + attr.getValue());
				 * }
				 */
				List<Element> elementList = child.elements();
				movie.setPoster(elementList.get(0).getText());
				movie.setTitle(elementList.get(1).getText());
				movie.setMaoyanrating(elementList.get(2).getText());
				movie.setMovieid(elementList.get(3).getText());
				movielist.add(movie);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		return vo;
	}

	public ResultVO<Movie> searchMovieById(String movieid, String moviename) {
		/*
		 * String URL = "https://maoyan.com/films/" + movieid;
		 * 
		 * // 找到title 找到豆瓣电影id String douban =
		 * "https://movie.douban.com/subject_search?search_text=???&cat=1002"; String
		 * doubanid = "";
		 */
		String result = null;
		String url = "http://v.juhe.cn/movie/index";// 请求接口地址
		Map params = new HashMap();// 请求参数
		params.put("title", moviename);// 需要检索的影片标题,utf8编码的urlencode
		params.put("smode", "");// <foncolor=red>是否精确查找，精确:1 模糊:0 默认1</font>
		params.put("pagesize", "");// <font color=red>每次返回条数，默认20,最大50</font>
		params.put("offset", "");// <font color=red>偏移量，默认0,最大760</font>
		params.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
		params.put("dtype", "");// 返回数据的格式,xml/json，默认json
		String s = "";
		try {
			result = net(url, params, "GET");
			JSONObject object = JSONObject.fromObject(result);
			if (object.getInt("error_code") == 0) {
				System.out.println(object.get("result"));
				s = object.get("result") + "";
				s = s.substring(1, s.length() - 1);
			} else {
				System.out.println(object.get("error_code") + ":" + object.get("reason"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// movieid maoyanrating genres runtime language
		// title poster writers film_locations directors
		// rating_count actors plot_simple year country
		// type release_date also_known_as

		// 猫眼rating假的； 豆瓣rating还没有爬； 想看人数还没有爬 先用movieid代替 ; 票房暂用movieid平方的前两位加小数点代替；
		// 导演 演员 图集 用python去获得
		// 涉及购票的用下一个方法
		Map<String, String> map = new Gson().fromJson(s, new TypeToken<HashMap<String, String>>() {
		}.getType());
		Movie movie = new Movie(movieid, "", map.get("rating"), "-1", map.get("movieid"), "", map.get("runtime"),
				map.get("language"), map.get("title"), map.get("poster"), map.get("writers"), map.get("film_locations"),
				map.get("rating_count"), map.get("plot_simple"), map.get("year"), map.get("country"), map.get("type"),
				map.get("release_date"), map.get("also_known_as"), null, null, null, null, null, null);

		if (map.get("rating").equals("-1")) {
			movie.setState("即将上映");
			movie.setMaoyanrating("暂无");
		} else {
			movie.setState("经典电影"); // 经典电影和正在热映无法分辨
		}

		int sale = Integer.parseInt(map.get("movieid"));
		long sales = sale * sale;
		String s1 = sales + "";
		String s2 = s1.charAt(0) + "." + s1.charAt(1)+"亿";
		movie.setSale(s2);

		ArrayList<String> genres = new ArrayList<String>();
		String gen = map.get("genres");
		String[] gens = gen.split("/");
		for (int i = 0; i < gens.length; i++) {
			genres.add(gens[i]);
		}
		movie.setGenres(genres);

		SearchClient sc = new SearchClient();
		try {
			String loc = sc.searchByName(movieid, moviename);
			SAXReader reader = new SAXReader();
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();

			ArrayList<Worker> directorList = new ArrayList<Worker>();
			ArrayList<Worker> actorList = new ArrayList<Worker>();
			ArrayList<String> picList = new ArrayList<String>();
			for (Element child : childElements) {

				List<Element> cchildElements = child.elements();
				for (Element cchild : cchildElements) {
					List<Attribute> attributeList = cchild.attributes();
					/*
					 * for (Attribute attr : attributeList) { System.out.println(attr.getName() +
					 * ": " + attr.getValue()); }
					 */
					List<Element> ElementList = cchild.elements();
					Worker w = new Worker();
					if (ElementList.size() > 1 && ElementList.get(1).getText().equals("director")) {
						w.setIdentity("director");
						w.setImg(ElementList.get(2).getText());
						w.setName(ElementList.get(0).getText());
						w.setRole(ElementList.get(3).getText());
						directorList.add(w);
					} else if (ElementList.size() > 1 && ElementList.get(1).getText().equals("actor")) {
						w.setIdentity("actor");
						w.setImg(ElementList.get(2).getText());
						w.setName(ElementList.get(0).getText());
						w.setRole(ElementList.get(3).getText());
						actorList.add(w);
					} else {
						picList.add(ElementList.get(0).getText());
					}

				}

			}
			movie.setActorList(actorList);
			movie.setDirectorList(directorList);
			movie.setPicList(picList);
			
			
			
			String douBanRating = sc.searchDoubanRatingByName(moviename);
			movie.setDoubanrating(douBanRating);
			
			
			
			
			
			System.out.println(movie.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResultVO(0, "", movie);
	}

	public ResultVO<Movie> getCinemaTicketsById(Integer size, Integer currentPage, String movieId) {

		return null;

	}

	////////////////////////////
	/**
	 *
	 * @param strUrl
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @param method
	 *            请求方法
	 * @return 网络请求字符串
	 * @throws Exception
	 */
	public static String net(String strUrl, Map params, String method) throws Exception {
		HttpURLConnection conn = null;
		BufferedReader reader = null;
		String rs = null;
		try {
			StringBuffer sb = new StringBuffer();
			if (method == null || method.equals("GET")) {
				strUrl = strUrl + "?" + urlencode(params);
			}
			URL url = new URL(strUrl);
			conn = (HttpURLConnection) url.openConnection();
			if (method == null || method.equals("GET")) {
				conn.setRequestMethod("GET");
			} else {
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
			}
			conn.setRequestProperty("User-agent", userAgent);
			conn.setUseCaches(false);
			conn.setConnectTimeout(DEF_CONN_TIMEOUT);
			conn.setReadTimeout(DEF_READ_TIMEOUT);
			conn.setInstanceFollowRedirects(false);
			conn.connect();
			if (params != null && method.equals("POST")) {
				try {
					DataOutputStream out = new DataOutputStream(conn.getOutputStream());
					out.writeBytes(urlencode(params));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			InputStream is = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
			String strRead = null;
			while ((strRead = reader.readLine()) != null) {
				sb.append(strRead);
			}
			rs = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (conn != null) {
				conn.disconnect();
			}
		}
		return rs;
	}

	// 将map型转为请求参数型
	public static String urlencode(Map<String, Object> data) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry i : data.entrySet()) {
			try {
				sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue() + "", "UTF-8")).append("&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
