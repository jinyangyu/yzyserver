package controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.thoughtworks.xstream.XStream;

import constant.Configure;
import constant.ResultCode;
import datasource.DataSource;
import demo.bean.CheckOrderInfo;
import demo.bean.CollegeModel;
import demo.bean.UserInfoModel;
import demo.bean.WxOrderModel;
import demo.bean.WxPayResultInfo;
import demo.result.CollegeResult;
import demo.result.RankResult;
import demo.result.Result;
import demo.util.HttpRequest;
import demo.util.RandomStringGenerator;
import demo.util.Signature;

public class CollegeRecommendController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	public void recommend() {

		logger1.info("CollegeRecommendController recommend");

		String scoreStr = getPara("score");
		String subject = getPara("subject");
		String clientSession = getPara("clientSession");
		String out_trade_no = getPara("out_trade_no");

		logger1.info("scoreStr:" + scoreStr + " subject:" + subject + " clientSession:" + clientSession
				+ " out_trade_no:" + out_trade_no);

		int score = 0;
		try {
			score = Integer.parseInt(scoreStr);
		} catch (Exception e) {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "params score invalid", null));
			return;
		}

		boolean isWen = false;
		if ("wenke".equals(subject)) {
			isWen = true;
		} else if ("like".equals(subject)) {
			isWen = false;
		} else {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "params subject invalid", null));
			return;
		}

		if (out_trade_no == null || "".equals(out_trade_no)) {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "params out_trade_no invalid", null));
			return;
		}

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		List<WxOrderModel> orders = WxOrderModel.dao.find("select * from orders where out_trade_no = ?", out_trade_no);
		if (orders == null || orders.isEmpty()) {
			renderJson(new Result(ResultCode.PREPAY_ERROR, "没有查到订单信息", null));
			return;
		}

		if (orders.size() != 1) {
			renderJson(new Result(ResultCode.PREPAY_ERROR, "有多条订单信息", null));
			return;
		}

		WxOrderModel order = orders.get(0);

		if ("SUCCESS".equals(order.getTrade_state())) {
			// trade_state 已为 SUCCESS，说明已经手动检查过，本次请求从我的订单发起。
		} else {

			// 手动检查支付结果。
			try {
				if (checkWxOrder(order)) {
				} else {
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 已成功支付,否则 checkWxOrder 中会返回 false 
		UserInfoModel user = users.get(0);
		logger1.info("user:" + user.getNickName());

		List<CollegeModel> colleges = DataSource.getInstance().recommendCollege(score, true);

		CollegeResult collegeResult = new CollegeResult();
		collegeResult.setColleges(colleges);
		List<RankResult> ranks = new ArrayList<RankResult>();
		ranks.add(new RankResult("2017", "102017"));
		ranks.add(new RankResult("2016", "202016"));
		ranks.add(new RankResult("2015", "302015"));
		collegeResult.setRanks(ranks);

		renderJson(new Result(ResultCode.SUCCESS, "query success", collegeResult));
	}

	private boolean checkWxOrder(WxOrderModel order) throws IllegalAccessException, UnrecoverableKeyException,
			KeyManagementException, ClientProtocolException, KeyStoreException, NoSuchAlgorithmException, IOException {

		boolean checkResult = false;

		CheckOrderInfo orderInfo = new CheckOrderInfo();
		orderInfo.setAppid(Configure.getAppID());
		orderInfo.setMch_id(Configure.getMch_id());
		if (order.getTransaction_id() != null && !"".equals(order.getTransaction_id())) {
			orderInfo.setTransaction_id(order.getTransaction_id());
		} else if (order.getOut_trade_no() != null && !"".equals(order.getOut_trade_no())) {
			orderInfo.setOut_trade_no(order.getOut_trade_no());
		}

		orderInfo.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
		orderInfo.setSign_type("MD5");
		// 生成签名
		String sign = Signature.getSign(orderInfo);
		orderInfo.setSign(sign);

		String checkResultStr = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/orderquery", orderInfo);
		logger1.info("---------检查订单返回:" + checkResultStr);
		XStream xStream = new XStream();
		xStream.alias("xml", WxPayResultInfo.class);
		WxPayResultInfo checkPayResult = (WxPayResultInfo) xStream.fromXML(checkResultStr);

		String resultSign;
		try {
			resultSign = Signature.checkSign(checkPayResult);
			logger1.info("-----result calc sign:" + resultSign);
			logger1.info("-----result sign:" + resultSign);
			if (!checkPayResult.getSign().equals(resultSign)) {
				logger1.error("-------支付结果校验失败");
				renderJson(new Result(ResultCode.SIGN_ERROR, "支付结果校验失败,怀疑被劫持", null));
			}
		} catch (IllegalAccessException e) {
		}

		if (order.getTotal_fee() != checkPayResult.getTotal_fee()) {
			logger1.error(
					"微信返回订单与数据库下单 支付金额不一致 数据库金额：" + order.getTotal_fee() + " 微信支付金额：" + checkPayResult.getTotal_fee());
			renderJson(new Result(ResultCode.SIGN_ERROR, "支付结果支付金额不一致,怀疑被劫持", null));
		}
		if (!order.getMch_id().equals(checkPayResult.getMch_id())) {
			logger1.error("微信返回订单商户id不一致 订单商户id：" + order.getMch_id() + " 微信支付商户id：" + checkPayResult.getMch_id());
			renderJson(new Result(ResultCode.SIGN_ERROR, "支付结果订单商户id不一致,怀疑被劫持", null));
		}
		if (!order.getOpenid().equals(checkPayResult.getOpenid())) {
			logger1.error("微信返回订单openid不一致 订单商户id：" + order.getOpenid() + " 微信支付openid：" + checkPayResult.getOpenid());
			renderJson(new Result(ResultCode.SIGN_ERROR, "支付结果订单openid不一致,怀疑被劫持", null));
		}

		if (!"SUCCESS".equals(checkPayResult.getResult_code())) {
			logger1.error("------- 支付结果为支付失败");
			renderJson(new Result(ResultCode.PAY_ERROR, "支付结果为支付失败", null));
		}

		if (!"SUCCESS".equals(checkPayResult.getTrade_state())) {
			logger1.error("------- 支付结果不成功,交易状态描述：" + checkPayResult.getTrade_state_desc());
			order.setStatusPayFailed();
			order.update();
			renderJson(
					new Result(ResultCode.PAY_ERROR, "支付结果不成功,交易状态描述:" + checkPayResult.getTrade_state_desc(), null));
		}

		if ("SUCCESS".equals(checkPayResult.getTrade_state())) {
			order.setBank_type(checkPayResult.getBank_type());
			order.setFee_type(checkPayResult.getFee_type());
			order.setIs_subscribe(checkPayResult.getIs_subscribe());
			order.setTime_end(checkPayResult.getTime_end());
			order.setTransaction_id(checkPayResult.getTransaction_id());
			order.setTrade_state(checkPayResult.getTrade_state());
			order.setTrade_state_desc(checkPayResult.getTrade_state_desc());

			logger1.info("------- 检查支付结果为支付成功,补充 trade_state：" + order.getTrade_state() + " trade_state_desc:"
					+ checkPayResult.getTrade_state_desc());

			order.setStatusPaySuccess();
			order.update();

			checkResult = true;
		}
		return checkResult;
	}

}