package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class QrCodeModel extends Model<QrCodeModel> {
	public static final QrCodeModel dao = new QrCodeModel().dao();

	private String id;
	private String openid;
	private String qrcode;
	private String time;

	private static final String id_NAME = "id";
	private static final String openid_NAME = "openid";
	private static final String qrcode_NAME = "qrcode";
	private static final String time_NAME = "time";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getOpenid() {
		return getStr(openid_NAME);
	}

	public void setOpenid(String openid) {
		set(openid_NAME, openid);
	}

	public String getQrcode() {
		return getStr(qrcode_NAME);
	}

	public void setQrcode(String qrcode) {
		set(qrcode_NAME, qrcode);
	}

	public String getTime() {
		return getStr(time_NAME);
	}

	public void setTime(String time) {
		set(time_NAME, time);
	}

}
