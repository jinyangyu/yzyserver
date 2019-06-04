package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class CollegeModelAll extends Model<CollegeModelAll> {
	public static final CollegeModelAll dao = new CollegeModelAll().dao();

	private int id;
	private String name;
	private String area;
	private String characteristic;
	private String type;
	private String build_time;
	private String import_subject_count;
	private String doctoral_program_count;
	private String master_program_count;
	private String belong;
	private String academician_count;
	private String student_count;
	private String telephone;
	private String address;
	private String official_website;
	private String enrolment_website;
	private String logo;
	private String province;
	private String college_id;
	private String wen_2018;
	private String wen_2017;
	private String wen_2016;
	private String wen_2015;
	private String li_2018;
	private String li_2017;
	private String li_2016;
	private String li_2015;

	private int wen_2018_rank;
	private int wen_2017_rank;
	private int wen_2016_rank;
	private int wen_2015_rank;

	private int li_2018_rank;
	private int li_2017_rank;
	private int li_2016_rank;
	private int li_2015_rank;

	// 非数据库字段
	private boolean is211;
	private boolean is985;

	public static CollegeModelAll copyFromCollege(CollegeModel college) {
		CollegeModelAll collegeAll = new CollegeModelAll();
		collegeAll.setName(college.getName());
		collegeAll.setArea(college.getArea());
		collegeAll.setCharacteristic(college.getCharacteristic());
		collegeAll.setType(college.getType());
		collegeAll.setBuild_time(college.getBuild_time());
		collegeAll.setImport_subject_count(college.getImport_subject_count());
		collegeAll.setDoctoral_program_count(college.getDoctoral_program_count());
		collegeAll.setMaster_program_count(college.getMaster_program_count());
		collegeAll.setBelong(college.getBelong());
		collegeAll.setAcademician_count(college.getAcademician_count());
		collegeAll.setStudent_count(college.getStudent_count());
		collegeAll.setTelephone(college.getTelephone());
		collegeAll.setAddress(college.getAddress());
		collegeAll.setOfficial_website(college.getOfficial_website());
		collegeAll.setEnrolment_website(college.getEnrolment_website());
		collegeAll.setLogo(college.getLogo());
		collegeAll.setProvince(college.getProvince());
		collegeAll.setCollege_id(college.getCollege_id());
		collegeAll.setWen_2018(college.getWen_2018());
		collegeAll.setWen_2017(college.getWen_2017());
		collegeAll.setWen_2016(college.getWen_2016());
		collegeAll.setWen_2015(college.getWen_2015());
		collegeAll.setLi_2018(college.getLi_2018());
		collegeAll.setLi_2017(college.getLi_2017());
		collegeAll.setLi_2016(college.getLi_2016());
		collegeAll.setLi_2015(college.getLi_2015());
		collegeAll.setWen_2018_rank(college.getWen_2018_rank());
		collegeAll.setWen_2017_rank(college.getWen_2017_rank());
		collegeAll.setWen_2016_rank(college.getWen_2016_rank());
		collegeAll.setWen_2015_rank(college.getWen_2015_rank());
		collegeAll.setLi_2018_rank(college.getLi_2018_rank());
		collegeAll.setLi_2017_rank(college.getLi_2017_rank());
		collegeAll.setLi_2016_rank(college.getLi_2016_rank());
		collegeAll.setLi_2015_rank(college.getLi_2015_rank());
		collegeAll.setIs211(college.isIs211());
		collegeAll.setIs985(college.isIs985());
		return collegeAll;
	}

	private static final String id_NAME = "id";
	private static final String name_NAME = "name";
	private static final String area_NAME = "area";
	private static final String characteristic_NAME = "characteristic";
	private static final String type_NAME = "type";
	private static final String build_time_NAME = "build_time";
	private static final String import_subject_count_NAME = "import_subject_count";
	private static final String doctoral_program_count_NAME = "doctoral_program_count";
	private static final String master_program_count_NAME = "master_program_count";
	private static final String belong_NAME = "belong";
	private static final String academician_count_NAME = "academician_count";
	private static final String student_count_NAME = "student_count";
	private static final String telephone_NAME = "telephone";
	private static final String address_NAME = "address";
	private static final String official_website_NAME = "official_website";
	private static final String enrolment_website_NAME = "enrolment_website";
	private static final String logo_NAME = "logo";
	private static final String province_NAME = "province";
	private static final String college_id_NAME = "college_id";

	private static final String wen_2018_NAME = "wen_2018";
	private static final String wen_2017_NAME = "wen_2017";
	private static final String wen_2016_NAME = "wen_2016";
	private static final String wen_2015_NAME = "wen_2015";
	private static final String li_2018_NAME = "li_2018";
	private static final String li_2017_NAME = "li_2017";
	private static final String li_2016_NAME = "li_2016";
	private static final String li_2015_NAME = "li_2015";

	private static final String wen_2018_rank_NAME = "wen_2018_rank";
	private static final String wen_2017_rank_NAME = "wen_2017_rank";
	private static final String wen_2016_rank_NAME = "wen_2016_rank";
	private static final String wen_2015_rank_NAME = "wen_2015_rank";
	private static final String li_2018_rank_NAME = "li_2018_rank";
	private static final String li_2017_rank_NAME = "li_2017_rank";
	private static final String li_2016_rank_NAME = "li_2016_rank";
	private static final String li_2015_rank_NAME = "li_2015_rank";

	public int getWen_2018_rank() {
		return getInt(wen_2018_rank_NAME);
	}

	public void setWen_2018_rank(int wen_2018_rank) {
		set(wen_2018_rank_NAME, wen_2018_rank);
	}

	public int getWen_2017_rank() {
		return getInt(wen_2017_rank_NAME);
	}

	public void setWen_2017_rank(int wen_2017_rank) {
		set(wen_2017_rank_NAME, wen_2017_rank);
	}

	public int getWen_2016_rank() {
		return getInt(wen_2016_rank_NAME);
	}

	public void setWen_2016_rank(int wen_2016_rank) {
		set(wen_2016_rank_NAME, wen_2016_rank);
	}

	public int getWen_2015_rank() {
		return getInt(wen_2015_rank_NAME);
	}

	public void setWen_2015_rank(int wen_2015_rank) {
		set(wen_2015_rank_NAME, wen_2015_rank);
	}

	public int getLi_2018_rank() {
		return getInt(li_2018_rank_NAME);
	}

	public void setLi_2018_rank(int li_2018_rank) {
		set(li_2018_rank_NAME, li_2018_rank);
	}

	public int getLi_2017_rank() {
		return getInt(li_2017_rank_NAME);
	}

	public void setLi_2017_rank(int li_2017_rank) {
		set(li_2017_rank_NAME, li_2017_rank);
	}

	public int getLi_2016_rank() {
		return getInt(li_2016_rank_NAME);
	}

	public void setLi_2016_rank(int li_2016_rank) {
		set(li_2016_rank_NAME, li_2016_rank);
	}

	public int getLi_2015_rank() {
		return getInt(li_2015_rank_NAME);
	}

	public void setLi_2015_rank(int li_2015_rank) {
		set(li_2015_rank_NAME, li_2015_rank);
	}

	public int getId() {
		return getInt(id_NAME);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return getStr(name_NAME);
	}

	public void setName(String name) {
		set(name_NAME, name);
	}

	public String getArea() {
		return getStr(area_NAME);
	}

	public void setArea(String area) {
		set(area_NAME, area);
	}

	public String getCharacteristic() {
		return getStr(characteristic_NAME);
	}

	public void setCharacteristic(String characteristic) {
		set(characteristic_NAME, characteristic);
	}

	public String getType() {
		return getStr(type_NAME);
	}

	public void setType(String type) {
		set(type_NAME, type);
	}

	public String getBuild_time() {
		return getStr(build_time_NAME);
	}

	public void setBuild_time(String build_time) {
		set(build_time_NAME, build_time);
	}

	public String getImport_subject_count() {
		return getStr(import_subject_count_NAME);
	}

	public void setImport_subject_count(String import_subject_count) {
		set(import_subject_count_NAME, import_subject_count);
	}

	public String getDoctoral_program_count() {
		return getStr(doctoral_program_count_NAME);
	}

	public void setDoctoral_program_count(String doctoral_program_count) {
		set(doctoral_program_count_NAME, doctoral_program_count);
	}

	public String getMaster_program_count() {
		return getStr(master_program_count_NAME);
	}

	public void setMaster_program_count(String master_program_count) {
		set(master_program_count_NAME, master_program_count);
	}

	public String getBelong() {
		return getStr(belong_NAME);
	}

	public void setBelong(String belong) {
		set(belong_NAME, belong);
	}

	public String getAcademician_count() {
		return getStr(academician_count_NAME);
	}

	public void setAcademician_count(String academician_count) {
		set(academician_count_NAME, academician_count);
	}

	public String getStudent_count() {
		return getStr(student_count_NAME);
	}

	public void setStudent_count(String student_count) {
		set(student_count_NAME, student_count);
	}

	public String getTelephone() {
		return getStr(telephone_NAME);
	}

	public void setTelephone(String telephone) {
		set(telephone_NAME, telephone);
	}

	public String getAddress() {
		return getStr(address_NAME);
	}

	public void setAddress(String address) {
		set(address_NAME, address);
	}

	public String getOfficial_website() {
		return getStr(official_website_NAME);
	}

	public void setOfficial_website(String official_website) {
		set(official_website_NAME, official_website);
	}

	public String getEnrolment_website() {
		return getStr(enrolment_website_NAME);
	}

	public void setEnrolment_website(String enrolment_website) {
		set(enrolment_website_NAME, enrolment_website);
	}

	public String getLogo() {
		return getStr(logo_NAME);
	}

	public void setLogo(String logo) {
		set(logo_NAME, logo);
	}

	public String getProvince() {
		return getStr(province_NAME);
	}

	public void setProvince(String province) {
		set(province_NAME, province);
	}

	public String getCollege_id() {
		return getStr(college_id_NAME);
	}

	public void setCollege_id(String college_id) {
		set(college_id_NAME, college_id);
	}

	public String getWen_2018() {
		return getStr(wen_2018_NAME);
	}

	public void setWen_2018(String wen_2018) {
		set(wen_2018_NAME, wen_2018);
	}

	public String getWen_2017() {
		return getStr(wen_2017_NAME);
	}

	public void setWen_2017(String wen_2017) {
		set(wen_2017_NAME, wen_2017);
	}

	public String getWen_2016() {
		return getStr(wen_2016_NAME);
	}

	public void setWen_2016(String wen_2016) {
		set(wen_2016_NAME, wen_2016);
	}

	public String getWen_2015() {
		return getStr(wen_2015_NAME);
	}

	public void setWen_2015(String wen_2015) {
		set(wen_2015_NAME, wen_2015);
	}

	public String getLi_2018() {
		return getStr(li_2018_NAME);
	}

	public void setLi_2018(String li_2018) {
		set(li_2018_NAME, li_2018);
	}

	public String getLi_2017() {
		return getStr(li_2017_NAME);
	}

	public void setLi_2017(String li_2017) {
		set(li_2017_NAME, li_2017);
	}

	public String getLi_2016() {
		return getStr(li_2016_NAME);
	}

	public void setLi_2016(String li_2016) {
		set(li_2016_NAME, li_2016);
	}

	public String getLi_2015() {
		return getStr(li_2015_NAME);
	}

	public void setLi_2015(String li_2015) {
		set(li_2015_NAME, li_2015);
	}

	public boolean isIs211() {
		if (getCharacteristic() == null) {
			return false;
		}
		return getCharacteristic().contains("211");
	}

	public void setIs211(boolean is211) {
		this.is211 = is211;
	}

	public boolean isIs985() {
		if (getCharacteristic() == null) {
			return false;
		}
		return getCharacteristic().contains("985");
	}

	public void setIs985(boolean is985) {
		this.is985 = is985;
	}

}
