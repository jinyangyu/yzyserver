package controller;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import com.jfinal.core.Controller;
import com.thoughtworks.xstream.XStream;

import bean.dbmodel.PrepayCountModel;
import bean.dbmodel.WxPayOrderModel;
import bean.requestinfo.WxPayResultInfo;
import util.Signature;
import util.StreamUtil;

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

		logger1.info("-------支付结果Info:" + payResultInfo);

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

		List<WxPayOrderModel> orders = WxPayOrderModel.dao.find(
				"select * from orders_wxpay where openid = ? and out_trade_no = ?", payResultInfo.getOpenid(),
				payResultInfo.getOut_trade_no());

		if (orders == null || orders.size() != 1) {
			logger1.error("微信返回订单在本地数据库下单信息中查不到 数据库订单：" + orders);
		} else {
			logger1.info("校验支付信息是否一致");
			WxPayOrderModel order = orders.get(0);
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

			if (!"SUCCESS".equals(payResultInfo.getResult_code())) {
				order.setStatusPayFailed();
				order.update();
				logger1.error("------- 支付结果为支付失败");
			}

			if (order.getTotal_fee() != payResultInfo.getTotal_fee()
					|| !order.getMch_id().equals(payResultInfo.getMch_id())
					|| !order.getOpenid().equals(payResultInfo.getOpenid())) {

				logger1.error("------- 支付信息与本地不一致");
				return;
			}

			order.setBank_type(payResultInfo.getBank_type());
			order.setFee_type(payResultInfo.getFee_type());
			order.setIs_subscribe(payResultInfo.getIs_subscribe());
			order.setTime_end(payResultInfo.getTime_end());
			order.setTransaction_id(payResultInfo.getTransaction_id());
			logger1.info("------- 支付结果payResultInfo.getTransaction_id()：" + payResultInfo.getTransaction_id());

			order.setStatusPaySuccess();

			logger1.info("update order:" + order.toString() + " \n===========in to databases");
			order.update();

			if ("PREPAY".equals(order.getPayType())) {
				updatePrepayCount(order.getOpenid(), order.getOut_trade_no(), order.getExtra());
			}
		}

		renderText("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
	}

	/**
	 * 检查是否是预购买成功回调，如果是，则将预购买次数，写入数据库
	 */
	private void updatePrepayCount(String openid, String out_trade_no, String extra) {
		// 预购买次数下单成功，写入购买次数
		int prepayCount = Integer.parseInt(extra);

		List<PrepayCountModel> prepayCounts = PrepayCountModel.dao
				.find("select * from prepay_count where out_trade_no = ?", out_trade_no);

		if (prepayCounts != null || prepayCounts.size() >= 1) {
			logger1.info("微信推送支付结果，支付成功。此次支付的预购买记录已写入，说明支付后，快速点击了院校推荐，那里手动查询了微信支付结果。此次推送结果滞后。不写入预购次数");
		} else {
			logger1.info("微信推送支付结果，支付成功。写入预购次数");
			
			PrepayCountModel prepayModel = new PrepayCountModel();
			prepayModel.setOpenid(openid);
			prepayModel.setOut_trade_no(out_trade_no);
			prepayModel.setCurrentCount(prepayCount);
			prepayModel.setTotalCount(prepayCount);
			prepayModel.setTime(String.valueOf(System.currentTimeMillis()));
			prepayModel.save();
		}
	}
}