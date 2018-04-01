package controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import datasource.DataSource;
import demo.bean.CollegeModel;
import demo.bean.RankResult;
import demo.bean.UserInfoModel;
import demo.result.CollegeResult;
import demo.result.Result;

public class CollegeRecommendController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	public void recommend() {

		logger1.info("CollegeRecommendController recommend");

		String score = getPara("score");
		String clientSession = getPara("clientSession");

		logger1.info("score:" + score + " clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			renderJson(new Result(201, "登陆信息失效", null));
			return;
		}

		UserInfoModel user = users.get(0);
		logger1.info("user:" + user.getNickName());

		List<CollegeModel> colleges = DataSource.getInstance().recommendCollege(score);

		CollegeResult collegeResult = new CollegeResult();
		collegeResult.setColleges(colleges);
		List<RankResult> ranks = new ArrayList<RankResult>();
		ranks.add(new RankResult("2017", "102017"));
		ranks.add(new RankResult("2016", "202016"));
		ranks.add(new RankResult("2015", "302015"));
		collegeResult.setRanks(ranks);

		renderJson(new Result(200, "query success", collegeResult));
	}
}