package controller;

import java.util.List;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.PrepayCountModel;
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

		UserInfoModel currentUser = users.get(0);
		logger1.info("currentUser:" + currentUser.getNickName() + " openId:" + currentUser.getOpenid());

		if ("".equals(currentUser.getOpenid())) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		String openid = currentUser.getOpenid();
		List<YzyOrderModel> yzyOrders = YzyOrderModel.dao.find("select * from orders_yzy where openid = ? order by time desc", openid);

		logger1.info("返回订单信息");

		MyOrdersResult result = new MyOrdersResult(yzyOrders);
		for (int i = 0; i < result.getRecommend_orders().size(); i++) {
			logger1.info("RecommendOrders ==== " + result.getRecommend_orders().get(i));
		}

		for (int i = 0; i < result.getExpert_orders().size(); i++) {
			logger1.info("ExpertOrders ==== " + result.getExpert_orders().get(i));
		}

		List<PrepayCountModel> prepayCounts = PrepayCountModel.dao.find("select * from prepay_count where openid = ?",
				currentUser.getOpenid());

		if (prepayCounts == null || prepayCounts.size() < 1) {
		} else {
			PrepayCountModel prepayModel = prepayCounts.get(0);
			result.setTotal_count(prepayModel.getTotalCount());
			result.setCurrent_count(prepayModel.getCurrentCount());
		}

		renderJson(new Result(ResultCode.SUCCESS, "success", result));
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
