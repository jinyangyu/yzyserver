package datasource;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import bean.dbmodel.CollegeModel;
import bean.dbmodel.ScoreRankModel;
import controller.CollegeRecommendController;

public class DataSource {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	private static DataSource dataSource = new DataSource();
	private List<CollegeModel> allColleges;

	private List<ScoreRankModel> li_2017_ScoreRanks;
	private List<ScoreRankModel> wen_2017_ScoreRanks;
	
	private List<ScoreRankModel> li_2016_ScoreRanks;
	private List<ScoreRankModel> wen_2016_ScoreRanks;
	
	private List<ScoreRankModel> li_2015_ScoreRanks;
	private List<ScoreRankModel> wen_2015_ScoreRanks;

	private IRecommendSource recommendSource_li;
	private IRecommendSource recommendSource_wen;

	private DataSource() {
		logger1.info("DataSource init");
		long start = System.currentTimeMillis();
		allColleges = CollegeModel.dao.find(
				"select "
				+ "id,name, area,characteristic,type,build_time,import_subject_count,doctoral_program_count,"
				+ "master_program_count,belong,academician_count,student_count,telephone,"
				+ "address,official_website,enrolment_website,logo,"
				+ "wen_2017,wen_2016,wen_2015,"
				+ "li_2017,li_2016,li_2015,province,college_id,"
				+ "li_2017_rank,li_2016_rank,li_2015_rank,"
				+ "wen_2017_rank,wen_2016_rank,wen_2015_rank "
				+ "from college"
//				+ " where id < 1000"
				);
		
		logger1.info("DataSource init colleges use:" + (System.currentTimeMillis() - start) + " ms");
		
//		for (int i = 0; i < allColleges.size(); i++) {
//			logger1.info(
//					allColleges.get(i).getId() + "--" + allColleges.get(i).getName());
//		}
		

		li_2017_ScoreRanks = ScoreRankModel.dao.find("select * from scores where year=2017 and wenli=? order by score desc", "li");
		wen_2017_ScoreRanks = ScoreRankModel.dao.find("select * from scores where year=2017 and wenli=? order by score desc", "wen");
		
		li_2016_ScoreRanks = ScoreRankModel.dao.find("select * from scores where year=2016 and wenli=? order by score desc", "li");
		wen_2016_ScoreRanks = ScoreRankModel.dao.find("select * from scores where year=2016 and wenli=? order by score desc", "wen");
		
		li_2015_ScoreRanks = ScoreRankModel.dao.find("select * from scores where year=2015 and wenli=? order by score desc", "li");
		wen_2015_ScoreRanks = ScoreRankModel.dao.find("select * from scores where year=2015 and wenli=? order by score desc", "wen");

		start = System.currentTimeMillis();

		recommendSource_li = new LiRecommend();
		recommendSource_wen = new WenRecommend();

		Collections.sort(allColleges, liComp);
		recommendSource_li.setCollege(allColleges);

		Collections.sort(allColleges, wenComp);
		recommendSource_wen.setCollege(allColleges);

		logger1.info("DataSource init colleges sort by li_2017 use:" + (System.currentTimeMillis() - start) + " ms");

	}

	public static DataSource getInstance() {
		return dataSource;
	}
	
	public List<CollegeModel> getAllColleges() {
		return allColleges;
	}

	public List<CollegeModel> recommendCollege(int score, boolean isWen) {
		//得到2017年对应排名的分数，以此查询提档线数据，给出推荐院校
		int recommendScore = getRecommendScore(score, isWen);
		logger1.info("recommendCollege recommendScore:" + recommendScore);

		if (isWen) {
			return recommendSource_wen.recommendCollege(score);
		}
		return recommendSource_li.recommendCollege(score);
	}

	/*
	 * 根据当年排名，查询去年（2017）年该排名对应的分数 以去年对应的分数查询相应推荐院校
	 */
	private int getRecommendScore(int score, boolean isWen) {

		int rank = get2017RankByScore(score,isWen);
		logger1.info("recommendCollege score To rank:" + rank);

		List<ScoreRankModel> scores;
		scores = isWen ? wen_2017_ScoreRanks : li_2017_ScoreRanks;

		for(ScoreRankModel scoreRank : scores) {
			logger1.info("recommendCollege scoreRank:" + scoreRank.getRank() + " rank:" + rank);
			if(scoreRank.getRank() >=rank) {
				logger1.info("recommendCollege rank To score:" + scoreRank.getScore());
				return scoreRank.getScore(); 
			}
		}
		return score;
	}

	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2017RankByScore(int score,boolean isWen) {
		
		List<ScoreRankModel> scores;
		scores = isWen ? wen_2017_ScoreRanks : li_2017_ScoreRanks;

		for(ScoreRankModel rank : scores) {
			if(rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return 100;
	}
	
	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2016RankByScore(int score,boolean isWen) {
		
		List<ScoreRankModel> scores;
		scores = isWen ? wen_2016_ScoreRanks : li_2016_ScoreRanks;

		for(ScoreRankModel rank : scores) {
			if(rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return 100;
	}
	
	/*
	 * 通过当年分数，查询当年排名
	 */
	public int get2015RankByScore(int score,boolean isWen) {
		
		List<ScoreRankModel> scores;
		scores = isWen ? wen_2015_ScoreRanks : li_2015_ScoreRanks;

		for(ScoreRankModel rank : scores) {
			if(rank.getScore() <= score) {
				return rank.getCount();
			}
		}
		return 100;
	}

	private Comparator liComp = new Comparator<CollegeModel>() {
		public int compare(CollegeModel arg0, CollegeModel arg1) {
			int li_2017_0 = 0;
			int li_2017_1 = 0;
			try {
				li_2017_0 = Integer.parseInt(arg0.getLi_2017());
			} catch (Exception e) {
			}

			try {
				li_2017_1 = Integer.parseInt(arg1.getLi_2017());
			} catch (Exception e) {
			}
			return li_2017_1 - li_2017_0;
		}
	};

	private Comparator wenComp = new Comparator<CollegeModel>() {
		public int compare(CollegeModel arg0, CollegeModel arg1) {
			int wen_2017_0 = 0;
			int wen_2017_1 = 0;
			try {
				wen_2017_0 = Integer.parseInt(arg0.getWen_2017());
			} catch (Exception e) {
			}

			try {
				wen_2017_1 = Integer.parseInt(arg1.getWen_2017());
			} catch (Exception e) {
			}
			return wen_2017_1 - wen_2017_0;
		}
	};
	
	public void clearRecommendCache() {
		recommendSource_li.clearCache();
		recommendSource_wen.clearCache();
	}

}
