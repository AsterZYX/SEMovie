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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSONObject;
import semoviegroup.semovie.model.Cinema;
import semoviegroup.semovie.model.Movie;
import semoviegroup.semovie.model.NewCinema;
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

    /*
     * public static void main(String[] args) { SearchService ss = new
     * SearchService(); ss.getCinemaTicketsById(12, 1, "大侦探皮卡丘", "346629",
     * "2019-05-19"); }
     */

    // 1.按关键字检索影片信息
    // showType=state catId=type sourceId=region yearId=time sortId=sort
    // size=30
    public ResultVO<List<Movie>> abstractsearch(String state, String type, String region, String time, String sort,
                                                Integer size, Integer currentPage) {
        String URL = "https://maoyan.com/films?showType=" + state + "&catId=" + type + "&sourceId=" + region
                + "&yearId=" + time + "&sortId=" + sort + "&offset=" + (currentPage - 1) * size;
        SearchClient sc = new SearchClient();
        List<Movie> movielist = new ArrayList<Movie>();
        String screturn = "";
        try {
            screturn = sc.search(state, type, region, time, sort, size, currentPage);
            String loc = screturn.substring(16, 67);
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
        ResultVO vo = new ResultVO(0, screturn.substring(67), movielist);
        System.out.print(vo.getMessage());
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

        // 猫眼rating fake； 想看人数还没有爬 先用movieid代替 ; 票房暂用movieid平方的前两位加小数点代替；
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
        long sales = sale;
        String s1 = sales + "";
        String s2 = s1.charAt(0) + "." + s1.charAt(1);
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

    public ResultVO<Movie> getCinemaTicketsById(Integer size, Integer currentPage, String name, String movieId,
                                                String showDate) {
        // String s = "{\"reason\": \"success\",\"result\":
        // [{\"cinemaName\":\"金逸影城上海龙之梦IMAX店\",\"cinemaId\":
        // \"1\",\"address\":\"上海市虹口区西江湾路388号凯德龙之梦B座6F-7F\",\"latitude\":
        // \"31.27125\",\"longitude\":\"121.4787\"},{\"cinemaName\":
        // \"上海南桥海上国际影城\",\"cinemaId\":\"2\",\"address\":
        // \"上海奉贤区百齐路288号（百联二期四楼）\",\"latitude\":\"30.91572\",\"longitude\":
        // \"121.4829\"}]}";
        String result3 = null;
        String url3 = "http://v.juhe.cn/movie/index";// 请求接口地址
        Map params3 = new HashMap();// 请求参数
        System.out.print(name);
        params3.put("title", name);// 需要检索的影片标题,utf8编码的urlencode
        params3.put("smode", "");// <foncolor=red>是否精确查找，精确:1 模糊:0 默认1</font>
        params3.put("pagesize", "");// <font color=red>每次返回条数，默认20,最大50</font>
        params3.put("offset", "");// <font color=red>偏移量，默认0,最大760</font>
        params3.put("key", APPKEY);// 应用APPKEY(应用详细页查询)
        params3.put("dtype", "");// 返回数据的格式,xml/json，默认json
        String s3 = "";
        try {
            result3 = net(url3, params3, "GET");
            JSONObject object = JSONObject.fromObject(result3);
            if (object.getInt("error_code") == 0) {
                // System.out.println(object.get("result"));
                s3 = object.get("result") + "";
                s3 = s3.substring(1, s3.length() - 1);
                // System.out.println(s3);
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, String> map3 = new Gson().fromJson(s3, new TypeToken<HashMap<String, String>>() {
        }.getType());

        SortedMap<Cinema, Double> taoppcinemaList = new TreeMap<Cinema, Double>();
        ArrayList<NewCinema> taoppcinemaListJUHE = new ArrayList<NewCinema>();
        ArrayList<NewCinema> taoppcinemaList2 = new ArrayList<NewCinema>();

        String juhemovieid = map3.get("movieid");
        System.out.println(name);
        System.out.println("juhemovieid:" + juhemovieid);
        String result = null;
        String url = "http://v.juhe.cn/movie/movies.cinemas";
        Map params = new HashMap();
        params.put("cityid", "14");
        params.put("movieid", juhemovieid);
        params.put("key", APPKEY);
        params.put("dtype", "");
        String s = "";
        try {
            result = net(url, params, "GET");
            JSONObject object =
                    JSONObject.fromObject(result);
            if (object.getInt("error_code") == 0) {
                System.out.println(object.get("result"));
                s = object.get("result") + "";
                System.out.println("!!!!!!!");
                // System.out.println(s);
            } else {
                System.out.println(object.get("error_code") + ":" + object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Gson gson1 = new Gson();
        List<innerCinema> list = gson1.fromJson(s, new
                TypeToken<List<innerCinema>>() {
                }.getType());
        System.out.print("list size:");
        System.out.println(list.size());


        for (innerCinema c : list) {
            System.out.println(c.toString());
            result = null;
            url = "http://v.juhe.cn/movie/cinemas.movies";
            params = new HashMap();
            params.put("cinemaid", c.getCinemaId());
            params.put("movieid", juhemovieid);
            params.put("key", APPKEY);
            params.put("dtype", "");
            s = "";
            try {
                result = net(url, params, "GET");
                JSONObject object = JSONObject.fromObject(result);
                if (object.getInt("error_code") == 0) {
                    s = object.get("result") + "";
                } else {
                    System.out.println(object.get("error_code") + ":" + object.get("reason"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            JsonObject jo = new JsonParser().parse(s).getAsJsonObject();
            System.out.println(jo);
            JsonArray lists = jo.get("lists").getAsJsonArray();
            JsonObject list0 = lists.get(0).getAsJsonObject();
            JsonElement r =
                    list0.get("broadcast");
            String sss =
                    r.getAsJsonArray().get(0).getAsJsonObject().get("price") + "";
            sss =
                    sss.substring(1, sss.length() - 1);
            System.out.println(sss);

            Cinema cinema = new Cinema();
            cinema.setLocation(c.getAddress());
            cinema.setName(c.getCinemaName());
            if (sss.equals("")) {
                taoppcinemaListJUHE.add(new NewCinema(cinema.getName(), cinema.getLocation(), "-1.0"));
                taoppcinemaList.put(cinema, -1.0);
            } else {
                taoppcinemaListJUHE.add(new NewCinema(cinema.getName(), cinema.getLocation(), sss));
                taoppcinemaList.put(cinema,
                        Double.parseDouble(sss));
            }

        }

        //taoppcinemaList.put(new Cinema("CGV影城(百家湖店)", "江宁区双龙大道1698号景枫广场3F-4F"), 40.0);
        // taoppcinemaList.put(new Cinema("卢米埃影城(紫峰购物广场店)", "鼓楼区中山北路紫峰购物中心5-6F"), 34.0);
        //taoppcinemaList.put(new Cinema("南京科技馆影城", "雨花台区紫荆花路9号（科技馆北门）"), 33.0);
        // taoppcinemaList.put(new Cinema("时代影城", "高淳区富克斯5楼"), 36.0);
        //taoppcinemaList.put(new Cinema("苏宁尊享影城 (新街口店)", "秦淮区淮海路68号苏宁生活广场2F"), 33.0);

        // taoppcinemaList2.add(new NewCinema("CGV影城(百家湖店)", "江宁区双龙大道1698号景枫广场3F-4F", "40.0"));
        // taoppcinemaList2.add(new NewCinema("卢米埃影城(紫峰购物广场店)", "鼓楼区中山北路紫峰购物中心5-6F", "34.0"));
        // taoppcinemaList2.add(new NewCinema("南京科技馆影城", "雨花台区紫荆花路9号（科技馆北门）", "33.0"));
        // taoppcinemaList2.add(new NewCinema("时代影城", "高淳区富克斯5楼", "36.0"));
        // taoppcinemaList2.add(new NewCinema("苏宁尊享影城 (新街口店)", "秦淮区淮海路68号苏宁生活广场2F", "33.0"));

        Movie m = new Movie();
        m.setMovieid(movieId);
        m.setTitle(name);
        m.setTaoppcinemaList(taoppcinemaListJUHE);  //可以改成taoppcinemaListJUHE   taoppcinemaList2
        m.setMaoyancinemaList(getMaoYanTicketByMovieId(movieId, showDate, size * (currentPage - 1) + ""));
        System.out.println("！！！！！！！！！！！！");

        System.out.println(m.toString());


        //为了好看可以将NewCinema里增加 maoyanprice juheprice字段fake数据


        return new ResultVO(0, "", m);

    }

    class innerCinema {
        String cinemaName;
        String cinemaId;
        String address;
        String latitude;
        String longitude;

        public innerCinema() {
        }

        public innerCinema(String cinemaName, String address) {
            super();
            this.cinemaName = cinemaName;
            this.cinemaId = "";
            this.address = address;
            this.latitude = "";
            this.longitude = "";
        }

        public String getCinemaName() {
            return cinemaName;
        }

        public void setCinemaName(String cinemaName) {
            this.cinemaName = cinemaName;
        }

        public String getCinemaId() {
            return cinemaId;
        }

        public void setCinemaId(String cinemaId) {
            this.cinemaId = cinemaId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "{\"cinemaName\":\"" + cinemaName + "\",\"cinemaId\":\"" + cinemaId + "\",\"address\":\"" + address
                    + "\",\"latitude\":\"" + latitude + "\",\"longitude\":\"" + longitude + "\"}";
        }

    }

    public ArrayList<NewCinema> getMaoYanTicketByMovieId(String movieid, String showDate, String offset) {
        SearchClient sc = new SearchClient();
        ArrayList<NewCinema> reli = new ArrayList<NewCinema>();
        try {
            String loc = sc.getMaoYanTicketByMovieId(movieid, showDate, offset);
            SAXReader reader = new SAXReader();
            File file = new File(loc);
            Document document = reader.read(file);
            Element root = document.getRootElement();
            List<Element> childElements = root.elements();

            for (Element child : childElements) {
                Cinema c = new Cinema();
                c.setLocation(child.elements().get(1).getText());
                c.setName(child.elements().get(0).getText());
                reli.add(new NewCinema(child.elements().get(0).getText(), child.elements().get(1).getText(),
                        child.elements().get(2).getText().substring(3)));

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return reli;

    }

    ////////////////////////////

    /**
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
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
