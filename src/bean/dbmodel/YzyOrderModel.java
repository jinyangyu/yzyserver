package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class YzyOrderModel extends Model<YzyOrderModel> {
	public static final YzyOrderModel dao = new YzyOrderModel().dao();

	private String id;
	private String out_trade_no;
	private int score;
	private String subject;
	private int type;
	private String phone;
	private String time;
	private String openid;

	private static final String id_NAME = "id";
	private static final String out_trade_no_NAME = "out_trade_no";
	private static final String score_NAME = "score";
	private static final String subject_NAME = "subject";
	private static final String type_NAME = "type";
	private static final String phone_NAME = "phone";
	private static final String time_NAME = "time";
	private static final String openid_NAME = "openid";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getOut_trade_no() {
		return getStr(out_trade_no_NAME);
	}

	public void setOut_trade_no(String out_trade_no) {
		set(out_trade_no_NAME, out_trade_no);
	}

	public int getScore() {
		return getInt(score_NAME);
	}

	public void setScore(int score) {
		set(score_NAME, score);
	}

	public String getSubject() {
		return getStr(subject_NAME);
	}

	public void setSubject(String subject) {
		set(subject_NAME, subject);
	}

	public int getType() {
		return getInt(type_NAME);
	}

	public void setRecommendType() {
		set(type_NAME, 1);
	}

	public void setExpertType() {
		set(type_NAME, 2);
	}

	public String getPhone() {
		return getStr(phone_NAME);
	}

	public void setPhone(String phone) {
		set(phone_NAME, phone);
	}

	public String getTime() {
		return getStr(time_NAME);
	}

	public void setTime(String time) {
		set(time_NAME, time);
	}

	public String getOpenid() {
		return getStr(openid_NAME);
	}

	public void setOpenid(String openid) {
		set(openid_NAME, openid);
	}

}
