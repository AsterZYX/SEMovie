package semoviegroup.semovie.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import semoviegroup.semovie.model.Movie;
import semoviegroup.semovie.model.Worker;
import semoviegroup.semovie.vo.ResultVO;

public class RankingListService {
	public static void main(String args[]) {
		RankingListService rs=new RankingListService();
		rs.getDoubanRating(0, 0);
	}
	
	public ResultVO<List<Movie>> getMovieBoxOfficeList(@RequestParam("size") Integer size,
			@RequestParam("page") Integer currentPage) {
		
		//String URL = "https://movie.douban.com/cinema/nowplaying/dongying/";
		String URL="https://box.maoyan.com/promovie/api/box/second.json";
		RankingListClient rc=new RankingListClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			String loc = rc.getMaoyanBoxList();
			SAXReader reader = new SAXReader();
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();

				List<Element> attributeList = child.elements();
				

				List<Element> elementList = child.elements();
				movie.setTitle(elementList.get(0).getText());
				movie.setSale(elementList.get(8).getText());
				movielist.add(movie);
				
			}
			System.out.println(movielist.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		//System.out.println(vo);
		return vo;
	}
	
	//猫眼评分
	public ResultVO<List<Movie>> getMaoyanRating(@RequestParam("size") Integer size,
			@RequestParam("page") Integer currentPage) {
		
		
		String URL="http://maoyan.com/board/4?offset="+(currentPage - 1) * size;
		RankingListClient rc=new RankingListClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			String loc = rc.getMaoyanRating(size, currentPage);
			SAXReader reader = new SAXReader();
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();

				List<Element> attributeList = child.elements();

				List<Element> elementList = child.elements();
				String actorList=elementList.get(3).getText();
				String[] al=actorList.split(",");
				ArrayList<Worker> acl=new ArrayList<Worker>();
				for(int i=0;i<al.length;i++) {
					Worker w=new Worker();
					w.setIdentity("演员");
					w.setName(al[i]);
					acl.add(w);
				}
				movie.setPoster(elementList.get(1).getText());
				movie.setTitle(elementList.get(2).getText());
				movie.setActorList(acl);
				movie.setYear(elementList.get(4).getText());
				movie.setMaoyanrating(elementList.get(5).getText());
				
				movielist.add(movie);
				
			}
			System.out.println(movielist.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		//System.out.println(vo);
		return vo;
	}
	
	//豆瓣评分
	public ResultVO<List<Movie>> getDoubanRating(@RequestParam("size") Integer size,
			@RequestParam("page") Integer currentPage) {
		
		RankingListClient rc=new RankingListClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			String loc = rc.getDoubanRating();
			SAXReader reader = new SAXReader();
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();

				List<Element> attributeList = child.elements();
				

				List<Element> elementList = child.elements();
				movie.setTitle(elementList.get(0).getText());
				movie.setDoubanrating(elementList.get(1).getText());
				movie.setRating_count(elementList.get(2).getText());
				movielist.add(movie);
				
			}
			System.out.println(movielist.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		//System.out.println(vo);
		return vo;
	}
}
