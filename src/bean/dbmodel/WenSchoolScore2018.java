package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class WenSchoolScore2018 extends Model<WenSchoolScore2018> {
	public static final WenSchoolScore2018 dao = new WenSchoolScore2018().dao();

	private String id;
	private int score;
	private int code;
	private String name;

	private static final String id_NAME = "id";
	private static final String score_NAME = "score";
	private static final String code_NAME = "code";
	private static final String name_NAME = "name";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public int getScore() {
		return getInt(score_NAME);
	}

	public void setScore(int score) {
		set(score_NAME, score);
	}

	public int getCode() {
		return getInt(code_NAME);
	}

	public void setCode(int code) {
		set(code_NAME, code);
	}

	public String getName() {
		return getStr(name_NAME);
	}

	public void setName(String name) {
		set(name_NAME, name);
	}

}
