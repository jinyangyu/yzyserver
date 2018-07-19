package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class InfoUserCheck extends Model<InfoUserCheck> {
	public static final InfoUserCheck dao = new InfoUserCheck().dao();

	private String id;
	private String openid;

	private static final String id_NAME = "id";
	private static final String openid_NAME = "openid";

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

}
