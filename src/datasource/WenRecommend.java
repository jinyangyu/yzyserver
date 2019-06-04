package datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import bean.dbmodel.CollegeModelAll;

public class WenRecommend implements IRecommendSource {
	public static Logger logger1 = Logger.getLogger(WenRecommend.class);

	private List<CollegeModelAll> allColleges_wen_2018;
	private HashMap<String, List<CollegeModelAll>> recommendCache;

	public WenRecommend() {
		allColleges_wen_2018 = new ArrayList<CollegeModelAll>();
		recommendCache = new HashMap<String, List<CollegeModelAll>>();
	}

	public void setCollege(List<CollegeModelAll> colleges) {
		allColleges_wen_2018.addAll(colleges);
	}

	public List<CollegeModelAll> recommendCollege(int score) {
		logger1.info("recommendCollege wen:" + score);
		if (recommendCache.containsKey(String.valueOf(score))) {
			logger1.info("recommendCollege return cache");
			return recommendCache.get(String.valueOf(score));
		}

		logger1.info("new recommendCollege");
		List<CollegeModelAll> results = RecommendUtil.getInstance().recommendCollege(allColleges_wen_2018, score, true);
		if (results != null) {
			recommendCache.put(String.valueOf(score), results);
		}
		return results;
	}

	public String clearCache() {
		int size = recommendCache.size();
		if (recommendCache != null) {
			recommendCache.clear();
		}
		return "文科推荐数据，已清空 " + size + " 条缓存\n";
	}

}
