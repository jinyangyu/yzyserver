package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class SchoolScoreModel extends Model<SchoolScoreModel> {
	public static final SchoolScoreModel dao = new SchoolScoreModel().dao();

	private String id;
	private int rank;
	private int school_rank;
	private int score;
	private String wenli;
	private String school;

	private static final String id_NAME = "id";
	private static final String rank_NAME = "rank";
	private static final String school_rank_NAME = "school_rank";
	private static final String score_NAME = "score";
	private static final String wenli_NAME = "wenli";
	private static final String school_NAME = "school";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public int getRank() {
		if (getStr(rank_NAME) == null) {
			return 0;
		}
		return getInt(rank_NAME);
	}

	public void setRank(int rank) {
		set(rank_NAME, rank);
	}

	public int getSchool_rank() {
		return getInt(school_rank_NAME);
	}

	public void setSchool_rank(int school_rank) {
		set(school_rank_NAME, school_rank);
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

}
