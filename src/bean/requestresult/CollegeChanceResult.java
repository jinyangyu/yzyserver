package bean.requestresult;

import bean.dbmodel.CollegeModelAll;

public class CollegeChanceResult {
	private CollegeModelAll college;
	private int chance;

	public CollegeModelAll getCollege() {
		return college;
	}

	public void setCollege(CollegeModelAll college) {
		this.college = college;
	}

	public int getChance() {
		return chance;
	}

	public void setChance(int chance) {
		this.chance = chance;
	}

}
