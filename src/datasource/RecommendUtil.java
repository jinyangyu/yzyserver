package datasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import bean.dbmodel.CollegeModelAll;

public class RecommendUtil {
	public static Logger logger1 = Logger.getLogger(RecommendUtil.class);
	private static final RecommendUtil util = new RecommendUtil();
	public static final int SCORE_RANGE = 22;

	private RecommendUtil() {

	}

	public static RecommendUtil getInstance() {
		return util;
	}

	public synchronized List<CollegeModelAll> recommendCollege(List<CollegeModelAll> colleges, int score,
			boolean isWen) {

		int scoreLow = score - SCORE_RANGE;
		int scoreHigh = score + 5;

		int firstIndex = -1;
		int endIndex = 0;

		// 超高分兼容
		if (isWen) {
			if (score > Integer.parseInt(colleges.get(0).getWen_2018())) {
				scoreLow = Integer.parseInt(colleges.get(0).getWen_2018()) - 40;
			}
		} else {
			if (scoreLow > Integer.parseInt(colleges.get(0).getLi_2018())) {
				scoreLow = Integer.parseInt(colleges.get(0).getLi_2018()) - 40;
			}
		}

		for (int i = 0; i < colleges.size(); i++) {
			if (isWen) {
				if ("".equals(colleges.get(i).getWen_2018())) {
					continue;
				}
			} else {
				if ("".equals(colleges.get(i).getLi_2018())) {
					continue;
				}
			}

			int line = 0;
			if (isWen) {
				line = Integer.parseInt(colleges.get(i).getWen_2018());
			} else {
				line = Integer.parseInt(colleges.get(i).getLi_2018());
			}

			// logger1.info(i + " line score:" + line);

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

		List<CollegeModelAll> resultList = new ArrayList<CollegeModelAll>();

		logger1.info("add result from:" + firstIndex + " to " + endIndex);

		for (int i = firstIndex; i <= endIndex; i++) {
			resultList.add(colleges.get(i));
		}

		List<CollegeModelAll> appendList = getHenanRecommendCollege(isWen, scoreLow);
		resultList.addAll(appendList);

		logger1.info("score " + score + " recommend =================");
		for (int i = 0; i < resultList.size(); i++) {
			if (isWen) {
				logger1.info(resultList.get(i).getName() + " " + resultList.get(i).getWen_2018());
			} else {
				logger1.info(resultList.get(i).getName() + " " + resultList.get(i).getLi_2018());
			}
		}

		logger1.info("==============================================");

		Set<String> nameSet = new HashSet<String>();
		Iterator<CollegeModelAll> iterator = resultList.iterator();
		if (iterator != null) {
			while (iterator.hasNext()) {
				String name = iterator.next().getName();
				if (nameSet.contains(name)) {
					logger1.info("=============学校重复，删除:" + name);
					iterator.remove();
				} else {
					nameSet.add(name);
				}
			}
		}

		logger1.info("============推荐院校共：" + resultList.size());
		return resultList;
	}

	public List<CollegeModelAll> getHenanRecommendCollege(boolean isWen, int score) {
		List<CollegeModelAll> appendList = new ArrayList<CollegeModelAll>();
		for (CollegeModelAll model : DataSource.getInstance().getHenanCollege(isWen)) {
			int line = score;
			try {
				line = Integer.parseInt(isWen ? model.getWen_2018() : model.getLi_2018());
			} catch (Exception e) {
			}
			if (line < score) {
				appendList.add(model);
			}
			if (appendList.size() >= 20) {
				break;
			}
		}
		return appendList;
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
