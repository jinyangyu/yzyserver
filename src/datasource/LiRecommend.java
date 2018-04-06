package datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import demo.bean.CollegeModel;

public class LiRecommend implements IRecommendSource {

	private List<CollegeModel> allColleges_li_2017;
	private HashMap<String, List<CollegeModel>> recommendCache;

	public LiRecommend() {
		allColleges_li_2017 = new ArrayList<CollegeModel>();
		recommendCache = new HashMap<String, List<CollegeModel>>();
	}

	public void setCollege(List<CollegeModel> colleges) {
		allColleges_li_2017.addAll(colleges);
	}

	public List<CollegeModel> recommendCollege(String scoreStr) {
		if (recommendCache.containsKey(scoreStr)) {
			return recommendCache.get(scoreStr);
		}
		
		int score = Integer.parseInt(scoreStr);
		List<CollegeModel> results = RecommendUtil.getInstance().recommendCollege(allColleges_li_2017, score);
		recommendCache.put(scoreStr, results);
		return results;
	}

}
