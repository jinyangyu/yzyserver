package controller;

import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.Info;
import bean.dbmodel.UserInfoModel;
import bean.requestresult.InfoUserResult;
import bean.requestresult.Result;
import constant.ResultCode;
import datasource.DataSource;

public class InfoCheckController extends Controller {
	public static Logger logger1 = Logger.getLogger(InfoCheckController.class);
	private static Random random = new Random(System.currentTimeMillis());

	public void check() {

		logger1.info("InfoCheckController check");

//		String clientSession = getPara("clientSession");
//
//		logger1.info("clientSession:" + clientSession);
//
//		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
//				clientSession);
//		if (users == null || users.isEmpty()) {
//			logger1.info("登陆信息失效,clientSession没查到");
//			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
//			return;
//		}

		// if (DataSource.getInstance().shouldUserNotify(currentUser.getOpenid())) {
		Info info = DataSource.getInstance().getInfoUser();
		if (info != null) {
			String title = info.getTitle();
			String content = info.getContent();
			String button_text = info.getButton_text();
			int hasphone = info.getHasphone();
			String phone_num = info.getPhone_num();

			if (hasphone == 1) {
				String[] phones = phone_num.split(";");
				if (phones.length == 0) {
					hasphone = 0;
				} else {
					int index = random.nextInt(phones.length);
					if (index < phones.length && index >= 0) {
						phone_num = phones[index];
					} else {
						hasphone = 0;
					}
				}
			}

			logger1.info("title:" + title + "content:" + content + "button_text:" + button_text + "hasphone:" + hasphone
					+ "phone_num:" + phone_num);
			InfoUserResult result = new InfoUserResult(title, content, button_text, hasphone, phone_num);

			renderJson(new Result(ResultCode.SUCCESS, "notify", result));
			return;
		}

		renderJson(new Result(ResultCode.SUCCESS, "no need notify", null));
	}

	public static void main(String[] args) {
		String phone_num = "18210512166;13682068591;18888889999";
		String[] phones = phone_num.split(";");
		for (String phone : phones) {
			System.out.println("phone:" + phone);
		}
		for (int i = 0; i < 10; i++) {
			System.out.println("random:" + random.nextInt(phones.length));
		}

	}
}
