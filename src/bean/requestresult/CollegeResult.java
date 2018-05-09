package bean.requestresult;

import java.util.List;

public class CollegeResult {
	public List<RankResult> ranks;
	public List<CollegeChanceResult> colleges;

	public int totalPage;
	public int totalSize;

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public List<RankResult> getRanks() {
		return ranks;
	}

	public void setRanks(List<RankResult> ranks) {
		this.ranks = ranks;
	}

	public List<CollegeChanceResult> getColleges() {
		return colleges;
	}

	public void setColleges(List<CollegeChanceResult> colleges) {
		this.colleges = colleges;
	}

}
