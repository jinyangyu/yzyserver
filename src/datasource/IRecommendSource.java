package datasource;

import java.util.List;

import demo.bean.CollegeModel;

public interface IRecommendSource {
	
	void setCollege(List<CollegeModel> colleges);
	List<CollegeModel> recommendCollege(int score);

}
