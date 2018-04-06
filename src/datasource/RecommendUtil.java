package datasource;

import java.util.List;

import demo.bean.CollegeModel;

public class RecommendUtil {

	private static final RecommendUtil util = new RecommendUtil();

	private RecommendUtil() {

	}

	public static RecommendUtil getInstance() {
		return util;
	}

	public synchronized List<CollegeModel> recommendCollege(List<CollegeModel> colleges, int score) {
		int scoreLow = score - 10;
		int firstIndex = 0;
		int endIndex = 0;
		for (int i = 0; i < colleges.size(); i++) {
			int line = Integer.parseInt(colleges.get(i).getWen_2017());
			if (line < score) {
				firstIndex = i;
			}

			if (line < scoreLow) {
				endIndex = i - 1;
			}
		}

		return colleges.subList(firstIndex, endIndex);
	}
}
