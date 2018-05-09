package controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.List;

import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

import bean.dbmodel.UserInfoModel;
import bean.requestresult.Result;
import constant.Configure;
import constant.ResultCode;

public class UserInfoController extends Controller {
	public static Logger logger1 = Logger.getLogger(UserInfoController.class);

	public void save() {
		logger1.info("UserInfoController save");

		String jsCode = getPara("js_code");
		String loginInfo = "";
		
		try {
			loginInfo = URLDecoder.decode(getPara("loginInfo"),"UTF-8");
			logger1.info("loginInfoUTF8:" + loginInfo);	
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger1.info("jsCode:" + jsCode);
		logger1.info("loginInfo:" + loginInfo);

		// {"session_key":"fa3BBl\/cf1Bv6WwLZgpb2w==","openid":"o4GPD5O_aVdtrrZJvDMQ-WL3hDUI"}
		// openid 用户唯一标识
		// session_key 会话密钥
		String result = HttpKit.get("https://api.weixin.qq.com/sns/jscode2session?" + "appid=" + Configure.getAppID()
				+ "&secret=" + Configure.getSecret() + "&js_code=" + jsCode + "&grant_type=authorization_code");

		logger1.info("https://api.weixin.qq.com/sns/jscode2session result:" + result);

		JSONObject jo = JSON.parseObject(result);
		String session_key = jo.getString("session_key");
		String openid = jo.getString("openid");
		String clientSession = MD5(session_key + "_" + openid);
		logger1.info("clientSession:" + clientSession);

		JSONObject loginInfoJO = JSON.parseObject(loginInfo);
		JSONObject userInfoJO = loginInfoJO.getJSONObject("userInfo");

		UserInfoModel userinfo = JSON.toJavaObject(userInfoJO, UserInfoModel.class);
		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where openid = ?",
				openid);
		if (users != null && !users.isEmpty()) {
			UserInfoModel user = users.get(0);
			user.setAvatarUrl(userinfo.getAvatarUrl());
			user.setCity(userinfo.getCity());
			user.setCountry(userinfo.getCountry());
			user.setGender(userinfo.getGender());
			user.setLanguage(userinfo.getLanguage());
			user.setNickName(userinfo.getNickName());
			user.setProvince(userinfo.getProvince());
			user.setOpenid(openid);
			user.setSessionKey(session_key);
			user.setClientSession(clientSession);

			logger1.info("user update:" + user.toString());	
			user.update();
		} else {
			userinfo.setOpenid(openid);
			userinfo.setSessionKey(session_key);
			userinfo.setClientSession(clientSession);
			
			logger1.info("user update:" + userinfo.toString());
			userinfo.save();
		}
		renderJson(new Result(ResultCode.SUCCESS, "success", clientSession));
	}

	private String MD5(String s) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(s.getBytes("utf-8"));
			return toHex(bytes);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static String toHex(byte[] bytes) {

		final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
		StringBuilder ret = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
			ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return ret.toString();
	}
}
