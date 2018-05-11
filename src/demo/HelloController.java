package demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.exceptions.ClientException;
import com.jfinal.core.Controller;

import bean.dbmodel.CollegeModel;
import bean.requestresult.Result;
import constant.ResultCode;
import controller.CollegeRecommendController;
import datasource.DataSource;
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

//		 String result =
//		 HttpKit.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
//		 + Configure.getAppID()
//		 + "&secret=" + Configure.getSecret());

		/*
		 {
			"data": "{\"access_token\":\"9_3ef7d4Kqv1To5ZtZi3wo79kFOhGalh-pm-4FHEXsWzkYLSoLIiN622A_P1Hq4fp-kplXeY1Tsv36SJ5cbX_4H9PaZgCCK3g7vwASCNe52gH5_VN5HG505ddh6f3MmAAckenQnMhamR7q12HbOSKbAEADHZ\",\"expires_in\":7200}",
			"result_code": 200,
			"result_msg": "query success"
		 }
		 */

		String token = "9_3ef7d4Kqv1To5ZtZi3wo79kFOhGalh-pm-4FHEXsWzkYLSoLIiN622A_P1Hq4fp-kplXeY1Tsv36SJ5cbX_4H9PaZgCCK3g7vwASCNe52gH5_VN5HG505ddh6f3MmAAckenQnMhamR7q12HbOSKbAEADHZ";

		
		for(int i=1000;i<2000;i++) {
			createQrCode(token, i);
		}
		

		renderJson(new Result(200, "query success", ""));
	}
	
	private void createQrCode(String token, int number) {
		HashMap<String, Object> queryParas = new HashMap<String, Object>();
		queryParas.put("scene", "tuiguang_" + number);
		queryParas.put("page", "pages/index/index");
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
			
//			File codeFile = new File("/root/apache-tomcat-7.0.85/webapps/yzyserver/yujinyang_code.png");
			
			String qrFileName = "/Users/yujinyang11/Desktop/app_code_1000_2000/豫志愿推广二维码_" + number + ".png";
			
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
	
	public void setRank() {
		List<CollegeModel> allCollege = DataSource.getInstance().getAllColleges();
		
		for (CollegeModel model : allCollege) {
			int li_2015 = 0;
			int li_2016 = 0;
			int li_2017 = 0;

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

			model.setLi_2015_rank(DataSource.getInstance().get2015RankByScore(li_2015, false));
			model.setLi_2016_rank(DataSource.getInstance().get2016RankByScore(li_2016, false));
			model.setLi_2017_rank(DataSource.getInstance().get2017RankByScore(li_2017, false));

			int wen_2015 = 0;
			int wen_2016 = 0;
			int wen_2017 = 0;

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

			model.setWen_2015_rank(DataSource.getInstance().get2015RankByScore(wen_2015, true));
			model.setWen_2016_rank(DataSource.getInstance().get2016RankByScore(wen_2016, true));
			model.setWen_2017_rank(DataSource.getInstance().get2017RankByScore(wen_2017, true));

			model.update();
		}
	}
	
	public void clearRecommendCache() {
		renderHtml(DataSource.getInstance().clearRecommendCache());
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

}