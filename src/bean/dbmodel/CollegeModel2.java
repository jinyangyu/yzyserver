package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class CollegeModel2 extends Model<CollegeModel2> {
	public static final CollegeModel2 dao = new CollegeModel2().dao();

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
	// private String survey;
	// private String national_key_discipline;
	// private String characteristic_specialty;
	// private String state_key_laboratories;
	// private String first_class_subject;
	// private String campus_scenery;
	private String wen_2017;
	private String wen_2016;
	private String wen_2015;
	// private String wen_2014;
	// private String wen_2013;
	private String li_2017;
	private String li_2016;
	private String li_2015;
	// private String li_2014;
	// private String li_2013;
	
	private int wen_2017_rank;
	private int wen_2016_rank;
	private int wen_2015_rank;
	
	private int li_2017_rank;
	private int li_2016_rank;
	private int li_2015_rank;

	// 非数据库字段
	private boolean is211;
	private boolean is985;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCharacteristic() {
		return characteristic;
	}
	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBuild_time() {
		return build_time;
	}
	public void setBuild_time(String build_time) {
		this.build_time = build_time;
	}
	public String getImport_subject_count() {
		return import_subject_count;
	}
	public void setImport_subject_count(String import_subject_count) {
		this.import_subject_count = import_subject_count;
	}
	public String getDoctoral_program_count() {
		return doctoral_program_count;
	}
	public void setDoctoral_program_count(String doctoral_program_count) {
		this.doctoral_program_count = doctoral_program_count;
	}
	public String getMaster_program_count() {
		return master_program_count;
	}
	public void setMaster_program_count(String master_program_count) {
		this.master_program_count = master_program_count;
	}
	public String getBelong() {
		return belong;
	}
	public void setBelong(String belong) {
		this.belong = belong;
	}
	public String getAcademician_count() {
		return academician_count;
	}
	public void setAcademician_count(String academician_count) {
		this.academician_count = academician_count;
	}
	public String getStudent_count() {
		return student_count;
	}
	public void setStudent_count(String student_count) {
		this.student_count = student_count;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOfficial_website() {
		return official_website;
	}
	public void setOfficial_website(String official_website) {
		this.official_website = official_website;
	}
	public String getEnrolment_website() {
		return enrolment_website;
	}
	public void setEnrolment_website(String enrolment_website) {
		this.enrolment_website = enrolment_website;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCollege_id() {
		return college_id;
	}
	public void setCollege_id(String college_id) {
		this.college_id = college_id;
	}
	public String getWen_2017() {
		return wen_2017;
	}
	public void setWen_2017(String wen_2017) {
		this.wen_2017 = wen_2017;
	}
	public String getWen_2016() {
		return wen_2016;
	}
	public void setWen_2016(String wen_2016) {
		this.wen_2016 = wen_2016;
	}
	public String getWen_2015() {
		return wen_2015;
	}
	public void setWen_2015(String wen_2015) {
		this.wen_2015 = wen_2015;
	}
	public String getLi_2017() {
		return li_2017;
	}
	public void setLi_2017(String li_2017) {
		this.li_2017 = li_2017;
	}
	public String getLi_2016() {
		return li_2016;
	}
	public void setLi_2016(String li_2016) {
		this.li_2016 = li_2016;
	}
	public String getLi_2015() {
		return li_2015;
	}
	public void setLi_2015(String li_2015) {
		this.li_2015 = li_2015;
	}
	public int getWen_2017_rank() {
		return wen_2017_rank;
	}
	public void setWen_2017_rank(int wen_2017_rank) {
		this.wen_2017_rank = wen_2017_rank;
	}
	public int getWen_2016_rank() {
		return wen_2016_rank;
	}
	public void setWen_2016_rank(int wen_2016_rank) {
		this.wen_2016_rank = wen_2016_rank;
	}
	public int getWen_2015_rank() {
		return wen_2015_rank;
	}
	public void setWen_2015_rank(int wen_2015_rank) {
		this.wen_2015_rank = wen_2015_rank;
	}
	public int getLi_2017_rank() {
		return li_2017_rank;
	}
	public void setLi_2017_rank(int li_2017_rank) {
		this.li_2017_rank = li_2017_rank;
	}
	public int getLi_2016_rank() {
		return li_2016_rank;
	}
	public void setLi_2016_rank(int li_2016_rank) {
		this.li_2016_rank = li_2016_rank;
	}
	public int getLi_2015_rank() {
		return li_2015_rank;
	}
	public void setLi_2015_rank(int li_2015_rank) {
		this.li_2015_rank = li_2015_rank;
	}
	public boolean isIs211() {
		return is211;
	}
	public void setIs211(boolean is211) {
		this.is211 = is211;
	}
	public boolean isIs985() {
		return is985;
	}
	public void setIs985(boolean is985) {
		this.is985 = is985;
	}
	public static CollegeModel2 getDao() {
		return dao;
	}

	
}
