package controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.aliyuncs.exceptions.ClientException;
import com.jfinal.core.Controller;
import com.thoughtworks.xstream.XStream;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.WxPayOrderModel;
import bean.dbmodel.YzyOrderModel;
import bean.requestinfo.CheckPayOrderInfo;
import bean.requestinfo.WxPayResultInfo;
import bean.requestresult.Result;
import constant.Configure;
import constant.ResultCode;
import util.HttpRequest;
import util.RandomStringGenerator;
import util.Signature;
import util.sms.SmsUtil;

public class ExpertPaySuccessController extends Controller {
	public static Logger logger1 = Logger.getLogger(SmsVerifyController.class);

	private static final String PHONE_DUWEI = "18810012406";
	private static final String PHONE_SHANGHUA = "18595388999";
	private static final String PHONE_MINGXI = "18639167036";
	private static final String PHONE_JINYANG = "18210512166";
	private static final String PHONE_WENJIAN = "15900209307";

	public void order() {

		logger1.info("CollegeRecommendController recommend");

		String clientSession = getPara("clientSession");
		String phone = getPara("phone");

		logger1.info("clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			logger1.info("登陆信息失效,clientSession没查到");
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}
		UserInfoModel user = users.get(0);

		String out_trade_no = getPara("out_trade_no");

		if (out_trade_no == null || "".equals(out_trade_no)) {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "params out_trade_no invalid", null));
			return;
		}

		List<WxPayOrderModel> wxOrders = WxPayOrderModel.dao.find("select * from orders_wxpay where out_trade_no = ?",
				out_trade_no);
		if (wxOrders == null || wxOrders.isEmpty()) {
			renderJson(new Result(ResultCode.PREPAY_ERROR, "没有查到订单信息", null));
			return;
		}

		if (wxOrders.size() != 1) {
			renderJson(new Result(ResultCode.PREPAY_ERROR, "有多条订单信息", null));
			return;
		}

		WxPayOrderModel wxOrder = wxOrders.get(0);

		if ("SUCCESS".equals(wxOrder.getTrade_state())) {
		} else {
			// 手动检查支付结果。
			try {
				if (checkWxOrder(wxOrder)) {
				} else {
					logger1.error("支付确认失败，但是客户端成功，需要人工确认 out_trade_no:" + out_trade_no);
					renderJson(new Result(ResultCode.PREPAY_ERROR, "支付确认失败，但是客户端成功，需要人工确认", null));
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		YzyOrderModel yzyOrder = new YzyOrderModel();
		yzyOrder.setOut_trade_no(out_trade_no);
		yzyOrder.setOpenid(user.getOpenid());
		yzyOrder.setTime(String.valueOf(System.currentTimeMillis()));
		yzyOrder.setExpertType();
		yzyOrder.setPhone(phone);
		yzyOrder.save();

		try {
//			SmsUtil.sendSms("SMS_134080035", PHONE_DUWEI,
//					"{\"consignee\":\"" + user.getNickName() + "\", \"number\": \"" + phone + "\"}");
//			logger1.info("!!!!!!!!!!!!!!!=====" + user.getNickName() + " " + phone + " 专家辅导支付成功！！！！！！！！ 发短信提醒杜威！！");
//			SmsUtil.sendSms("SMS_134080035", PHONE_SHANGHUA,
//					"{\"consignee\":\"" + user.getNickName() + "\", \"number\": \"" + phone + "\"}");
//			logger1.info("!!!!!!!!!!!!!!!=====" + user.getNickName() + " " + phone + " 专家辅导支付成功！！！！！！！！ 发短信提醒尚华！！");
			SmsUtil.sendSms("SMS_134080035", PHONE_MINGXI,
					"{\"consignee\":\"" + user.getNickName() + "\", \"number\": \"" + phone + "\"}");
			logger1.info("!!!!!!!!!!!!!!!=====" + user.getNickName() + " " + phone + " 专家辅导支付成功！！！！！！！！ 发短信提醒明西！！");
			SmsUtil.sendSms("SMS_134080035", PHONE_JINYANG,
					"{\"consignee\":\"" + user.getNickName() + "\", \"number\": \"" + phone + "\"}");
			logger1.info("!!!!!!!!!!!!!!!=====" + user.getNickName() + " " + phone + " 专家辅导支付成功！！！！！！！！ 发短信提醒金洋！！");
			SmsUtil.sendSms("SMS_134080035", PHONE_WENJIAN,
					"{\"consignee\":\"" + user.getNickName() + "\", \"number\": \"" + phone + "\"}");
			logger1.info("!!!!!!!!!!!!!!!=====" + user.getNickName() + " " + phone + " 专家辅导支付成功！！！！！！！！ 发短信提醒文建！！");
		} catch (ClientException e) {
			e.printStackTrace();
			logger1.error("发送支付成功通知失败,阿里云服务器错误");
		}
		logger1.info("阿里云支付成功通知短信发送成功");

		renderJson(new Result(ResultCode.SUCCESS, "专家辅导支付成功", null));
	}

	private boolean checkWxOrder(WxPayOrderModel order) throws IllegalAccessException, UnrecoverableKeyException,
			KeyManagementException, ClientProtocolException, KeyStoreException, NoSuchAlgorithmException, IOException {

		boolean checkResult = false;

		CheckPayOrderInfo orderInfo = new CheckPayOrderInfo();
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
