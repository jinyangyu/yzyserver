package demo;

import java.util.List;

import com.jfinal.core.Controller;
import demo.bean.CollegeModel;
import demo.result.Result;

public class HelloController extends Controller {
	public void index() {

		System.out.println("hello request");

		System.out.println("select * from college");
		List<CollegeModel> colleges = CollegeModel.dao.find("select * from college where id < 101 and id > 99");

		for (CollegeModel model : colleges) {
			String[] names = model._getAttrNames();
			for (String name : names) {
				System.out.println(name + " " + model.getStr(name));
			}
		}

		System.out.println("select * from college");
		renderJson(new Result(200, "query success", colleges));
	}
}