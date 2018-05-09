package datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import bean.dbmodel.CollegeModel;

public class RecommendUtil {
	public static Logger logger1 = Logger.getLogger(RecommendUtil.class);
	private static final RecommendUtil util = new RecommendUtil();
	public static final int SCORE_RANGE = 20;

	private RecommendUtil() {

	}

	public static RecommendUtil getInstance() {
		return util;
	}

	public synchronized List<CollegeModel> recommendCollege(List<CollegeModel> colleges, int score, boolean isWen) {
		
		int scoreLow = score - SCORE_RANGE;
		int scoreHigh = score + 5;
		
		int firstIndex = -1;
		int endIndex = 0;

		// 超高分兼容
		if (isWen) {
			if (score > Integer.parseInt(colleges.get(0).getWen_2017())) {
				scoreLow = Integer.parseInt(colleges.get(0).getWen_2017()) - 40;
			}
		} else {
			if (scoreLow > Integer.parseInt(colleges.get(0).getLi_2017())) {
				scoreLow = Integer.parseInt(colleges.get(0).getLi_2017()) - 40;
			}
		}
		
		for (int i = 0; i < colleges.size(); i++) {
			if (isWen) {
				if ("".equals(colleges.get(i).getWen_2017())) {
					continue;
				}
			} else {
				if ("".equals(colleges.get(i).getLi_2017())) {
					continue;
				}
			}

			int line = 0;
			if (isWen) {
				line = Integer.parseInt(colleges.get(i).getWen_2017());
			} else {
				line = Integer.parseInt(colleges.get(i).getLi_2017());
			}

			logger1.info(i + " line score:" + line);

			if (line <= scoreHigh && firstIndex == -1) {
				firstIndex = i;
				logger1.info("firstIndex:" + firstIndex);
			}

			if (line < scoreLow) {
				endIndex = i - 1;
				logger1.info("endIndex:" + endIndex);
				break;
			}
		}

		if (firstIndex > endIndex || endIndex >= colleges.size()) {
			return null;
		}

		List<CollegeModel> resultList = new ArrayList<CollegeModel>();

		logger1.info("add result from:" + firstIndex + " to " + endIndex);

		for (int i = firstIndex; i <= endIndex; i++) {
			resultList.add(colleges.get(i));
		}

		logger1.info("score " + score + " recommend =================");
		for (int i = 0; i < resultList.size(); i++) {
			if (isWen) {
				logger1.info(resultList.get(i).getName() + " " + resultList.get(i).getWen_2017());
			} else {
				logger1.info(resultList.get(i).getName() + " " + resultList.get(i).getLi_2017());
			}
		}

		logger1.info("==============================================");
		return resultList;
	}

	public static void main(String[] args) {
		SortedMap<String, Integer> map = new TreeMap(); // SortedMap接收TreeMap的实例
		map.put("504", 54);
		map.put("510", 67);
		map.put("430", 24);
		map.put("502", 52);
		map.put("450", 38);
		map.put("480", 48);
		map.put("502", 52);
		map.put("502", 53);

		for (String key : map.keySet()) {
			System.out.println(key + " " + map.get(key) + "%");
		}

	}
}
