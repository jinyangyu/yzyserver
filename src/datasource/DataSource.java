package datasource;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;

import controller.CollegeRecommendController;
import demo.bean.CollegeModel;

public class DataSource {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	private static DataSource dataSource = new DataSource();
	private List<CollegeModel> allColleges;
	
	private IRecommendSource recommendSource_li;
	private IRecommendSource recommendSource_wen;

	private DataSource() {
		logger1.info("DataSource init");
		long start = System.currentTimeMillis();
		allColleges = CollegeModel.dao.find("select * from college where id < 100");
		logger1.info("DataSource init colleges use:" + (System.currentTimeMillis() - start) + " ms");

		start = System.currentTimeMillis();
		
		recommendSource_li = new LiRecommend();
		recommendSource_wen = new WenRecommend();
		
		Collections.sort(allColleges, liComp);
		recommendSource_li.setCollege(allColleges);
		
		Collections.sort(allColleges, wenComp);
		recommendSource_wen.setCollege(allColleges);
		
		logger1.info("DataSource init colleges sort by li_2017 use:" + (System.currentTimeMillis() - start) + " ms");

		for (int i = 0; i < 30; i++) {
			System.out.println(allColleges.get(i).getName() + " li_2017:" + allColleges.get(i).getLi_2017());
		}
	}
	
	public static DataSource getInstance() {
		return dataSource;
	}

	public List<CollegeModel> recommendCollege(String score,boolean isWen) {
		if(isWen) {
			return recommendSource_wen.recommendCollege(score);
		}
		return recommendSource_li.recommendCollege(score);

//		colleges = CollegeModel.dao.find("select * from college where id < 4");
//		recommendCache.put(String.valueOf(score), colleges);
//		return colleges;
	}
	
	private Comparator liComp = new Comparator<CollegeModel>() {
		public int compare(CollegeModel arg0, CollegeModel arg1) {
			int li_2017_0 = 0;
			int li_2017_1 = 0;
			try {
				li_2017_0 = Integer.parseInt(arg0.getLi_2017());
			} catch (Exception e) {
			}

			try {
				li_2017_1 = Integer.parseInt(arg1.getLi_2017());
			} catch (Exception e) {
			}
			return li_2017_1 - li_2017_0;
		}
	};
	
	private Comparator wenComp = new Comparator<CollegeModel>() {
		public int compare(CollegeModel arg0, CollegeModel arg1) {
			int wen_2017_0 = 0;
			int wen_2017_1 = 0;
			try {
				wen_2017_0 = Integer.parseInt(arg0.getWen_2017());
			} catch (Exception e) {
			}

			try {
				wen_2017_1 = Integer.parseInt(arg1.getWen_2017());
			} catch (Exception e) {
			}
			return wen_2017_1 - wen_2017_0;
		}
	};

}
