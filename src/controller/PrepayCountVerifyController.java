package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.PrepayCountModel;
import bean.dbmodel.UserInfoModel;
import bean.requestresult.MoneyInfo;
import bean.requestresult.PrepayCheckResult;
import bean.requestresult.Result;
import constant.ResultCode;
import datasource.DataSource;
import datasource.YouHuiDataSource;

public class PrepayCountVerifyController extends Controller {
	public static Logger logger1 = Logger.getLogger(PrepayCountVerifyController.class);

	public void check() {

		logger1.info("PrepayCountVerifyController check");

		String clientSession = getPara("clientSession");
		String test = getPara("test");

		logger1.info("clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			logger1.info("登陆信息失效,clientSession没查到");
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}
		UserInfoModel currentUser = users.get(0);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

		MoneyInfo moneyInfo = YouHuiDataSource.getSharedMoneyInfo(currentUser.getOpenid());
		System.out.println(
				"moneyInfo:" + moneyInfo.getOrigin() + " " + moneyInfo.getCoupon() + " " + moneyInfo.getFinalfee());

		boolean isAdmin = DataSource.getInstance().isAdmin(currentUser.getOpenid());

		try {
			Date firstDay = sdf.parse("20190624 11:00:00");
			if (!isAdmin && (System.currentTimeMillis() - firstDay.getTime() < 0)) {
				MoneyInfo moneyInfo1 = new MoneyInfo();
				moneyInfo1.setOrigin(PaymentController.MONEY_PREPAY);
				renderJson(new Result(ResultCode.PREPAY_CANNOT_PAY, "高考分数尚未公布，请耐心等待", moneyInfo1));
				return;
			}

			Date secondDay = sdf.parse("20190625 03:00:00");
			if (!isAdmin && System.currentTimeMillis() - secondDay.getTime() < 0) {
				renderJson(new Result(ResultCode.PREPAY_CAN_PREPAY,
						"恭喜您支付成功并获得一次志愿推荐次数，您可在2019年6月25日03:00后，直接查询推荐结果，无需再次支付！", moneyInfo));
				return;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<PrepayCountModel> prepayCounts = PrepayCountModel.dao.find("select * from prepay_count where openid = ?",
				currentUser.getOpenid());

		if (prepayCounts == null || prepayCounts.size() < 1) {
			renderJson(new Result(ResultCode.PREPAY_COUNT_NULL, "无预购,正常支付", moneyInfo));
			return;
		}

		PrepayCountModel prepayModel = null;
		for (int i = 0; i < prepayCounts.size(); i++) {
			prepayModel = prepayCounts.get(i);

			if (prepayModel.getCurrentCount() > 0) {
				renderJson(
						new Result(ResultCode.SUCCESS, "使用预购次数", new PrepayCheckResult(prepayModel.getOut_trade_no())));
				return;
			}
		}
		renderJson(new Result(ResultCode.PREPAY_COUNT_0, "预购次数已用完", moneyInfo));

	}
}
