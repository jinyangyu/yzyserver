package datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.dbmodel.CollegeModelAll;

public class LiRecommend implements IRecommendSource {

	private List<CollegeModelAll> allColleges_li_2018;
	private HashMap<String, List<CollegeModelAll>> recommendCache;

	public LiRecommend() {
		allColleges_li_2018 = new ArrayList<CollegeModelAll>();
		recommendCache = new HashMap<String, List<CollegeModelAll>>();
	}

	public void setCollege(List<CollegeModelAll> colleges) {
		allColleges_li_2018.addAll(colleges);
	}

	public List<CollegeModelAll> recommendCollege(int score) {
		if (recommendCache.containsKey(String.valueOf(score))) {
			return recommendCache.get(String.valueOf(score));
		}

		List<CollegeModelAll> results = RecommendUtil.getInstance().recommendCollege(allColleges_li_2018, score, false);
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
		return "理科推荐数据，已清空 " + size + " 条缓存\n";
	}

}
