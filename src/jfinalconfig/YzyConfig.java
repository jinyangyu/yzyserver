package jfinalconfig;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log4jLogFactory;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

import bean.dbmodel.AdminOpenid;
import bean.dbmodel.ChanceCacheModel;
import bean.dbmodel.CollegeModel;
import bean.dbmodel.CollegeModelAll;
import bean.dbmodel.Info;
import bean.dbmodel.InfoUserCheck;
import bean.dbmodel.LiSchoolScore2018;
import bean.dbmodel.LiankaoScoreModel;
import bean.dbmodel.PrepayCountModel;
import bean.dbmodel.QrCodeModel;
import bean.dbmodel.SchoolScoreModel;
import bean.dbmodel.ScoreRankModel;
import bean.dbmodel.SmsModel;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.WenSchoolScore2018;
import bean.dbmodel.WxPayOrderModel;
import bean.dbmodel.YzyOrderModel;
import controller.CollegePDFController;
import controller.CollegeRecommendController;
import controller.ExpertPaySuccessController;
import controller.InfoCheckController;
import controller.MyOrdersController;
import controller.PaymentController;
import controller.PrepayCountVerifyController;
import controller.QRCodeBindController;
import controller.SmsController;
import controller.SmsVerifyController;
import controller.UserInfoController;
import controller.WXPayResultController;
import datasource.DataSource;
import datasource.LiankaoDataSource;
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
		me.add("/qrcode", QRCodeBindController.class);
		System.out.println("configRoute QRCodeBindController");
		me.add("/prepaycount", PrepayCountVerifyController.class);
		System.out.println("configRoute PrepayCountVerifyController");
		me.add("/info", InfoCheckController.class);
		System.out.println("configRoute InfoCheckController");
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
		arp.addMapping("qrcode", QrCodeModel.class);
		arp.addMapping("prepay_count", PrepayCountModel.class);
		arp.addMapping("admin_openid", AdminOpenid.class);
		arp.addMapping("info_user_check", InfoUserCheck.class);
		arp.addMapping("info", Info.class);
		arp.addMapping("top_school_scores", SchoolScoreModel.class);
		arp.addMapping("liankao_score_rank", LiankaoScoreModel.class);
		arp.addMapping("li_2018", LiSchoolScore2018.class);
		arp.addMapping("wen_2018", WenSchoolScore2018.class);
		arp.addMapping("college_all", CollegeModelAll.class);

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
		LiankaoDataSource.getInstance().init();
	}
}