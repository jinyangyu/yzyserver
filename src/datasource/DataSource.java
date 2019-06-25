package datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import bean.dbmodel.AdminOpenid;
import bean.dbmodel.CollegeModelAll;
import bean.dbmodel.Info;
import bean.dbmodel.InfoUserCheck;
import bean.dbmodel.ScoreRankModel;
import controller.CollegeRecommendController;

public class DataSource {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	private static DataSource dataSource = new DataSource();
	private List<CollegeModelAll> allColleges;
	private List<CollegeModelAll> henanColleges_wen;
	private List<CollegeModelAll> henanColleges_li;

	private List<ScoreRankModel> li_2019_ScoreRanks;
	private List<ScoreRankModel> wen_2019_ScoreRanks;
	
	private List<ScoreRankModel> li_2018_ScoreRanks;
	private List<ScoreRankModel> wen_2018_ScoreRanks;

	private List<ScoreRankModel> li_2017_ScoreRanks;
	private List<ScoreRankModel> wen_2017_ScoreRanks;

	private List<ScoreRankModel> li_2016_ScoreRanks;
	private List<ScoreRankModel> wen_2016_ScoreRanks;

	private List<ScoreRankModel> li_2015_ScoreRanks;
	private List<ScoreRankModel> wen_2015_ScoreRanks;

	private IRecommendSource recommendSource_li;
	private IRecommendSource recommendSource_wen;

	private HashSet<String> adminMap;
	private HashSet<String> notifyedUserMap;

	private Info infoUser;

	private DataSource() {
		logger1.info("DataSource init");
		long start = System.currentTimeMillis();
		allColleges = CollegeModelAll.dao.find(
				"select " + "id,name, area,characteristic,type,build_time,import_subject_count,doctoral_program_count,"
						+ "master_program_count,belong,academician_count,student_count,telephone,"
						+ "address,official_website,enrolment_website,logo," + "wen_2018,wen_2017,wen_2016,wen_2015,"
						+ "li_2018,li_2017,li_2016,li_2015,province,college_id,"
						+ "li_2018_rank,li_2017_rank,li_2016_rank,li_2015_rank,"
						+ "wen_2018_rank,wen_2017_rank,wen_2016_rank,wen_2015_rank " + "from college_all");

		logger1.info("DataSource init colleges use:" + (System.currentTimeMillis() - start) + " ms");

		li_2019_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2019 and wenli=? order by score desc", "li");
		wen_2019_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2019 and wenli=? order by score desc", "wen");
		
		li_2018_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2018 and wenli=? order by score desc", "li");
		wen_2018_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2018 and wenli=? order by score desc", "wen");

		li_2017_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2017 and wenli=? order by score desc", "li");
		wen_2017_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2017 and wenli=? order by score desc", "wen");

		li_2016_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2016 and wenli=? order by score desc", "li");
		wen_2016_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2016 and wenli=? order by score desc", "wen");

		li_2015_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2015 and wenli=? order by score desc", "li");
		wen_2015_ScoreRanks = ScoreRankModel.dao
				.find("select * from scores where year=2015 and wenli=? order by score desc", "wen");

		start = System.currentTimeMillis();

		recommendSource_li = new LiRecommend();
		recommendSource_wen = new WenRecommend();

		Collections.sort(allColleges, liComp);
		recommendSource_li.setCollege(allColleges);

		Collections.sort(allColleges, wenComp);
		recommendSource_wen.setCollege(allColleges);

		logger1.info("DataSource init colleges sort by li_2018 use:" + (System.currentTimeMillis() - start) + " ms");

		initHenanCollege();

		resetAdmin();
		resetInfoUser();
		resetNotifyedUser();
	}

	private void initHenanCollege() {
		henanColleges_wen = new ArrayList<CollegeModelAll>();
		henanColleges_li = new ArrayList<CollegeModelAll>();
		for (CollegeModelAll model : allColleges) {
			if ("河南".equals(model.getProvince())) {
				henanColleges_wen.add(model);
				henanColleges_li.add(model);
			}
		}
		Collections.sort(henanColleges_li, liComp);
		Collections.sort(henanColleges_wen, wenComp);
	}

	public List<CollegeModelAll> getHenanCollege(boolean isWen) {
		return isWen ? henanColleges_wen : henanColleges_li;
	}

	public void resetNotifyedUser() {
		if (notifyedUserMap == null) {
			notifyedUserMap = new HashSet<String>();
		}
		notifyedUserMap.clear();

		List<InfoUserCheck> infos = InfoUserCheck.dao.find("select * from info_user_check");
		for (InfoUserCheck info : infos) {
			notifyedUserMap.add(info.getOpenid());
		}
	}

	public boolean shouldUserNotify(String openId) {
		boolean result = !notifyedUserMap.contains(openId);
		if (result) {
			notifyedUserMap.add(openId);
			InfoUserCheck userCheck = new InfoUserCheck();
			userCheck.setOpenid(openId);
			userCheck.save();
		}
		return result;
	}

	public void resetInfoUser() {
		List<Info> infos = Info.dao.find("select * from info");
		if (infos != null && infos.size() > 0) {
			infoUser = infos.get(0);
		}
	}

