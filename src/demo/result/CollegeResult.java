package demo.result;

import java.util.List;

import demo.bean.CollegeModel;
import demo.bean.RankResult;

public class CollegeResult {
	
	public List<RankResult> ranks;
	
	public List<CollegeModel> colleges;

	public List<RankResult> getRanks() {
		return ranks;
	}

	public void setRanks(List<RankResult> ranks) {
		this.ranks = ranks;
	}

	public List<CollegeModel> getColleges() {
		return colleges;
	}

	public void setColleges(List<CollegeModel> colleges) {
		this.colleges = colleges;
	}

}
