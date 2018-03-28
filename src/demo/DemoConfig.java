package demo;

import com.jfinal.config.*;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log4jLogFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import controller.CollegePDFController;
import controller.UserInfoController;
import demo.bean.CollegeModel;
import demo.bean.UserInfoModel;

public class DemoConfig extends JFinalConfig {
	public void configConstant(Constants me) {
		PropKit.use("project_config.txt");
		
		me.setDevMode(PropKit.getBoolean("devMode",false));
		me.setEncoding(PropKit.get("encoding"));
		
		me.setJsonFactory(new FastJsonFactory());
		me.setLogFactory(new Log4jLogFactory());
	}

	public void configRoute(Routes me) {
		System.out.println("configRoute");
		me.add("/hello", HelloController.class);
		System.out.println("configRoute hello");
		me.add("/userinfo", UserInfoController.class);
		System.out.println("configRoute userinfo");
		me.add("/mkpdf", CollegePDFController.class);
		System.out.println("configRoute mkpdf");
	}

	public void configEngine(Engine me) {
		System.out.println("configEngine");
	}

	public void configPlugin(Plugins me) {
		System.out.println("configPlugin");
		
		DruidPlugin dp = new DruidPlugin(
				PropKit.get("jdbcUrl"),
				PropKit.get("user"), 
				PropKit.get("password").trim());
		me.add(dp);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		me.add(arp);
		
		arp.addMapping("college",CollegeModel.class);
		arp.addMapping("userinfo",UserInfoModel.class);
		arp.setShowSql(true);
	}

	public void configInterceptor(Interceptors me) {
		System.out.println("configInterceptor");
	}

	public void configHandler(Handlers me) {
		System.out.println("configHandler");
	}
}