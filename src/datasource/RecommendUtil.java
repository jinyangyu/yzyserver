package datasource;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import demo.bean.CollegeModel;

public class RecommendUtil {
	public static Logger logger1 = Logger.getLogger(RecommendUtil.class);
	private static final RecommendUtil util = new RecommendUtil();
	private static final int MAX_RECOMMEND_COUNT = 100;

	private RecommendUtil() {

	}

	public static RecommendUtil getInstance() {
		return util;
	}

	public synchronized List<CollegeModel> recommendCollege(List<CollegeModel> colleges, int score, boolean isWen) {
		logger1.info("recommend hight score:" + score);
		int scoreLow = score - 20;
		logger1.info("recommend low score:" + scoreLow);
		int firstIndex = 0;
		int endIndex = 0;

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

			int line = Integer.parseInt(colleges.get(i).getWen_2017());
			if (!isWen) {
				line = Integer.parseInt(colleges.get(i).getLi_2017());
			}

			logger1.info(i + " line score:" + line);

			if (line < score && firstIndex == 0) {
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

		if (endIndex - firstIndex > MAX_RECOMMEND_COUNT) {
			endIndex = firstIndex + MAX_RECOMMEND_COUNT;
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
}
