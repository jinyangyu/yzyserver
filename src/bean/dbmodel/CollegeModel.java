package bean.dbmodel;

import java.util.Random;

import com.jfinal.plugin.activerecord.Model;

public class CollegeModel extends Model<CollegeModel> {
	public static final CollegeModel dao = new CollegeModel().dao();
	private static Random random = new Random(System.currentTimeMillis());

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
	private String survey;
	private String national_key_discipline;
	private String characteristic_specialty;
	private String state_key_laboratories;
	private String first_class_subject;
	private String campus_scenery;
	private String wen_2017;
	private String wen_2016;
	private String wen_2015;
	private String wen_2014;
	private String wen_2013;
	private String li_2017;
	private String li_2016;
	private String li_2015;
	private String li_2014;
	private String li_2013;

	// 非数据库字段
	private boolean is211;
	private boolean is985;
	private String probability;

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
	private static final String survey_NAME = "survey";
	private static final String national_key_discipline_NAME = "national_key_discipline";
	private static final String characteristic_specialty_NAME = "characteristic_specialty";
	private static final String state_key_laboratories_NAME = "state_key_laboratories";
	private static final String first_class_subject_NAME = "first_class_subject";
	private static final String campus_scenery_NAME = "campus_scenery";
	private static final String wen_2017_NAME = "wen_2017";
	private static final String wen_2016_NAME = "wen_2016";
	private static final String wen_2015_NAME = "wen_2015";
	private static final String wen_2014_NAME = "wen_2014";
	private static final String wen_2013_NAME = "wen_2013";
	private static final String li_2017_NAME = "li_2017";
	private static final String li_2016_NAME = "li_2016";
	private static final String li_2015_NAME = "li_2015";
	private static final String li_2014_NAME = "li_2014";
	private static final String li_2013_NAME = "li_2013";

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
		this.name = name;
	}

	public String getArea() {
		return getStr(area_NAME);
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCharacteristic() {
		return getStr(characteristic_NAME);
	}

	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}

	public String getType() {
		return getStr(type_NAME);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBuild_time() {
		return getStr(build_time_NAME);
	}

	public void setBuild_time(String build_time) {
		this.build_time = build_time;
	}

	public String getImport_subject_count() {
		return getStr(import_subject_count_NAME);
	}

	public void setImport_subject_count(String import_subject_count) {
		this.import_subject_count = import_subject_count;
	}

	public String getDoctoral_program_count() {
		return getStr(doctoral_program_count_NAME);
	}

	public void setDoctoral_program_count(String doctoral_program_count) {
		this.doctoral_program_count = doctoral_program_count;
	}

	public String getMaster_program_count() {
		return getStr(master_program_count_NAME);
	}

	public void setMaster_program_count(String master_program_count) {
		this.master_program_count = master_program_count;
	}

	public String getBelong() {
		return getStr(belong_NAME);
	}

	public void setBelong(String belong) {
		this.belong = belong;
	}

	public String getAcademician_count() {
		return getStr(academician_count_NAME);
	}

	public void setAcademician_count(String academician_count) {
		this.academician_count = academician_count;
	}

	public String getStudent_count() {
		return getStr(student_count_NAME);
	}

	public void setStudent_count(String student_count) {
		this.student_count = student_count;
	}

	public String getTelephone() {
		return getStr(telephone_NAME);
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return getStr(address_NAME);
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOfficial_website() {
		return getStr(official_website_NAME);
	}

	public void setOfficial_website(String official_website) {
		this.official_website = official_website;
	}

	public String getEnrolment_website() {
		return getStr(enrolment_website_NAME);
	}

	public void setEnrolment_website(String enrolment_website) {
		this.enrolment_website = enrolment_website;
	}

	public String getLogo() {
		return getStr(logo_NAME);
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getSurvey() {
		return getStr(survey_NAME);
	}

	public void setSurvey(String survey) {
		this.survey = survey;
	}

	public String getNational_key_discipline() {
		return getStr(national_key_discipline_NAME);
	}

	public void setNational_key_discipline(String national_key_discipline) {
		this.national_key_discipline = national_key_discipline;
	}

	public String getCharacteristic_specialty() {
		return getStr(characteristic_specialty_NAME);
	}

	public void setCharacteristic_specialty(String characteristic_specialty) {
		this.characteristic_specialty = characteristic_specialty;
	}

	public String getState_key_laboratories() {
		return getStr(state_key_laboratories_NAME);
	}

	public void setState_key_laboratories(String state_key_laboratories) {
		this.state_key_laboratories = state_key_laboratories;
	}

	public String getFirst_class_subject() {
		return getStr(first_class_subject_NAME);
	}

	public void setFirst_class_subject(String first_class_subject) {
		this.first_class_subject = first_class_subject;
	}

	public String getCampus_scenery() {
		return getStr(campus_scenery_NAME);
	}

	public void setCampus_scenery(String campus_scenery) {
		this.campus_scenery = campus_scenery;
	}

	public String getWen_2017() {
		return getStr(wen_2017_NAME);
	}

	public void setWen_2017(String wen_2017) {
		this.wen_2017 = wen_2017;
	}

	public String getWen_2016() {
		return getStr(wen_2016_NAME);
	}

	public void setWen_2016(String wen_2016) {
		this.wen_2016 = wen_2016;
	}

	public String getWen_2015() {
		return getStr(wen_2015_NAME);
	}

	public void setWen_2015(String wen_2015) {
		this.wen_2015 = wen_2015;
	}

	public String getWen_2014() {
		return getStr(wen_2014_NAME);
	}

	public void setWen_2014(String wen_2014) {
		this.wen_2014 = wen_2014;
	}

	public String getWen_2013() {
		return getStr(wen_2013_NAME);
	}

	public void setWen_2013(String wen_2013) {
		this.wen_2013 = wen_2013;
	}

	public String getLi_2017() {
		return getStr(li_2017_NAME);
	}

	public void setLi_2017(String li_2017) {
		this.li_2017 = li_2017;
	}

	public String getLi_2016() {
		return getStr(li_2016_NAME);
	}

	public void setLi_2016(String li_2016) {
		this.li_2016 = li_2016;
	}

	public String getLi_2015() {
		return getStr(li_2015_NAME);
	}

	public void setLi_2015(String li_2015) {
		this.li_2015 = li_2015;
	}

	public String getLi_2014() {
		return getStr(li_2014_NAME);
	}

	public void setLi_2014(String li_2014) {
		this.li_2014 = li_2014;
	}

	public String getLi_2013() {
		return getStr(li_2013_NAME);
	}

	public void setLi_2013(String li_2013) {
		this.li_2013 = li_2013;
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

	public String getProbability() {

		return String.valueOf(random.nextInt(100));
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

}
