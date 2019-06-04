package datasource;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import bean.dbmodel.LiankaoScoreModel;
import bean.dbmodel.SchoolScoreModel;

public class LiankaoDataSource {
	public static Logger logger = Logger.getLogger(LiankaoDataSource.class);
	private static LiankaoDataSource instance = new LiankaoDataSource();

	private LiankaoDataSource() {
	}

	public static LiankaoDataSource getInstance() {
		return instance;
	}

	private List<LiankaoScoreModel> liankao_1_wen;
	private List<LiankaoScoreModel> liankao_1_li;
	private List<LiankaoScoreModel> liankao_2_wen;
	private List<LiankaoScoreModel> liankao_2_li;

	private List<LiankaoScoreModel> liankao_3_wen;
	private List<LiankaoScoreModel> liankao_3_li;
	private List<LiankaoScoreModel> liankao_4_wen;
	private List<LiankaoScoreModel> liankao_4_li;

	private HashMap<Integer, Integer> yigaoRankMap_wen = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> yigaoRankMap_li = new HashMap<Integer, Integer>();

	private HashMap<Integer, Integer> wxyzRankMap_li = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> wxyzRankMap_wen = new HashMap<Integer, Integer>();

	public void init() {
		List<SchoolScoreModel> list = SchoolScoreModel.dao
				.find("select * from top_school_scores where school=? and wenli=? order by score desc", "yigao", "wen");

		for (SchoolScoreModel scoreModel : list) {
			yigaoRankMap_wen.put(scoreModel.getSchool_rank(), scoreModel.getRank());
		}

		list = SchoolScoreModel.dao
				.find("select * from top_school_scores where school=? and wenli=? order by score desc", "yigao", "li");

		for (SchoolScoreModel scoreModel : list) {
			yigaoRankMap_li.put(scoreModel.getSchool_rank(), scoreModel.getRank());
		}

		list = SchoolScoreModel.dao
				.find("select * from top_school_scores where school=? and wenli=? order by score desc", "wxyz", "li");

		for (SchoolScoreModel scoreModel : list) {
			wxyzRankMap_li.put(scoreModel.getSchool_rank(), scoreModel.getRank());
		}

		list = SchoolScoreModel.dao
				.find("select * from top_school_scores where school=? and wenli=? order by score desc", "wxyz", "wen");

		for (SchoolScoreModel scoreModel : list) {
			wxyzRankMap_wen.put(scoreModel.getSchool_rank(), scoreModel.getRank());
		}

		liankao_1_wen = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=1 and wenli=? order by score desc", "wen");

		liankao_1_li = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=1 and wenli=? order by score desc", "li");

		liankao_2_wen = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=2 and wenli=? order by score desc", "wen");

		liankao_2_li = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=2 and wenli=? order by score desc", "li");

		liankao_3_wen = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=3 and wenli=? order by score desc", "wen");

		liankao_3_li = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=3 and wenli=? order by score desc", "li");

		liankao_4_wen = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=4 and wenli=? order by score desc", "wen");

		liankao_4_li = LiankaoScoreModel.dao
				.find("select * from liankao_score_rank where liankao_id=4 and wenli=? order by score desc", "li");

	}

	public int getLiankaoRecommendScore(int liankao_id, int score, boolean isWen) {
		HashMap<Integer, Integer> scoreRanks = null;

		List<LiankaoScoreModel> liankaoList = null;
		switch (liankao_id) {
		case 1:
			scoreRanks = isWen ? wxyzRankMap_wen : wxyzRankMap_li;
			liankaoList = isWen ? liankao_1_wen : liankao_1_li;
			break;
		case 2:
			scoreRanks = isWen ? wxyzRankMap_wen : wxyzRankMap_li;
			liankaoList = isWen ? liankao_2_wen : liankao_2_li;
			break;
		case 3:
			scoreRanks = isWen ? yigaoRankMap_wen : yigaoRankMap_li;
			liankaoList = isWen ? liankao_3_wen : liankao_3_li;
			break;
		case 4:
			scoreRanks = isWen ? yigaoRankMap_wen : yigaoRankMap_li;
			liankaoList = isWen ? liankao_4_wen : liankao_4_li;
			break;
		}

		int liankaoRank = 0;
		for (LiankaoScoreModel scoreModel : liankaoList) {
			if (scoreModel.getScore() <= score) {
				liankaoRank = scoreModel.getRank();
				logger.info("联考成绩：" + score + " 对应学校：" + scoreModel.getSchool() + " 对应排名：" + liankaoRank);
				break;
			}
		}

		System.out.println("联考在学校里的排名:" + liankaoRank);
		int lastYearRank = scoreRanks.get(liankaoRank);
		System.out.println("学校排名对应去年高考排名:" + lastYearRank);

		return DataSource.getInstance().get2018ScoreByIndex(lastYearRank, isWen);
	}

}
