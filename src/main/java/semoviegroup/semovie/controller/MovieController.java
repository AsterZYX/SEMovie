package semoviegroup.semovie.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import semoviegroup.semovie.model.Analysis;
import semoviegroup.semovie.model.Movie;
import semoviegroup.semovie.service.AnalysisService;
import semoviegroup.semovie.service.RankingListService;
import semoviegroup.semovie.service.SearchService;
import semoviegroup.semovie.service.StateService;
import semoviegroup.semovie.vo.ResultVO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    SearchService searchService;
    @Autowired
    StateService stateService;
    @Autowired
    RankingListService rankingListService;
    @Autowired
    AnalysisService analysisService;

    /**
     * 得到正在热映的电影
     *
     * @param size        每页显示数量
     * @param currentPage 当前页数
     * @return
     */
    @GetMapping("/now")
    public ResultVO<List<Movie>> getMoviesOnShow(@RequestParam("size") Integer size,
                                                 @RequestParam("page") Integer currentPage) {
        return stateService.getMoviesOnShow(size, currentPage);
    }

    /**
     * 得到即将上映的电影
     *
     * @return
     */
    @GetMapping("/next")
    public ResultVO<List<Movie>> getMoviesOnNext(@RequestParam("size") Integer size,
                                                 @RequestParam("page") Integer currentPage) {
        return stateService.getMoviesOnNext(size, currentPage);
    }

    /**
     * 得到最热的电影
     *
     * @return
     */
    @GetMapping("/hot")
    public ResultVO<List<Movie>> getHotestMovies(@RequestParam("size") Integer size,
                                                 @RequestParam("page") Integer currentPage) {
        return stateService.getHotestMovies(size, currentPage);
    }

    /**
     * 得到今日票房榜单
     *
     * @return
     */
    @GetMapping("/list/sales")
    public ResultVO<List<Movie>> getMovieBoxOfficeList(@RequestParam("size") Integer size,
                                                       @RequestParam("page") Integer currentPage) {
        return rankingListService.getMovieBoxOfficeList(size, currentPage);
    }

    /**
     * 得到猫眼评分榜单
     *
     * @return
     */
    @GetMapping("/list/score/maoyan")
    public ResultVO<List<Movie>> getMovieScoreList(@RequestParam("size") Integer size,
                                                   @RequestParam("page") Integer currentPage) {
        return rankingListService.getMaoyanRating(size, currentPage);
    }

    /**
     * 得到豆瓣评分榜单
     *
     * @return
     */
    @GetMapping("/list/score/douban")
    public ResultVO<List<Movie>> getDoubanMovieScoreList(@RequestParam("size") Integer size,
                                                         @RequestParam("page") Integer currentPage) {
        return rankingListService.getDoubanRating(size, currentPage);
    }

    /**
     * 搜索电影
     *
     * @param size        每页显示数量
     * @param currentPage 当前页数
     * @param keyword     关键字
     * @param type        电影类型
     * @param region      电影地区
     * @param time        电影年份
     * @param sort        排序根据
     * @return
     */
    @GetMapping("/list/search")
    public ResultVO<List<Movie>> searchMovie(@RequestParam("size") Integer size,
                                             @RequestParam("page") Integer currentPage, @RequestParam("keyword") String keyword,
                                             @RequestParam("type") String type, @RequestParam("region") String region, @RequestParam("time") String time,
                                             @RequestParam("sort") String sort, @RequestParam("state") String state) {

        return searchService.abstractsearch(state, type, region, time, sort, size, currentPage);
    }

    /**
     * 得到电影详情
     *
     * @param movieId   电影编号
     * @param moviename 电影名称
     * @return
     */
    @GetMapping("/detail")
    public ResultVO<Movie> getMovieDetailById(@RequestParam("id") String movieId,
                                              @RequestParam("title") String moviename) {
        return searchService.searchMovieById(movieId, moviename);
    }

    /**
     * 得到电影购票信息
     *
     * @param size        12 每页显示数量
     * @param currentPage 从1开始 当前页数
     * @param movieName   电影名称
     * @param movieId     电影编号
     * @param showDate    日期 //格式： 2019-05-18
     * @return
     */
    @GetMapping("/ticket")
    public ResultVO<Movie> getCinemaTicketsById(@RequestParam("size") Integer size,
                                                @RequestParam("page") Integer currentPage, @RequestParam("title") String movieName,
                                                @RequestParam("id") String movieId, @RequestParam("showDate") String showDate) {
        return searchService.getCinemaTicketsById(size, currentPage, movieName, movieId, showDate);
    }

    /**
     * 得到电影评价分析
     *
     * @param id        电影ID（猫眼）
     * @param movieName 电影名称
     * @return 分析结果
     */
    @GetMapping("/remark")
    public ResultVO<Analysis> getRemarkAnalysis(@RequestParam("id") Integer id, @RequestParam("name") String movieName) {
        Analysis ana = new Analysis();
        String s = analysisService.getSummaryAndKeywords(movieName);
        String[] ss = s.split(" ");
        List<String> llist = new ArrayList<String>();
        for (int i = 0; i < 10; i++) {
            llist.add(ss[i]);
        }
        ana.setKeywords(llist);
        ana.setSummary(ss[10]);

        ResultVO<Analysis> vo = new ResultVO<Analysis>(0, "", ana);
        return vo;
    }

}
