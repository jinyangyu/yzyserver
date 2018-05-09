package bean.requestresult;

import bean.dbmodel.CollegeModel;

public class CollegeChanceResult {
	private CollegeModel college;
	private int chance;

	public CollegeModel getCollege() {
		return college;
	}

	public void setCollege(CollegeModel college) {
		this.college = college;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

}
