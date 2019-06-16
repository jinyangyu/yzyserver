package demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.jfinal.core.Controller;

import bean.dbmodel.CollegeModelAll;
import bean.dbmodel.QrCodeModel;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.WxPayOrderModel;
import bean.requestresult.Result;
import constant.ResultCode;
import controller.CollegeRecommendController;
import datasource.DataSource;
import datasource.RecommendSource;
import util.LianKaoDataBuilder;
import util.sms.SmsUtil;

public class HelloController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegeRecommendController.class);

	public void index() {

		System.out.println("hello request");

		System.out.println("select * from college");

		// https://www.yujinyang.cn/yzyserver/recommend/recommend?
		// score=555&
		// subject=wenke&
		// clientSession=9C128580E4A0847085274B79CFFA610A&
		// out_trade_no=4qrm7ss1n7fselph49nhynollii3ctf8&
		// page=2&pageSize=20&
		// sort=probability&
		// filters=理工类,财经类,艺术类,医学类

		// List<CollegeModel> colleges =
		// DataSource.getInstance().recommendCollege(100,true);

		// String result =
		// HttpKit.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
		// + Configure.getAppID()
		// + "&secret=" + Configure.getSecret());
		//
		// System.out.println("result:" + result);

		/*
		 * result:{ "access_token":
		 * "21_rlWyR65KJgx2ql_pzCJ9A4MlnXjy1Pa-VcZQw5KFXvqJvJ58juXi1XcV8ybiZ1JbhFHYW_LSh5fY1kyeC2_GEppaSjYefTIBP_kYzJMJXs5od7xfSNxtDtfAnRllTTrSGoiIq_Cr0T9asIe5NCUcACAHWO",
		 * "expires_in":7200}
		 */

		String token = "21_rlWyR65KJgx2ql_pzCJ9A4MlnXjy1Pa-VcZQw5KFXvqJvJ58juXi1XcV8ybiZ1JbhFHYW_LSh5fY1kyeC2_GEppaSjYefTIBP_kYzJMJXs5od7xfSNxtDtfAnRllTTrSGoiIq_Cr0T9asIe5NCUcACAHWO";

		for (int i = 6001; i < 6999; i++) {
			createQrCode(token, i);
		}

		renderJson(new Result(200, "query success", ""));
	}

	private void createQrCode(String token, int number) {
		HashMap<String, Object> queryParas = new HashMap<String, Object>();
		queryParas.put("scene", "tuiguang_" + number);
		queryParas.put("page", "pages/expert/expert");
		queryParas.put("width", "430");
		queryParas.put("auto_color", true);

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token);
		httpPost.addHeader("content-type", "application/json");
		String body = JSON.toJSONString(queryParas);

		System.out.println(body);

		StringEntity entity;

		String result = "";
		try {
			entity = new StringEntity(body);

			entity.setContentType("image/png");

			httpPost.setEntity(entity);
			HttpResponse response;

			response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();

			// File codeFile = new
			// File("/root/apache-tomcat-7.0.85/webapps/yzyserver/yujinyang_code.png");

			String qrFileName = "/Users/yujinyang11/Desktop/app_code_6001_6999/豫志愿推广二维码_" + number + ".png";

			File codeFile = new File(qrFileName);

			FileOutputStream out = new FileOutputStream(codeFile);

			byte[] buffer = new byte[8192];
			int bytesRead = 0;
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

			out.flush();
			out.close();

			System.out.println("成功生成 " + qrFileName);
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		}
	}

	public void resetAdmins() {
		DataSource.getInstance().resetAdmin();
		renderJson(new Result(200, "重新加载管理员成功", ""));
	}

	public void setRank() {
		List<CollegeModelAll> allCollege = DataSource.getInstance().getAllColleges();

		for (CollegeModelAll model : allCollege) {
			int li_2015 = 0;
			int li_2016 = 0;
			int li_2017 = 0;
			int li_2018 = 0;

			if (model.getLi_2015() != null && !"".equals(model.getLi_2015())) {
				try {
					li_2015 = Integer.parseInt(model.getLi_2015());
				} catch (Exception e) {
				}
			}
			if (model.getLi_2016() != null && !"".equals(model.getLi_2016())) {
				try {
					li_2016 = Integer.parseInt(model.getLi_2016());
				} catch (Exception e) {
				}
			}
			if (model.getLi_2017() != null && !"".equals(model.getLi_2017())) {
				try {
					li_2017 = Integer.parseInt(model.getLi_2017());
				} catch (Exception e) {
				}
			}
			if (model.getLi_2018() != null && !"".equals(model.getLi_2018())) {
				try {
					li_2018 = Integer.parseInt(model.getLi_2018());
				} catch (Exception e) {
				}
			}

			// model.setLi_2015_rank(DataSource.getInstance().get2015RankByScore(li_2015,
			// false));
			// model.setLi_2016_rank(DataSource.getInstance().get2016RankByScore(li_2016,
			// false));
			// model.setLi_2017_rank(DataSource.getInstance().get2017RankByScore(li_2017,
			// false));
			model.setLi_2018_rank(DataSource.getInstance().get2018RankByScore(li_2018, false));

			int wen_2015 = 0;
			int wen_2016 = 0;
			int wen_2017 = 0;
			int wen_2018 = 0;

			if (model.getWen_2015() != null && !"".equals(model.getWen_2015())) {
				try {
					wen_2015 = Integer.parseInt(model.getWen_2015());
				} catch (Exception e) {
				}
			}
			if (model.getWen_2016() != null && !"".equals(model.getWen_2016())) {
				try {
					wen_2016 = Integer.parseInt(model.getWen_2016());
				} catch (Exception e) {
				}
			}
			if (model.getWen_2017() != null && !"".equals(model.getWen_2017())) {
				try {
					wen_2017 = Integer.parseInt(model.getWen_2017());
				} catch (Exception e) {
				}
			}
			if (model.getWen_2018() != null && !"".equals(model.getWen_2018())) {
				try {
					wen_2018 = Integer.parseInt(model.getWen_2018());
				} catch (Exception e) {
				}
			}

			// model.setWen_2015_rank(DataSource.getInstance().get2015RankByScore(wen_2015,
			// true));
			// model.setWen_2016_rank(DataSource.getInstance().get2016RankByScore(wen_2016,
			// true));
			// model.setWen_2017_rank(DataSource.getInstance().get2017RankByScore(wen_2017,
			// true));
			model.setWen_2018_rank(DataSource.getInstance().get2018RankByScore(wen_2018, true));

			model.update();
		}

		renderJson(new Result(ResultCode.SUCCESS, "更新成功", null));
	}

	public void clearRecommendCache() {
		renderHtml(DataSource.getInstance().clearRecommendCache());
	}

	public void resetShouldUserNotify() {
		DataSource.getInstance().resetNotifyedUser();
		renderJson(new Result(200, "重置成功", null));
	}

	public void resetInfo() {
		DataSource.getInstance().resetInfoUser();
		renderJson(new Result(200, "重置成功", DataSource.getInstance().getInfoUser()));
	}

	public void sendSMS() {
		try {
			SmsUtil.sendSms("SMS_130815233", "18210512166", "{\"code\":\"" + String.valueOf(444232) + "\"}");
		} catch (ClientException e) {
			e.printStackTrace();
			logger1.info("发送验证码失败,阿里云服务器错误");
			renderJson(new Result(ResultCode.SMS_ERROR, "短信发送失败，请稍后尝试", null));
			return;
		}
		renderJson(new Result(ResultCode.SUCCESS, "短信发送成功", null));
	}

	public void parseQrcode() {
		HashMap<String, String> userMap = new HashMap<String, String>();

		List<UserInfoModel> userList = UserInfoModel.dao.find("select * from userinfo");
		for (UserInfoModel user : userList) {
			userMap.put(user.getNickName(), user.getOpenid());
		}

		List<QrCodeModel> qrcodeList = QrCodeModel.dao.find("select * from qrcode");
		HashMap<String, QrCodeModel> qrcodeMap = new HashMap<String, QrCodeModel>();
		for (QrCodeModel qrcode : qrcodeList) {
			qrcodeMap.put(qrcode.getOpenid(), null);
		}

		int need = 0;
		int notNeed = 0;
		int total = 0;
		int dbTotal = qrcodeList.size();

		LogParser parser = new LogParser();
		try {
			List<UserQr> userQrList = parser.getUserQrcode();
			total = userQrList.size();
			for (UserQr userQr : userQrList) {
				if (userMap.containsKey(userQr.nickName)) {
					if (qrcodeMap.containsKey(userMap.get(userQr.nickName))) {
						notNeed++;
					} else {
						need++;
						// QrCodeModel qrCodeModel = new QrCodeModel();
						// qrCodeModel.setOpenid(userMap.get(userQr.nickName));
						// qrCodeModel.setQrcode(userQr.qrcode);
						// qrCodeModel.save();
					}
				} else {
					System.out.println(userQr.nickName + " 不存在 !!!!!!!!!!");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		renderJson(new Result(ResultCode.SUCCESS,
				"数据库共:" + dbTotal + " 待确认共:" + total + "； " + notNeed + " 已存在推荐人；" + need + " 需要写入", null));
	}

	public void qrcodeCount() {
		List<QrCodeModel> qrcodeList = QrCodeModel.dao.find("select * from qrcode");
		List<WxPayOrderModel> ordersList = WxPayOrderModel.dao
				.find("select * from orders_wxpay where total_fee in (3900,4900) and status='PAY_SUCCESS'");

		HashMap<String, WxPayOrderModel> orderMap = new HashMap<String, WxPayOrderModel>();
		for (WxPayOrderModel order : ordersList) {
			orderMap.put(order.getOpenid(), order);
		}

		HashMap<String, List<String>> qrMap = new HashMap<String, List<String>>();
		for (QrCodeModel qrcode : qrcodeList) {
			if (qrMap.containsKey(qrcode.getQrcode())) {
				List<String> ids = qrMap.get(qrcode.getQrcode());
				if (ids.contains(qrcode.getOpenid())) {
					System.out.println("推荐人写入重复！！！！！");
				} else {
					ids.add(qrcode.getOpenid());
				}
			} else {
				List<String> ids = new ArrayList<String>();
				ids.add(qrcode.getOpenid());
				qrMap.put(qrcode.getQrcode(), ids);
			}
		}

		String line = "";
		for (String code : qrMap.keySet()) {
			List<String> openidList = qrMap.get(code);
			line = code + "\t" + openidList.size() + "\t";

			int payCount1 = 0;
			int payCount2 = 0;
			int payCount3 = 0;
			for (String openid : openidList) {
				if (orderMap.containsKey(openid)) {
					WxPayOrderModel order = orderMap.get(openid);
					if (order.getTotal_fee() == 3900) {
						payCount1++;
					} else if (order.getTotal_fee() == 4900) {
						payCount2++;
					} else {
						payCount3++;
					}
				}
			}
			line += payCount1;
			line += "\t";

			line += payCount2;
			line += "\t";

			line += payCount3;
			line += "\t";
			System.out.println(line);
		}

		renderJson(new Result(ResultCode.SUCCESS, "", null));
	}

	public void initLiankaoData() {
		LianKaoDataBuilder.getInstance().init();
		renderJson(new Result(ResultCode.SUCCESS, "", null));
	}

	public void newRecommend() {
		RecommendSource.getInstance().init();
		renderJson(new Result(ResultCode.SUCCESS, "init", null));
	}

	public void fixWen() {
		RecommendSource.getInstance().fixWen();
	}

	public void userCount() {
		String nickName = getPara("nickName");
		try {
			String nickName1 = new String(nickName.getBytes("UTF-8"), "UTF-8");
			System.out.println("nickName1:" + nickName1);
			String nickName2 = new String(nickName.getBytes("GBK"), "UTF-8");
			System.out.println("nickName2:" + nickName2);
			String nickName3 = new String(nickName.getBytes("iso-8859-1"), "UTF-8");
			System.out.println("nickName3:" + nickName3);

			nickName = nickName3;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		System.out.println("nickName:" + nickName);
		try {
			nickName = new String(Base64.encodeBase64(nickName.getBytes("UTF-8")), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where nickName = ?", nickName);

		if (users == null || users.size() == 0) {
			renderJson(new Result(ResultCode.SUCCESS, "昵称未查到对应账号，是否改过昵称。请联系管理员", null));
			return;
		}

		if (users.size() > 1) {
			renderJson(new Result(ResultCode.SUCCESS, "昵称对应多个账号，请联系管理员", null));
			return;
		}

		UserInfoModel user = users.get(0);

		String openId = user.getOpenid();

		List<QrCodeModel> bindList = QrCodeModel.dao.find("select * from qrcode where shareFrom = ?", openId);
		if (bindList != null) {
			int firstCount = bindList.size();
			int secondCount = 0;

			List<QrCodeModel> qrQueue = new ArrayList<QrCodeModel>();
			for (QrCodeModel model : bindList) {
				qrQueue.add(model);
			}

			while (!qrQueue.isEmpty()) {
				QrCodeModel model = qrQueue.remove(0);
				System.out.println("qrQueue size:" + qrQueue.size());
				List<QrCodeModel> qrlist = QrCodeModel.dao.find("select * from qrcode where shareFrom = ?",
						model.getOpenid());
				if (qrlist != null) {
					System.out.println("二级分享人：" + model.getOpenid() + " 分享：" + qrlist.size());
					secondCount += qrlist.size();
					for (QrCodeModel m : qrlist) {
						qrQueue.add(m);
					}
					System.out.println("after add qrQueue size:" + qrQueue.size());
				}
			}

			renderHtml(
					"<h1>一级转发人数:" + firstCount + "</h1>" + 
					"<h1>二级转发人数:" + secondCount + "</h1>" +
					"<h1>总转发人数:" + (firstCount + secondCount) + "</h1>"
					);

			return;
		}
		renderJson(new Result(ResultCode.SUCCESS, "尚未转发", null));

	}

}