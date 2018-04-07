package demo;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.CollegeModel;
import bean.requestresult.Result;
import controller.CollegeRecommendController;
import datasource.DataSource;

public class HelloController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);
	
	public void index() {

		System.out.println("hello request");

		System.out.println("select * from college");
		List<CollegeModel> colleges = DataSource.getInstance().recommendCollege(100,true);

		renderJson(new Result(200, "query success", colleges));
	}
}