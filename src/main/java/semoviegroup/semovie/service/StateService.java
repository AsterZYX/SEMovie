package semoviegroup.semovie.service;
import service.StateClient;
public class StateService {
	 /**
     * 得到正在热映的电影
     * @param size 每页显示数量
     * @param currentPage 当前页数
     * @return
     */
    @GetMapping("/now")
    public ResultVO<List<Movie>> getMoviesOnShow(@RequestParam("size") Integer size,
                                                 @RequestParam("page") Integer currentPage){
        String URL="https://movie.douban.com/cinema/nowplaying/dongying/";
        StateClient sc=new StateClient();
        List<Movie> =new ArrayList<Movie>();

		try {
			String loc = sc.getMoviesOnShow(size,currentPage);
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
				movie.setTitle(elementList.get(0).getText());
				movie.setDoubanrating(elementList.get(1).getText());
				movie.setPlot_simple(elementList.get(2).getText());
				movielist.add(movie);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ResultVO vo = new ResultVO(0, "", movielist);
		return vo.;
    }


    /**
     * 得到即将上映的电影
     * @return
     */
    @GetMapping("/next")
    public ResultVO<List<Movie>> getMoviesOnNext(@RequestParam("size") Integer size,
                                                 @RequestParam("page") Integer currentPage){
        return null;
    }

    /**
     * 得到最热的电影
     * @return
     */
    @GetMapping("/hot")
    public ResultVO<List<Movie>> getHotestMovies(@RequestParam("size") Integer size,
                                                 @RequestParam("page") Integer currentPage){
        return null;
    }

}
