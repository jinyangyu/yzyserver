package datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.json.Json;

import bean.dbmodel.CollegeModel;
import bean.dbmodel.CollegeModelAll;
import bean.dbmodel.LiSchoolScore2018;
import bean.dbmodel.WenSchoolScore2018;

public class RecommendSource {

	public static Logger LOGGER = Logger.getLogger(RecommendSource.class);

	private static final RecommendSource instance = new RecommendSource();

	private List<LiSchoolScore2018> li2018List;
	private List<WenSchoolScore2018> wen2018List;

	private List<LiSchoolScore2018> noCollege = new ArrayList<LiSchoolScore2018>();
	private List<LiSchoolScore2018> errorCollege = new ArrayList<LiSchoolScore2018>();

	private RecommendSource() {
	}

	public static RecommendSource getInstance() {
		return instance;
	}

	public void init() {
		li2018List = LiSchoolScore2018.dao.find("select * from li_2018 order by score desc");

		for (LiSchoolScore2018 li : li2018List) {

			CollegeModel college = CollegeModel.dao.findFirst("select "
					+ "id,name, area,characteristic,type,build_time,import_subject_count,doctoral_program_count,"
					+ "master_program_count,belong,academician_count,student_count,telephone,"
					+ "address,official_website,enrolment_website,logo," + "wen_2018,wen_2017,wen_2016,wen_2015,"
					+ "li_2018,li_2017,li_2016,li_2015,province,college_id,"
					+ "li_2018_rank,li_2017_rank,li_2016_rank,li_2015_rank,"
					+ "wen_2018_rank,wen_2017_rank,wen_2016_rank,wen_2015_rank " + "from college "
					+ "where college_id = ?", li.getCode());

			if (college != null) {
				CollegeModelAll collegeAll = CollegeModelAll.copyFromCollege(college);
				collegeAll.setName(li.getName());
				collegeAll.setLi_2018(String.valueOf(li.getScore()));
				collegeAll.save();
			} else {
				noCollege.add(li);
			}
		}

		for (LiSchoolScore2018 li : noCollege) {
			String name = li.getName().substring(0, 4);

			CollegeModel college = CollegeModel.dao.findFirst("select "
					+ "id,name, area,characteristic,type,build_time,import_subject_count,doctoral_program_count,"
					+ "master_program_count,belong,academician_count,student_count,telephone,"
					+ "address,official_website,enrolment_website,logo," + "wen_2018,wen_2017,wen_2016,wen_2015,"
					+ "li_2018,li_2017,li_2016,li_2015,province,college_id,"
					+ "li_2018_rank,li_2017_rank,li_2016_rank,li_2015_rank,"
					+ "wen_2018_rank,wen_2017_rank,wen_2016_rank,wen_2015_rank " + "from college "
					+ "where name like ?", name + "%");

			if (college != null) {
				CollegeModelAll collegeAll = CollegeModelAll.copyFromCollege(college);
				collegeAll.setName(li.getName());
				collegeAll.setCollege_id(String.valueOf(li.getCode()));
				collegeAll.setLi_2018(String.valueOf(li.getScore()));
				collegeAll.setLi_2017("");
				collegeAll.setLi_2016("");
				collegeAll.setLi_2015("");

				WenSchoolScore2018 wen = WenSchoolScore2018.dao.findFirst("select * from wen_2018 where name = ?",
						li.getName());

				collegeAll.setWen_2018(wen == null ? "" : String.valueOf(wen.getScore()));
				collegeAll.setWen_2017("");
				collegeAll.setWen_2016("");
				collegeAll.setWen_2015("");
				collegeAll.save();
			} else {
				errorCollege.add(li);
			}
		}

		for (LiSchoolScore2018 li : errorCollege) {
			LOGGER.info("wen2018List:" + Json.getJson().toJson(li) + " NO COLLEGE " + li.getName().substring(0, 4));
		}

	}

	public void fixWen() {
		wen2018List = WenSchoolScore2018.dao.find("select * from wen_2018 order by score desc");

		List<CollegeModelAll> allCollege = DataSource.getInstance().getAllColleges();
		for (CollegeModelAll college : allCollege) {
			String name = college.getName();
			int wen_2018 = 0;
			try {
				wen_2018 = Integer.parseInt(college.getWen_2018());
			} catch (Exception e) {
			}

			if (name.contains("（较高收费）")) {
				LOGGER.info("name:" + name + " from wen_2018:" + wen_2018);
				// name = name.substring(0, name.length() - "（较高收费）".length());
				WenSchoolScore2018 wenSchool = WenSchoolScore2018.dao
						.findFirst("select * from wen_2018 where name = ? order by score desc", name);
				college.setWen_2018(wenSchool == null ? "" : String.valueOf(wenSchool.getScore()));
				college.update();
				LOGGER.info("name:" + name + " to wen_2018:" + college.getWen_2018());
			}
			/*
			 * if (name.contains("（一本）") && wen_2018 < 547) { LOGGER.info("name:" + name +
			 * " wen_2018:" + wen_2018); // name = name.substring(0, name.length() -
			 * "（一本）".length()); // List<WenSchoolScore2018> wenSchools =
			 * WenSchoolScore2018.dao //
			 * .find("select * from wen_2018 where name = ? order by score desc", name); //
			 * for (WenSchoolScore2018 sc : wenSchools) { // fix(college, sc); // } }
			 * 
			 * if (name.contains("（二本）") && wen_2018 > 547) { LOGGER.info("name:" + name +
			 * " wen_2018:" + wen_2018); // name = name.substring(0, name.length() -
			 * "（一本）".length()); // List<WenSchoolScore2018> wenSchools =
			 * WenSchoolScore2018.dao //
			 * .find("select * from wen_2018 where name = ? order by score desc", name); //
			 * for (WenSchoolScore2018 sc : wenSchools) { // fix(college, sc); // } } if
			 * (name.contains("（专科）") && wen_2018 > 436) { LOGGER.info("name:" + name +
			 * " wen_2018:" + wen_2018); // name = name.substring(0, name.length() -
			 * "（一本）".length()); // List<WenSchoolScore2018> wenSchools =
			 * WenSchoolScore2018.dao //
			 * .find("select * from wen_2018 where name = ? order by score desc", name); //
			 * for (WenSchoolScore2018 sc : wenSchools) { // fix(college, sc); // } }
			 */
		}
	}

	private void fix(CollegeModelAll college, WenSchoolScore2018 wenSchool) {
		int li_2018 = 0;
		try {
			li_2018 = Integer.parseInt(college.getLi_2018());
		} catch (Exception e) {
		}

		if (li_2018 >= 499 && wenSchool.getScore() >= 547) {
			// 一本
			college.setWen_2018(String.valueOf(wenSchool.getScore()));
			college.update();
		}
		if (li_2018 < 499 && li_2018 > 374 && wenSchool.getScore() < 547 && wenSchool.getScore() > 436) {
			// 二本
			college.setWen_2018(String.valueOf(wenSchool.getScore()));
			college.update();
		}
		if (li_2018 < 374 && wenSchool.getScore() < 436) {
			// 专科
			college.setWen_2018(String.valueOf(wenSchool.getScore()));
			college.update();
		}
	}

	public static void main(String[] args) {
		String name = "河南工业大学（专科）";
		name = name.substring(0, name.length() - "（一本）".length());
		System.out.println("name:" + name);
	}
}
