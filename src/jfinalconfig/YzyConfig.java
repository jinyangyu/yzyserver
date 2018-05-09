package jfinalconfig;

import com.jfinal.config.*;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log4jLogFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import bean.dbmodel.ChanceCacheModel;
import bean.dbmodel.CollegeModel;
import bean.dbmodel.ScoreRankModel;
import bean.dbmodel.SmsModel;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.WxPayOrderModel;
import bean.dbmodel.YzyOrderModel;
import controller.CollegePDFController;
import controller.CollegeRecommendController;
import controller.SmsVerifyController;
import controller.ExpertPaySuccessController;
import controller.MyOrdersController;
import controller.PaymentController;
import controller.SmsController;
import controller.UserInfoController;
import controller.WXPayResultController;
import datasource.DataSource;
import demo.HelloController;

public class YzyConfig extends JFinalConfig {
	public void configConstant(Constants me) {
		PropKit.use("project_config.txt");

		me.setDevMode(PropKit.getBoolean("devMode", false));
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
		me.add("/recommend", CollegeRecommendController.class);
		System.out.println("configRoute collegeRecommend");
		me.add("/sms", SmsController.class);
		System.out.println("configRoute SmsController");
		me.add("/smsverify", SmsVerifyController.class);
		System.out.println("configRoute SmsController");
		me.add("/pay", PaymentController.class);
		System.out.println("configRoute PaymentController");
		me.add("/wxpay", WXPayResultController.class);
		System.out.println("configRoute WXPayResultController");
		me.add("/myorders", MyOrdersController.class);
		System.out.println("configRoute MyOrdersController");
		me.add("/expertpaysuccess", ExpertPaySuccessController.class);
		System.out.println("configRoute ExpertPaySuccessController");
	}

	public void configEngine(Engine me) {
		System.out.println("configEngine");
	}

	public void configPlugin(Plugins me) {
		System.out.println("configPlugin");

		DruidPlugin dp = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		
		me.add(dp);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		me.add(arp);

		arp.addMapping("college", CollegeModel.class);
		arp.addMapping("userinfo", UserInfoModel.class);
		arp.addMapping("sms_verify", SmsModel.class);
		arp.addMapping("orders_wxpay", WxPayOrderModel.class);
		arp.addMapping("orders_yzy", YzyOrderModel.class);
		arp.addMapping("scores", ScoreRankModel.class);
		arp.addMapping("chance_cache", ChanceCacheModel.class);
		arp.setShowSql(true);
	}

	public void configInterceptor(Interceptors me) {
		System.out.println("configInterceptor");
	}

	public void configHandler(Handlers me) {
		System.out.println("configHandler");
	}

	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
		System.out.println("afterJFinalStart");
		DataSource.getInstance();
	}
}