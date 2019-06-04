package datasource;

import java.util.List;

import bean.dbmodel.CollegeModelAll;

public interface IRecommendSource {
	
	void setCollege(List<CollegeModelAll> colleges);
	List<CollegeModelAll> recommendCollege(int score);
	String clearCache();

}
