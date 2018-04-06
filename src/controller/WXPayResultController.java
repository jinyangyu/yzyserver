package controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import com.jfinal.core.Controller;
import com.thoughtworks.xstream.XStream;

import demo.bean.WxOrderModel;
import demo.bean.WxPayResultInfo;
import demo.util.Signature;
import demo.util.StreamUtil;

public class WXPayResultController extends Controller {
	public static Logger logger1 = Logger.getLogger(WXPayResultController.class);

	public void result() throws IOException {

		logger1.info("----------WXPayResultController recommend");

		String reqParams = StreamUtil.read(getRequest().getInputStream());
		logger1.info("-------支付结果:" + reqParams);

		/*
		 * <xml> <appid><![CDATA[wxda057bdf3873a58e]]></appid>
		 * <bank_type><![CDATA[CFT]]></bank_type> <cash_fee><![CDATA[1]]></cash_fee>
		 * <fee_type><![CDATA[CNY]]></fee_type>
		 * <is_subscribe><![CDATA[N]]></is_subscribe>
		 * <mch_id><![CDATA[1501524091]]></mch_id>
		 * <nonce_str><![CDATA[ggy0t0voqr8mbecaxsrfuvk3jka68n9c]]></nonce_str>
		 * <openid><![CDATA[oi1oY42JvRcuSIbfzuW3uiyD1CBk]]></openid>
		 * <out_trade_no><![CDATA[85727awodbonrai4rvxgxfvvx1xzis6s]]></out_trade_no>
		 * <result_code><![CDATA[SUCCESS]]></result_code>
		 * <return_code><![CDATA[SUCCESS]]></return_code>
		 * <sign><![CDATA[279C0445D674C7C77BFBB41A27D3D18C]]></sign>
		 * <time_end><![CDATA[20180406204642]]></time_end> <total_fee>1</total_fee>
		 * <trade_type><![CDATA[JSAPI]]></trade_type>
		 * <transaction_id><![CDATA[4200000085201804063633378883]]></transaction_id>
		 * </xml>
		 */

		XStream xStream = new XStream();
		xStream.alias("xml", WxPayResultInfo.class);
		WxPayResultInfo payResultInfo = (WxPayResultInfo) xStream.fromXML(reqParams);

		if (!"SUCCESS".equals(payResultInfo.getResult_code())) {
			logger1.error("-------支付结果解析失败");
		}

		String resultSign;
		try {
			resultSign = Signature.checkSign(payResultInfo);
			logger1.info("-----result calc sign:" + resultSign);
			logger1.info("-----result sign:" + resultSign);
			if (!payResultInfo.getSign().equals(resultSign)) {
				logger1.error("-------支付结果校验失败");
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		List<WxOrderModel> orders = WxOrderModel.dao.find("select * from orders where openid = ? and out_trade_no = ?",
				payResultInfo.getOpenid(), payResultInfo.getOut_trade_no());

		if (orders == null || orders.size() != 1) {
			logger1.error("微信返回订单在本地数据库下单信息中查不到 数据库订单：" + orders);
		} else {
			logger1.info("校验支付信息是否一致");
			WxOrderModel order = orders.get(0);
			if (order.getTotal_fee() != payResultInfo.getTotal_fee()) {
				logger1.error("微信返回订单与数据库下单 支付金额不一致 数据库金额：" + order.getTotal_fee() + " 微信支付金额："
						+ payResultInfo.getTotal_fee());
			}
			if (!order.getMch_id().equals(payResultInfo.getMch_id())) {
				logger1.error("微信返回订单商户id不一致 订单商户id：" + order.getMch_id() + " 微信支付商户id：" + payResultInfo.getMch_id());
			}
			if (!order.getOpenid().equals(payResultInfo.getOpenid())) {
				logger1.error(
						"微信返回订单openid不一致 订单商户id：" + order.getOpenid() + " 微信支付openid：" + payResultInfo.getOpenid());
			}

			order.setBank_type(payResultInfo.getBank_type());
			order.setFee_type(payResultInfo.getFee_type());
			order.setIs_subscribe(payResultInfo.getIs_subscribe());
			order.setTime_end(payResultInfo.getTime_end());
			order.setTransaction_id(payResultInfo.getTransaction_id());

			order.setStatusPaySuccess();
			order.update();
		}

		logger1.info("-------支付结果Info:" + payResultInfo);

		StringBuffer sb = new StringBuffer("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");

		renderText("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
	}
}