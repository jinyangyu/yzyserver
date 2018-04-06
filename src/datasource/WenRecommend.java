package datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import demo.bean.CollegeModel;

public class WenRecommend implements IRecommendSource {

	private List<CollegeModel> allColleges_wen_2017;
	private HashMap<String, List<CollegeModel>> recommendCache;

	public WenRecommend() {
		allColleges_wen_2017 = new ArrayList<CollegeModel>();
		recommendCache = new HashMap<String, List<CollegeModel>>();
	}

	public void setCollege(List<CollegeModel> colleges) {
		allColleges_wen_2017.addAll(colleges);
	}

	public List<CollegeModel> recommendCollege(String scoreStr) {
		if (recommendCache.containsKey(scoreStr)) {
			return recommendCache.get(scoreStr);
		}

		int score = Integer.parseInt(scoreStr);
		List<CollegeModel> results = RecommendUtil.getInstance().recommendCollege(allColleges_wen_2017, score);
		recommendCache.put(scoreStr, results);
		return results;
	}

}
