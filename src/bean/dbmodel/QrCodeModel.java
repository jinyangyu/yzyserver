package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class QrCodeModel extends Model<QrCodeModel> {
	public static final QrCodeModel dao = new QrCodeModel().dao();

	private String id;
	private String openid;
	private String qrcode;
	private String sharefrom;
	private String time;
	private int used;

	private static final String id_NAME = "id";
	private static final String openid_NAME = "openid";
	private static final String qrcode_NAME = "qrcode";
	private static final String sharefrom_NAME = "sharefrom";
	private static final String time_NAME = "time";
	private static final String used_NAME = "used";

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

	public String getSharefrom() {
		return getStr(sharefrom_NAME);
	}

	public void setSharefrom(String sharefrom) {
		set(sharefrom_NAME, sharefrom);
	}

	public int getUsed() {
		return getInt(used_NAME);
	}

	public void setUsed(int used) {
		set(used_NAME, used);
	}

}
