package controller;

import java.util.List;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.SmsModel;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.YzyOrderModel;
import bean.requestresult.MyOrdersResult;
import bean.requestresult.Result;
import constant.ResultCode;

public class MyOrdersController extends Controller {
	public static Logger logger1 = Logger.getLogger(MyOrdersController.class);

	public void orders() {

		logger1.info("CollegeRecommendController recommend");

		String clientSession = getPara("clientSession");

		logger1.info("clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			logger1.info("登陆信息失效,clientSession没查到");
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		UserInfoModel user = users.get(0);
		logger1.info("user:" + user.getNickName() + " openId:" + user.getOpenid());

		if ("".equals(user.getOpenid())) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		String openid = user.getOpenid();
		List<YzyOrderModel> yzyOrders = YzyOrderModel.dao.find("select * from orders_yzy where openid = ?", openid);

		logger1.info("返回订单信息");

		MyOrdersResult result = new MyOrdersResult(yzyOrders);
		for (int i = 0; i < result.getOrders().size(); i++) {
			logger1.info("==== " + result.getOrders().get(i));
		}
		renderJson(new Result(ResultCode.SUCCESS, "success", result));
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
