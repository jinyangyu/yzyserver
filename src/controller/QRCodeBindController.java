package controller;

import java.util.List;
import org.apache.log4j.Logger;
import com.jfinal.core.Controller;

import bean.dbmodel.QrCodeModel;
import bean.dbmodel.UserInfoModel;
import bean.requestresult.Result;
import constant.ResultCode;

public class QRCodeBindController extends Controller {
	public static Logger logger1 = Logger.getLogger(QRCodeBindController.class);

	public void bind() {

		logger1.info("QRCodeBindController bind");

		String clientSession = getPara("clientSession");

		logger1.info("clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			logger1.info("登陆信息失效,clientSession没查到");
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		String qrCode = getPara("scene");
		if (qrCode == null || "".equals(qrCode)) {
			qrCode = "0000";
		}

		UserInfoModel user = users.get(0);
		String openId = user.getOpenid();
		
		if(openId == null || "".equals(openId)) {
			
			logger1.error("用户 " + user.getNickName() + " openId 是空，数据异常!!!");
			
			renderJson(new Result(ResultCode.SUCCESS, "qrcode bind failed openId is Null", null));
			return;
		}
		
		List<QrCodeModel> bindList  = QrCodeModel.dao.find("select * from qrcode where openid = ?", openId);
		
		if(bindList == null || bindList.size() == 0) {
			QrCodeModel bind = new QrCodeModel();
			bind.setOpenid(openId);
			bind.setQrcode(qrCode);
			bind.setTime(String.valueOf(System.currentTimeMillis()));
			bind.save();
			
			renderJson(new Result(ResultCode.SUCCESS, "qrcode bind success", ""));
		}else {
			QrCodeModel bind = bindList.get(0);
			String errorString = "用户 " + user.getNickName() + " openId:" + openId + " 已绑定推广人："
					+ bind.getQrcode();
			logger1.error(errorString);
			renderJson(new Result(ResultCode.SUCCESS, "qrcode bind failed:" + errorString, null));
		}
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}
}
