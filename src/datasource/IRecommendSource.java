package datasource;

import java.util.List;

import bean.dbmodel.CollegeModel;

public interface IRecommendSource {
	
	void setCollege(List<CollegeModel> colleges);
	List<CollegeModel> recommendCollege(int score);
	void clearCache();

}
