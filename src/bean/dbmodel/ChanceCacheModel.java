package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class ChanceCacheModel extends Model<ChanceCacheModel> {
	public static final ChanceCacheModel dao = new ChanceCacheModel().dao();

	private String id;
	private String cacheKey;
	private String chances;

	private static final String id_NAME = "id";
	private static final String cacheKey_NAME = "cacheKey";
	private static final String chances_NAME = "chances";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getCacheKey() {
		return getStr(cacheKey_NAME);
	}

	public void setCacheKey(String cacheKey) {
		set(cacheKey_NAME, cacheKey);
	}

	public String getChances() {
		return getStr(chances_NAME);
	}

	public void setChances(String chances) {
		set(chances_NAME, chances);
	}

}
