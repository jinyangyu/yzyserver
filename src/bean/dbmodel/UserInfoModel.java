package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class UserInfoModel extends Model<UserInfoModel> {
	public static final UserInfoModel dao = new UserInfoModel().dao();

	private String id;
	private String nickName;
	private String avatarUrl;
	private String gender;
	private String city;
	private String province;
	private String country;
	private String language;
	private String openid;
	private String sessionKey;
	private String clientSession;
	private String pdfPath;
	private String qrcode;

	private static final String id_NAME = "id";
	private static final String nickName_NAME = "nickName";
	private static final String avatarUrl_NAME = "avatarUrl";
	private static final String gender_NAME = "gender";
	private static final String city_NAME = "city";
	private static final String province_NAME = "province";
	private static final String country_NAME = "country";
	private static final String language_NAME = "language";
	private static final String openid_NAME = "openid";
	private static final String sessionKey_NAME = "sessionKey";
	private static final String clientSession_NAME = "clientSession";
	private static final String pdfPath_NAME = "pdfPath";
	private static final String qrcode_NAME = "qrcode";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickName() {
		return getStr(nickName_NAME);
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
		set(nickName_NAME, nickName);
	}

	public String getAvatarUrl() {
		return getStr(avatarUrl_NAME);
	}

	public void setAvatarUrl(String avatarUrl) {
		set(avatarUrl_NAME, avatarUrl);
	}

	public String getGender() {
		return getStr(gender_NAME);
	}

	public void setGender(String gender) {
		set(gender_NAME, gender);
	}

	public String getCity() {
		return getStr(city_NAME);
	}

	public void setCity(String city) {
		set(city_NAME, city);
	}

	public String getProvince() {
		return getStr(province_NAME);
	}

	public void setProvince(String province) {
		set(province_NAME, province);
	}

	public String getCountry() {
		return getStr(country_NAME);
	}

	public void setCountry(String country) {
		set(country_NAME, country);
	}

	public String getLanguage() {
		return getStr(language_NAME);
	}

	public void setLanguage(String language) {
		set(language_NAME, language);
	}

	public String getOpenid() {
		return getStr(openid_NAME);
	}

	public void setOpenid(String openid) {
		set(openid_NAME, openid);
	}

	public String getSessionKey() {
		return getStr(sessionKey_NAME);
	}

	public void setSessionKey(String sessionKey) {
		set(sessionKey_NAME, sessionKey);
	}

	public String getClientSession() {
		return getStr(clientSession_NAME);
	}

	public void setClientSession(String clientSession) {
		set(clientSession_NAME, clientSession);
	}

	public String getPdfPath() {
		return getStr(pdfPath_NAME);
	}

	public void setPdfPath(String pdfPath) {
		set(pdfPath_NAME, pdfPath);
	}
	
	public String getQrcode() {
		return getStr(qrcode_NAME);
	}

	public void setQrcode(String qrcode) {
		set(qrcode_NAME, qrcode);
	}

	@Override
	public String toString() {
		return "UserInfoModel [id=" + id + ", nickName=" + nickName + ", avatarUrl=" + avatarUrl + ", gender=" + gender
				+ ", city=" + city + ", province=" + province + ", country=" + country + ", language=" + language
				+ ", openid=" + openid + ", sessionKey=" + sessionKey + ", clientSession=" + clientSession
				+ ", pdfPath=" + pdfPath + ", qrcode=" + qrcode + "]";
	}

}
