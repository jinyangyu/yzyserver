package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class LiankaoScoreModel extends Model<LiankaoScoreModel> {
	public static final LiankaoScoreModel dao = new LiankaoScoreModel().dao();

	private String id;
	private int score;
	private int rank;
	private int count;
	private String school;
	private String wenli;
	private int liankao_id;

	private static final String id_NAME = "id";
	private static final String score_NAME = "score";
	private static final String rank_NAME = "rank";
	private static final String count_NAME = "count";
	private static final String school_NAME = "school";
	private static final String wenli_NAME = "wenli";
	private static final String liankao_id_NAME = "liankao_id";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public int getRank() {
		return getInt(rank_NAME);
	}

	public void setRank(int rank) {
		set(rank_NAME, rank);
	}
	
	public int getCount() {
		return getInt(count_NAME);
	}

	public void setCount(int count) {
		set(count_NAME, count);
	}

	public int getScore() {
		return getInt(score_NAME);
	}

	public void setScore(int score) {
		set(score_NAME, score);
	}

	public String getWenli() {
		return getStr(wenli_NAME);
	}

	public void setWenli(String wenli) {
		set(wenli_NAME, wenli);
	}

	public String getSchool() {
		return getStr(school_NAME);
	}

	public void setSchool(String school) {
		set(school_NAME, school);
	}

	public int getLiankao_id() {
		return getInt(liankao_id_NAME);
	}

	public void setLiankao_id(int liankao_id) {
		set(liankao_id_NAME, liankao_id);
	}

}
