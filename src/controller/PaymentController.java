package controller;

import java.util.List;

import org.apache.log4j.Logger;
import com.jfinal.core.Controller;
import com.thoughtworks.xstream.XStream;
import constant.Configure;
import constant.ResultCode;
import demo.bean.OrderInfo;
import demo.bean.OrderReturnInfo;
import demo.bean.UserInfoModel;
import demo.bean.WxOrderModel;
import demo.result.PrepayResult;
import demo.result.Result;
import demo.util.HttpRequest;
import demo.util.MD5;
import demo.util.RandomStringGenerator;
import demo.util.Signature;

public class PaymentController extends Controller {
	public static Logger logger1 = Logger.getLogger(PaymentController.class);

	private static final int PAY_FOR_RECOMMEND = 1; // 院校推荐订单
	private static final int PAY_FOR_EXPORT = 2; // 专家预约订单
	private UserInfoModel currentUser;

	private WxOrderModel wxOrderModel;

	public void recommend() {

		String clientSession = getPara("clientSession");
		logger1.info("clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}
		currentUser = users.get(0);

		int payType = 0;
		try {
			payType = Integer.parseInt(getPara("type"));
		} catch (Exception e) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "支付类型参数错误", null));
			return;
		}

		if (payType == PAY_FOR_RECOMMEND) {
			String scoreStr = getPara("score");
			int score = 0;
			try {
				score = Integer.parseInt(scoreStr);
			} catch (Exception e) {
				renderJson(new Result(ResultCode.LOGIN_ERROR, "查询分数参数错误", null));
				return;
			}
		} else if (payType == PAY_FOR_EXPORT) {

		} else {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "支付类型参数错误", null));
			return;
		}

		/*
		 * <xml> <return_code><![CDATA[SUCCESS]]></return_code>
		 * <return_msg><![CDATA[OK]]></return_msg>
		 * <appid><![CDATA[wxda057bdf3873a58e]]></appid>
		 * <mch_id><![CDATA[1501524091]]></mch_id>
		 * <nonce_str><![CDATA[X2brvOD8IBDV3Uca]]></nonce_str>
		 * <sign><![CDATA[B4FF79C59942DDF00B23F88118B5DB14]]></sign>
		 * <result_code><![CDATA[SUCCESS]]></result_code>
		 * <prepay_id><![CDATA[wx062046307543307844008c4f0594606674]]></prepay_id>
		 * <trade_type><![CDATA[JSAPI]]></trade_type> </xml>
		 */
		OrderReturnInfo returnInfo = requestWXPreOrder();

		if (returnInfo != null) {
			// 返回前端需要的下单参数.
			PrepayResult result = new PrepayResult();
			result.setNonceStr(RandomStringGenerator.getRandomStringByLength(32));
			result.setSignType("MD5");
			result.setTimeStamp(String.valueOf(System.currentTimeMillis() / 1000));
			result.setPackage_str("prepay_id=" + returnInfo.getPrepay_id());

			/*
			 * paySign = MD5( appId=wxd678efh567hg6787
			 * &nonceStr=5K8264ILTKCH16CQ2502SI8ZNMTM67VS
			 * &package=prepay_id=wx2017033010242291fcfe0db70013231072 &signType=MD5
			 * &timeStamp=1490840662 &key=qazwsxedcrfvtgbyhnujmikolp111111 ) =
			 * 22D9B4E54AB1950F51E0649E8810ACD6
			 */
			String paySign = MD5.MD5Encode("appId=" + Configure.getAppID() + "&nonceStr=" + result.getNonceStr()
					+ "&package=" + result.getPackage_str() + "&signType=MD5" + "&timeStamp=" + result.getTimeStamp()
					+ "&key=" + Configure.getKey());
			result.setPaySign(paySign);

			wxOrderModel.setPrepay_id(returnInfo.getPrepay_id());
			wxOrderModel.setStatusPrepaySuccess();
			wxOrderModel.save();

			renderJson(new Result(ResultCode.SUCCESS, "query success", result));
		} else {
			wxOrderModel.setStatusPrepayFailed();
			wxOrderModel.save();
			renderJson(new Result(ResultCode.PREPAY_ERROR, "prepay failed", null));
		}
	}

	private OrderReturnInfo requestWXPreOrder() {
		try {
			String openid = currentUser.getOpenid();
			OrderInfo order = new OrderInfo();
			order.setAppid(Configure.getAppID());
			order.setMch_id(Configure.getMch_id());
			order.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
			order.setBody("豫志愿-院校推荐");
			order.setOut_trade_no(RandomStringGenerator.getRandomStringByLength(32));
			order.setTotal_fee(1);
			order.setSpbill_create_ip(getRequest().getRemoteHost());
			logger1.info("getSpbill_create_ip:" + order.getSpbill_create_ip());
			order.setNotify_url("http://123.207.149.165:8080/yzyserver/wxpay/result");
			order.setTrade_type("JSAPI");
			order.setOpenid(openid);
			order.setSign_type("MD5");
			// 生成签名
			String sign = Signature.getSign(order);
			order.setSign(sign);

			String result = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", order);

			/*
			 * <xml> <appid>wxda057bdf3873a58e</appid> <mch_id>1501524091</mch_id>
			 * <nonce_str>ggy0t0voqr8mbecaxsrfuvk3jka68n9c</nonce_str>
			 * <sign_type>MD5</sign_type> <sign>D9A5EC76DCDAC76E76054F4FE0B5DB77</sign>
			 * <body>豫志愿-院校推荐</body>
			 * <out_trade_no>85727awodbonrai4rvxgxfvvx1xzis6s</out_trade_no>
			 * <total_fee>1</total_fee> <spbill_create_ip>114.243.215.79</spbill_create_ip>
			 * <notify_url>http://123.207.149.165:8080/yzyserver/wxpay/result</notify_url>
			 * <trade_type>JSAPI</trade_type> <openid>oi1oY42JvRcuSIbfzuW3uiyD1CBk</openid>
			 * </xml>
			 */

			System.out.println(result);
			logger1.info("---------下单返回:" + result);

			XStream xStream = new XStream();
			xStream.alias("xml", OrderReturnInfo.class);
			OrderReturnInfo returnInfo = (OrderReturnInfo) xStream.fromXML(result);

			if (returnInfo != null) {
				String resultSign = Signature.checkSign(returnInfo);
				logger1.info("-----result calc sign:" + resultSign);
				logger1.info("-----result sign:" + resultSign);
				if (!returnInfo.getSign().equals(resultSign)) {
					logger1.error("预下单返回信息sign校验失败");
					return null;
				}

				wxOrderModel = new WxOrderModel();
				wxOrderModel.setAppid(order.getAppid());
				wxOrderModel.setMch_id(order.getMch_id());
				wxOrderModel.setBody(order.getBody());
				wxOrderModel.setOut_trade_no(order.getOut_trade_no());
				wxOrderModel.setTotal_fee(order.getTotal_fee());
				wxOrderModel.setSpbill_create_ip(order.getSpbill_create_ip());
				wxOrderModel.setNotify_url(order.getNotify_url());
				wxOrderModel.setTrade_type(order.getTrade_type());
				wxOrderModel.setOpenid(order.getOpenid());

				return returnInfo;
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger1.error("-------", e);
		}
		return null;
	}
}