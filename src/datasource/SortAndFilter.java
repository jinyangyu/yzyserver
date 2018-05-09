package datasource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

import bean.dbmodel.CollegeModel;
import bean.requestresult.CollegeChanceResult;

public class SortAndFilter {

	public static Logger logger1 = Logger.getLogger(SortAndFilter.class);

	private List<CollegeChanceResult> colleges;
	private String sortKey;
	private String filters;
	private String provinces;
	private boolean isWen;

	private static HashMap<String, Comparator<CollegeChanceResult>> compMap = new HashMap<String, Comparator<CollegeChanceResult>>();
	static {
		Comparator<CollegeChanceResult> probabilityComp = new Comparator<CollegeChanceResult>() {

			public int compare(CollegeChanceResult o1, CollegeChanceResult o2) {
				return o1.getChance() - o2.getChance();
			}
		};
		compMap.put("probability_wen", probabilityComp);
		compMap.put("probability_li", probabilityComp);

		compMap.put("admitLine_wen", new Comparator<CollegeChanceResult>() {

			public int compare(CollegeChanceResult o1, CollegeChanceResult o2) {
				int wen1 = Integer.parseInt(o1.getCollege().getWen_2017());
				int wen2 = Integer.parseInt(o2.getCollege().getWen_2017());
				return wen1 - wen2;
			}
		});

		compMap.put("admitLine_li", new Comparator<CollegeChanceResult>() {

			public int compare(CollegeChanceResult o1, CollegeChanceResult o2) {
				int li1 = Integer.parseInt(o1.getCollege().getLi_2017());
				int li2 = Integer.parseInt(o2.getCollege().getLi_2017());
				return li1 - li2;
			}
		});
	}

	public SortAndFilter(List<CollegeChanceResult> colleges, String sortKey, String filters, String provinces,
			boolean isWen) {
		this.colleges = new ArrayList<CollegeChanceResult>();
		this.colleges.addAll(colleges);

		this.sortKey = sortKey;
		this.filters = filters;
		this.provinces = provinces;
		this.isWen = isWen;
	}

	public List<CollegeChanceResult> getResults() {
		doFilter();
		doProvinces();
		doSort();
		return colleges;
	}

	private void doProvinces() {
		if (provinces == null || "".equals(provinces)) {
			logger1.info("doProvinces provinces null return");
			return;
		}

		String provincesArray[] = provinces.split(",");
		Set<String> provincesSet = new HashSet<String>();
		for (String s : provincesArray) {
			provincesSet.add(s);

			logger1.info("doProvinces provinces set add:" + s);
		}

		List<CollegeChanceResult> results = new ArrayList<CollegeChanceResult>();
		for (CollegeChanceResult college : colleges) {

			logger1.info("doProvinces college:" + college.getCollege().getName() + " province:"
					+ college.getCollege().getProvince() + " contains:"
					+ provincesSet.contains(college.getCollege().getProvince()));

			if (provincesSet.contains(college.getCollege().getProvince())) {
				results.add(college);
			}
		}

		colleges.clear();
		colleges.addAll(results);
		logger1.info("doProvinces finish size:" + colleges.size());
	}

	private void doFilter() {
		if (filters == null || "".equals(filters)) {
			logger1.info("doFilter filters null return");
			return;
		}

		String filterArray[] = filters.split(",");
		Set<String> filterSet = new HashSet<String>();
		for (String s : filterArray) {
			filterSet.add(s);
		}

		List<CollegeChanceResult> results = new ArrayList<CollegeChanceResult>();
		for (CollegeChanceResult college : colleges) {
			if (filterSet.contains(college.getCollege().getType())) {
				results.add(college);
			}
		}

		colleges.clear();
		colleges.addAll(results);
		logger1.info("doFilter finish size:" + colleges.size());
	}

	private void doSort() {
		if (sortKey == null || "".equals(sortKey)) {
			sortKey = "probability";
		}

		String key = isWen ? sortKey + "_wen" : sortKey + "_li";
		logger1.info("doSort sortKey:" + key);

		if (compMap.containsKey(key)) {
			Collections.sort(colleges, compMap.get(key));
		}
	}

}
