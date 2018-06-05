package controller;

import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.PrepayCountModel;
import bean.dbmodel.UserInfoModel;
import bean.requestresult.PrepayCheckResult;
import bean.requestresult.Result;
import constant.ResultCode;

public class PrepayCountVerifyController extends Controller {
	public static Logger logger1 = Logger.getLogger(PrepayCountVerifyController.class);

	public void check() {

		logger1.info("PrepayCountVerifyController check");

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

		List<PrepayCountModel> prepayCounts = PrepayCountModel.dao.find("select * from prepay_count where openid = ?",
				currentUser.getOpenid());

		if (prepayCounts == null || prepayCounts.size() < 1) {
			renderJson(new Result(ResultCode.PREPAY_COUNT_NULL, "无预购", null));
			return;
		}

		PrepayCountModel prepayModel = null;
		for (int i = 0; i < prepayCounts.size(); i++) {
			prepayModel = prepayCounts.get(i);

			if (prepayModel.getCurrentCount() > 0) {
				renderJson(
						new Result(ResultCode.SUCCESS, "使用预购次数", new PrepayCheckResult(prepayModel.getOut_trade_no())));
				return;
			} else {
				renderJson(new Result(ResultCode.PREPAY_COUNT_0, "预购次数已用完", null));
			}
		}

	}
}
