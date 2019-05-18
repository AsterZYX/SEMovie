package semoviegroup.semovie.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import semoviegroup.semovie.model.Movie;
import semoviegroup.semovie.vo.ResultVO;
import semoviegroup.semovie.service.StateClient;

@Service
public class StateService {
	/*
	 * public static void main(String[] args) { StateService ss = new
	 * StateService(); ss.getMoviesOnNext(0, 4); }
	 */

	/**
	 * 得到正在热映的电影
	 *
	 * @param size
	 *            每页显示数量
	 * @param currentPage
	 *            当前页数
	 * @return
	 */
	@GetMapping("/now")
	public ResultVO<List<Movie>> getMoviesOnShow(@RequestParam("size") Integer size,
												 @RequestParam("page") Integer currentPage) {
		String URL = "https://movie.douban.com/cinema/nowplaying/dongying/";
		StateClient sc = new StateClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			// String loc = sc.getMoviesOnShow(size, currentPage);
			SAXReader reader = new SAXReader();
			String loc = "D:\\PycharmProjects\\SEmovie\\Showing.xml";
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();
				List<Element> attributeList = child.elements();
				/*
				 * for (Element attr : attributeList) {
				 * //System.out.println(attr.getText().length());
				 * //System.out.println(attr.getText()); //if(attr.getText().length()>0) {
				 * //System.out.println(attr.getName() + ": " + attr.getText().substring(3,6));
				 * //} }
				 */

				List<Element> elementList = child.elements();
				movie.setTitle(elementList.get(0).getText());
				movie.setDoubanrating(elementList.get(1).getText());
				movie.setPoster(elementList.get(2).getText());
				movielist.add(movie);
				// System.out.println(movielist.toString());
			}
			// System.out.println(movielist.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		// System.out.println(vo);
		return vo;
	}

	/**
	 * 得到即将上映的电影
	 *
	 * @return
	 */
	@GetMapping("/next")
	public ResultVO<List<Movie>> getMoviesOnNext(@RequestParam("size") Integer size,
												 @RequestParam("page") Integer currentPage) {
		String URL = "https://movie.douban.com/coming";
		StateClient sc = new StateClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			// String loc = sc.getMoviesOnNext(size, currentPage);
			SAXReader reader = new SAXReader();
			String loc = "D:\\PycharmProjects\\SEmovie\\Later.xml";
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();

				List<Element> attributeList = child.elements();
				for (Element attr : attributeList) {
					System.out.println(attr.getText().length());
					System.out.println(attr.getText());
					// if(attr.getText().length()>0) {
					// System.out.println(attr.getName() + ": " + attr.getText().substring(3,6));
					// }
				}

				List<Element> elementList = child.elements();
				System.out.println(elementList.size());
				movie.setTitle(elementList.get(0).getText());
				movie.setRelease_date(elementList.get(1).getText());
				movie.setType(elementList.get(2).getText());
				movie.setCountry(elementList.get(3).getText());
				movie.setWanting(elementList.get(4).getText());
				movielist.add(movie);
				// System.out.println(movielist.toString());
			}
			System.out.println(movielist.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		// System.out.println(vo);
		return vo;
	}

	/**
	 * 得到最热的电影
	 *
	 * @return
	 */
	@GetMapping("/hot")
	public ResultVO<List<Movie>> getHotestMovies(@RequestParam("size") Integer size,
												 @RequestParam("page") Integer currentPage) {
		String URL = "";
		StateClient sc = new StateClient();
		List<Movie> movielist = new ArrayList<Movie>();

		try {
			// String loc = sc.getHotestMovies(size, currentPage);
			SAXReader reader = new SAXReader();
			String loc = "D:\\PycharmProjects\\SEmovie\\Hot.xml";
			File file = new File(loc);
			Document document = reader.read(file);
			Element root = document.getRootElement();
			List<Element> childElements = root.elements();
			Movie movie = null;
			for (Element child : childElements) {
				movie = new Movie();

				List<Element> attributeList = child.elements();
				for (Element attr : attributeList) {
					System.out.println(attr.getText().length());
					System.out.println(attr.getText());
					// if(attr.getText().length()>0) {
					// System.out.println(attr.getName() + ": " + attr.getText().substring(3,6));
					// }
				}

				List<Element> elementList = child.elements();
				movie.setTitle(elementList.get(0).getText());
				movie.setDoubanrating(elementList.get(1).getText());
				movie.setPoster(elementList.get(2).getText());
				movielist.add(movie);
				// System.out.println(movielist.toString());
			}
			// System.out.println(movielist.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		// System.out.println(vo);
		return vo;
	}

}
