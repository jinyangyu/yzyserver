package demo.bean;

public class RankResult {
	public String year;
	public String rank;

	public RankResult(String year, String rank) {
		super();
		this.year = year;
		this.rank = rank;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

}