	public Info getInfoUser() {
		return infoUser;
	}

	public void resetAdmin() {
		List<AdminOpenid> admins = AdminOpenid.dao.find("select * from admin_openid");

		if (adminMap == null) {
			adminMap = new HashSet<String>();
		} else {
			adminMap.clear();
		}
		for (AdminOpenid admin : admins) {
			adminMap.add(admin.getOpenid());
		}
	}

	public boolean isAdmin(String openId) {
		return adminMap.contains(openId);
	}

	public static DataSource getInstance() {
		return dataSource;
	}

	public List<CollegeModelAll> getAllColleges() {
		return allColleges;
	}

	public List<CollegeModelAll> recommendCollege(int score, boolean isWen) {
		// 得到2018年对应排名的分数，以此查询提档线数据，给出推荐院校
		int recommendScore = getRecommendScore(score, isWen);
		logger1.info("recommendCollege recommendScore:" + recommendScore);

		return recommendCollegeByScore(recommendScore, isWen);
	}

	public List<CollegeModelAll> recommendCollegeByScore(int recommendScore, boolean isWen) {
		if (isWen) {
			return recommendSource_wen.recommendCollege(recommendScore);
		}
		return recommendSource_li.recommendCollege(recommendScore);
	}

	/*
	 * 根据当年排名，查询去年（2018）年该排名对应的分数 以去年对应的分数查询相应推荐院校
	 */
	public int getRecommendScore(int score, boolean isWen) {

		int rank = get2019RankByScore(score, isWen);
		logger1.info("recommendCollege score To rank:" + rank);

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2018_ScoreRanks : li_2018_ScoreRanks;

		for (ScoreRankModel scoreRank : scores) {
			// logger1.info("recommendCollege scoreRank:" + scoreRank.getRank() + " rank:" +
			// rank);
			if (scoreRank.getCount() >= rank) {
				// logger1.info("recommendCollege rank To score:" + scoreRank.getScore());
				return scoreRank.getScore();
			}
		}
		return score;
	}
	
	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2019RankByScore(int score, boolean isWen) {

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2019_ScoreRanks : li_2019_ScoreRanks;

		for (ScoreRankModel rank : scores) {
			if (rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return -1;
	}

	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2018RankByScore(int score, boolean isWen) {

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2018_ScoreRanks : li_2018_ScoreRanks;

		for (ScoreRankModel rank : scores) {
			if (rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return -1;
	}

	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2017RankByScore(int score, boolean isWen) {

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2017_ScoreRanks : li_2017_ScoreRanks;

		for (ScoreRankModel rank : scores) {
			if (rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return -1;
	}

	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2016RankByScore(int score, boolean isWen) {

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2016_ScoreRanks : li_2016_ScoreRanks;

		for (ScoreRankModel rank : scores) {
			if (rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return -1;
	}

	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2015RankByScore(int score, boolean isWen) {

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2015_ScoreRanks : li_2015_ScoreRanks;

		for (ScoreRankModel rank : scores) {
			if (rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return -1;
	}

	public int get2018ScoreByRank(int rank, boolean isWen) {
		List<ScoreRankModel> scoreRanks = isWen ? wen_2018_ScoreRanks : li_2018_ScoreRanks;
		for (ScoreRankModel model : scoreRanks) {
			if (model.getCount() >= rank) {
				return model.getScore();
			}
		}
		return 0;
	}

	public int get2018ScoreByIndex(int index, boolean isWen) {
		List<ScoreRankModel> scoreRanks = isWen ? wen_2018_ScoreRanks : li_2018_ScoreRanks;
		for (ScoreRankModel model : scoreRanks) {
			if (model.getRank() >= index) {
				return model.getScore();
			}
		}
		return 0;
	}

	private Comparator liComp = new Comparator<CollegeModelAll>() {
		public int compare(CollegeModelAll arg0, CollegeModelAll arg1) {
			int li_2018_0 = 0;
			int li_2018_1 = 0;
			try {
				li_2018_0 = Integer.parseInt(arg0.getLi_2018());
			} catch (Exception e) {
			}

			try {
				li_2018_1 = Integer.parseInt(arg1.getLi_2018());
			} catch (Exception e) {
			}
			return li_2018_1 - li_2018_0;
		}
	};

	private Comparator wenComp = new Comparator<CollegeModelAll>() {
		public int compare(CollegeModelAll arg0, CollegeModelAll arg1) {
			int wen_2018_0 = 0;
			int wen_2018_1 = 0;
			try {
				wen_2018_0 = Integer.parseInt(arg0.getWen_2018());
			} catch (Exception e) {
			}

			try {
				wen_2018_1 = Integer.parseInt(arg1.getWen_2018());
			} catch (Exception e) {
			}
			return wen_2018_1 - wen_2018_0;
		}
	};

	public String clearRecommendCache() {
		String result_li = recommendSource_li.clearCache();
		String result_wen = recommendSource_wen.clearCache();

		String result_cache = ChanceUtil.clearChanceCache();

		return "<br>" + result_li + "</br>" + "<br>" + result_wen + "</br>" + "<br>" + result_cache + "</br>";
	}

}
