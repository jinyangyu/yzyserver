package controller;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.SmsModel;
import bean.dbmodel.UserInfoModel;
import bean.requestresult.Result;
import constant.ResultCode;
import util.MobileUtil;

public class SmsController extends Controller {
	public static Logger logger1 = Logger.getLogger(SmsController.class);
	private static Random random = new Random(System.currentTimeMillis());

	private static final String PHONE_NUMBER_INVALIDATE = "请输入正确的手机号";
	private static final String REQUEST_FAST_ERROR = "发送过快，请稍后尝试";
	private static final String SMS_SERVER_ERROR = "短信发送失败，请稍后尝试";

	public void send() {

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
		
		String phone = getPara("phone");
		if (!MobileUtil.isMobile(phone)) {
			logger1.info(phone + " " + PHONE_NUMBER_INVALIDATE);
			renderJson(new Result(ResultCode.SMS_ERROR, PHONE_NUMBER_INVALIDATE, null));
			return;
		}

		List<SmsModel> smsList = SmsModel.dao.find("select * from sms_verify where phone = ?", phone);
		if (smsList != null && smsList.size() > 0) {
			SmsModel lastSms = smsList.get(smsList.size() - 1);
			long current = System.currentTimeMillis();
			long lastSmsSendTime = 0;
			try {
				lastSmsSendTime = Long.parseLong(lastSms.getSendtime());
			} catch (Exception e) {
			}
			if(current - lastSmsSendTime < 10 * 1000) {
				logger1.info("current:" + current + " lastSmsSendTime:" + lastSmsSendTime + " " + PHONE_NUMBER_INVALIDATE);
				renderJson(new Result(ResultCode.SMS_ERROR, REQUEST_FAST_ERROR, null));
				return;
			}
		}
		
		int randCode = (int) (random.nextDouble() * 1000000);
		logger1.info("发送验证码：" + randCode + " to phone:" + phone);

		// TODO request sms_server to send sms

		SmsModel smsModel = new SmsModel();
		smsModel.setPhone(phone);
		smsModel.setCode(String.valueOf(randCode));
		smsModel.setSendtime(String.valueOf(System.currentTimeMillis()));
		smsModel.save();

		logger1.info("发送验证码成功,等待发起专家预约订单请求");
		renderJson(new Result(ResultCode.SUCCESS, "sms send success", null));
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
