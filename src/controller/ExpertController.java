package controller;

import java.util.List;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.SmsModel;
import bean.dbmodel.UserInfoModel;
import bean.requestresult.Result;
import constant.ResultCode;

public class ExpertController extends Controller {
	public static Logger logger1 = Logger.getLogger(ExpertController.class);

	public void order() {

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
		String verify_code = getPara("verify_code");

		if (phone == null || "".equals(phone) || verify_code == null || "".equals(verify_code)) {
			logger1.info("参数异常，手机号、验证码为空");
			renderJson(new Result(ResultCode.SMS_ERROR, "手机号、验证码为空，请正确输入", null));
			return;
		}

		List<SmsModel> smsList = SmsModel.dao.find("select * from sms_verify where phone = ?", phone);
		if (smsList == null || smsList.size() == 0) {
			logger1.info("数据库没有发送验证码记录，请先获取验证码");
			renderJson(new Result(ResultCode.SMS_ERROR, "请先获取验证码", null));
			return;
		}

		SmsModel lastSms = smsList.get(smsList.size() - 1);
		long current = System.currentTimeMillis();
		long lastSmsSendTime = 0;
		try {
			lastSmsSendTime = Long.parseLong(lastSms.getSendtime());
		} catch (Exception e) {
		}
		if (current - lastSmsSendTime > 5 * 60 * 1000) {
			logger1.info("current:" + current + " lastSmsSendTime:" + lastSmsSendTime + "验证码已过期,请重新获取");
			renderJson(new Result(ResultCode.SMS_ERROR, "验证码已过期,请重新获取", null));
			return;
		}

		if (!lastSms.getCode().equals(verify_code)) {
			logger1.info("lastSms.getCode():" + lastSms.getCode() + " verify_code:" + verify_code + "验证码有误，请正确输入");
			renderJson(new Result(ResultCode.SMS_ERROR, "验证码有误，请正确输入", null));
			return;
		}

		logger1.info("验证码正确,发起预约订单");
		renderJson(new Result(ResultCode.SUCCESS, "验证码正确,发起预约订单", null));
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
