package constant;

public class Configure {
	private static String key = "DaoHeng2018YuZhiYuan2018DaoHeng2";

	//小程序ID	
	private static String appID = "wxda057bdf3873a58e";
	//商户号
	private static String mch_id = "1501524091";
	
	private static String secret = "22cfc9a499c8bcd60162994a5cf146dc";

	public static String getSecret() {
		return secret;
	}

	public static void setSecret(String secret) {
		Configure.secret = secret;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static String getAppID() {
		return appID;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static String getMch_id() {
		return mch_id;
	}

	public static void setMch_id(String mch_id) {
		Configure.mch_id = mch_id;
	}

}
