package datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import bean.dbmodel.CollegeModel;
import controller.CollegeRecommendController;

public class WenRecommend implements IRecommendSource {
	public static Logger logger1 = Logger.getLogger(WenRecommend.class);

	private List<CollegeModel> allColleges_wen_2017;
	private HashMap<String, List<CollegeModel>> recommendCache;

	public WenRecommend() {
		allColleges_wen_2017 = new ArrayList<CollegeModel>();
		recommendCache = new HashMap<String, List<CollegeModel>>();
	}

	public void setCollege(List<CollegeModel> colleges) {
		allColleges_wen_2017.addAll(colleges);

//		for (int i = 0; i < allColleges_wen_2017.size(); i++) {
//			System.out.println(
//					allColleges_wen_2017.get(i).getName() + " wen_2017:" + allColleges_wen_2017.get(i).getWen_2017());
//		}
	}

	public List<CollegeModel> recommendCollege(int score) {
		logger1.info("recommendCollege wen:" + score);
		if (recommendCache.containsKey(String.valueOf(score))) {
			logger1.info("recommendCollege return cache");
			return recommendCache.get(String.valueOf(score));
		}

		logger1.info("new recommendCollege");
		List<CollegeModel> results = RecommendUtil.getInstance().recommendCollege(allColleges_wen_2017, score, true);
		if (results != null) {
			recommendCache.put(String.valueOf(score), results);
		}
		return results;
	}
	
	public void clearCache() {
		if(recommendCache != null) {
			recommendCache.clear();
		}
	}

}
