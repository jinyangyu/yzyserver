package controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

import bean.dbmodel.CollegeModel;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.WxPayOrderModel;
import bean.dbmodel.YzyOrderModel;
import bean.requestinfo.CheckPayOrderInfo;
import bean.requestinfo.WxPayResultInfo;
import bean.requestresult.CollegeChanceResult;
import bean.requestresult.CollegeResult;
import bean.requestresult.RankResult;
import bean.requestresult.Result;
import constant.Configure;
import constant.ResultCode;
import datasource.ChanceUtil;
import datasource.DataSource;
import datasource.SortAndFilter;
import util.HttpRequest;
import util.RandomStringGenerator;
import util.Signature;

public class CollegeRecommendController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	private static final int DEFAULT_PAGE_SIZE = 20;

	private String subject;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private int pageNum = 0;

	boolean checkOrder = false;

	public void recommend() {

		logger1.info("CollegeRecommendController recommend");

		String scoreStr = getPara("score");
		subject = getPara("subject");
		String clientSession = getPara("clientSession");
		String out_trade_no = getPara("out_trade_no");

		try {
			pageSize = Integer.parseInt(getPara("pageSize"));
		} catch (Exception e) {
		}
		if (pageSize <= 10) {
			pageSize = 10;
		}

		try {
			pageNum = Integer.parseInt(getPara("pageNum"));
		} catch (Exception e) {
		}

		logger1.info("scoreStr:" + scoreStr + " subject:" + subject + " clientSession:" + clientSession
				+ " out_trade_no:" + out_trade_no + " pageSize:" + pageSize + " pageNum:" + pageNum);

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
		UserInfoModel user = users.get(0);
		logger1.info("user:" + user.getNickName() + " openId:" + user.getOpenid());

		if ("".equals(user.getOpenid())) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		if (checkOrder) {
			List<YzyOrderModel> yzyOrders = YzyOrderModel.dao.find(
					"select * from orders_yzy where out_trade_no = ? and type = 1 and openid = ?", out_trade_no,
					user.getOpenid());
			if (yzyOrders != null && yzyOrders.size() != 0 && yzyOrders.get(0).getOut_trade_no() != null
					&& !"".equals(yzyOrders.get(0).getOut_trade_no()) && score == yzyOrders.get(0).getScore()
					&& subject.equals(yzyOrders.get(0).getSubject())) {
				// 2、检查订单数据，如果是已存在完成的订单，则直接查询。
				doRecommendByScore(score, isWen);

				yzyOrders.get(0).setTime(String.valueOf(System.currentTimeMillis()));
				yzyOrders.get(0).update();
			} else { // 1、新订单，检查支付结果
				startRecommendByCheckWxOrder(out_trade_no, user, score, isWen);
			}
		} else {
			doRecommendByScore(score, isWen);
		}
		
//		 doRecommendByScore(score, isWen);
	}

	private void startRecommendByCheckWxOrder(String out_trade_no, UserInfoModel user, int score, boolean isWen) {
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
			// trade_state 已为 SUCCESS，说明已经手动检查过，本次请求从我的订单发起。
		} else {

			// 手动检查支付结果。
			try {
				if (checkWxOrder(wxOrder)) {
				} else {
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		YzyOrderModel yzyOrder = new YzyOrderModel();
		yzyOrder.setOut_trade_no(out_trade_no);
		yzyOrder.setScore(score);
		yzyOrder.setOpenid(user.getOpenid());
		yzyOrder.setSubject(subject);
		yzyOrder.setTime(String.valueOf(System.currentTimeMillis()));
		yzyOrder.setRecommendType();
		yzyOrder.save();
		doRecommendByScore(score, isWen);
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

	private void doRecommendByScore(int score, boolean isWen) {
		List<CollegeModel> colleges_all = DataSource.getInstance().recommendCollege(score, isWen);
		logger1.info("doRecommendByScore colleges_all:" + colleges_all.size());

		String sortKey = getPara("sort");
		String filters = getPara("filters");
		String provinces = getPara("provinces");

		if (filters != null && !"".equals(filters)) {
			try {
				filters = URLDecoder.decode(filters, "UTF-8");
				logger1.info("filters UTF8:" + filters);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (provinces != null && !"".equals(provinces)) {
			try {
				provinces = URLDecoder.decode(provinces, "UTF-8");
				logger1.info("provinces UTF8:" + provinces);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		logger1.info("doRecommendByScore sortKey:" + sortKey + " filters:" + filters + " isWen:" + isWen + " provinces:"
				+ provinces);

		// 填入录取概率
		CollegeResult collegeResult = new CollegeResult();
		ChanceUtil chanceUtil = new ChanceUtil();
		List<CollegeChanceResult> chanceCollegeList = chanceUtil.getChanceList(colleges_all, isWen, score);

		// 对含概率的院校列表进行排序+筛选
		SortAndFilter sortAndFilter = new SortAndFilter(chanceCollegeList, sortKey, filters, provinces, isWen);
		chanceCollegeList = sortAndFilter.getResults();

		logger1.info("doRecommendByScore chanceCollegeList:" + chanceCollegeList.size());
		int totalPage = Math.max(1, (int) Math.ceil((double) chanceCollegeList.size() / (double) pageSize));

		List<CollegeChanceResult> resultCollegeList = new ArrayList<CollegeChanceResult>();
		if (chanceCollegeList.size() > 0) {

			logger1.info("doRecommendByScore totalPage:" + totalPage);

			if (pageNum >= totalPage) {

			} else {

				logger1.info("doRecommendByScore college from:" + (pageNum * pageSize) + " to:"
						+ Math.min(colleges_all.size(), (pageNum + 1) * pageSize));
				for (int i = (pageNum * pageSize); i < Math.min(chanceCollegeList.size(),
						(pageNum + 1) * pageSize); i++) {
					resultCollegeList.add(chanceCollegeList.get(i));
				}
			}
		}

		collegeResult.setTotalPage(totalPage);
		collegeResult.setTotalSize(colleges_all.size());
		collegeResult.setColleges(resultCollegeList);

		if (!colleges_all.isEmpty()) {
			List<RankResult> ranks = new ArrayList<RankResult>();
			ranks.add(
					new RankResult("2017", String.valueOf(DataSource.getInstance().get2017RankByScore(score, isWen))));
			ranks.add(
					new RankResult("2016", String.valueOf(DataSource.getInstance().get2016RankByScore(score, isWen))));
			ranks.add(
					new RankResult("2015", String.valueOf(DataSource.getInstance().get2015RankByScore(score, isWen))));
			collegeResult.setRanks(ranks);
		}

		renderJson(new Result(ResultCode.SUCCESS, "query success", collegeResult));
	}

}