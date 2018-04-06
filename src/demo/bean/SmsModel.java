package demo.bean;

import com.jfinal.plugin.activerecord.Model;

public class SmsModel extends Model<SmsModel> {
	public static final SmsModel dao = new SmsModel().dao();

	private String id;
	private String phone;
	private String code;
	private String sendtime;

	private static final String id_NAME = "id";
	private static final String phone_NAME = "phone";
	private static final String code_NAME = "code";
	private static final String sendtime_NAME = "sendtime";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getPhone() {
		return getStr(phone_NAME);
	}

	public void setPhone(String phone) {
		set(phone_NAME, phone);
	}

	public String getCode() {
		return getStr(code_NAME);
	}

	public void setCode(String code) {
		set(code_NAME, code);
	}

	public String getSendtime() {
		return getStr(sendtime_NAME);
	}

	public void setSendtime(String sendtime) {
		set(sendtime_NAME, sendtime);
	}

}
