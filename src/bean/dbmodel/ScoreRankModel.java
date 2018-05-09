package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class ScoreRankModel extends Model<ScoreRankModel> {
	public static final ScoreRankModel dao = new ScoreRankModel().dao();

	private String id;
	private int rank;
	private int score;
	private int count;
	private String year;
	private String wenli;

	private static final String id_NAME = "id";
	private static final String rank_NAME = "rank";
	private static final String score_NAME = "score";
	private static final String count_NAME = "count";
	private static final String year_NAME = "year";
	private static final String wenli_NAME = "wenli";

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

	public int getScore() {
		return getInt(score_NAME);
	}

	public void setScore(int score) {
		set(score_NAME, score);
	}

	public int getCount() {
		return getInt(count_NAME);
	}

	public void setCount(int count) {
		set(count_NAME, count);
	}

	public String getYear() {
		return getStr(year_NAME);
	}

	public void setYear(String year) {
		set(year_NAME, year);
	}

	public String getWenli() {
		return getStr(wenli_NAME);
	}

	public void setWenli(String wenli) {
		set(wenli_NAME, wenli);
	}

}
