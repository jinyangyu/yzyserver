package datasource;

import java.util.HashMap;
import java.util.List;

import demo.bean.CollegeModel;

public class DataSource {

	private static DataSource dataSource = new DataSource();
	private HashMap<String, List<CollegeModel>> recommendCache;

	private DataSource() {
		recommendCache = new HashMap<String, List<CollegeModel>>();
	}

	public static DataSource getInstance() {
		return dataSource;
	}

	public List<CollegeModel> recommendCollege(String score) {
		List<CollegeModel> colleges = null;
		if (recommendCache.containsKey(score)) {
			colleges = recommendCache.get(score);
		}

		if (colleges != null && !colleges.isEmpty()) {
			return colleges;
		}

		colleges = CollegeModel.dao.find("select * from college where id < 4");
		recommendCache.put(String.valueOf(score), colleges);
		return colleges;
	}

}
