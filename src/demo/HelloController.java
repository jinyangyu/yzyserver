package demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.kit.HttpKit;

import bean.dbmodel.CollegeModel;
import bean.requestresult.Result;
import constant.Configure;
import controller.CollegeRecommendController;
import datasource.DataSource;

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

		/*
		 * "data": " { \"access_token\":
		 * \"9_pygitvSWEU1r-ivA54I0w8cTCNAa5rFKg88QQcDn0IKy4xKaEHMr9zr5Ozw2EakR5ivm3aXD6sGPh2wOO5wM-ufLr1y6OkqPmKDs-am0zQid1bRRHRHcWmNLZPTvyKwHg5ZguXBgYR8-FxhFQXMcADAMTI\",
		 * \"expires_in\":7200 }",
		 */

		String token = "9_pygitvSWEU1r-ivA54I0w8cTCNAa5rFKg88QQcDn0IKy4xKaEHMr9zr5Ozw2EakR5ivm3aXD6sGPh2wOO5wM-ufLr1y6OkqPmKDs-am0zQid1bRRHRHcWmNLZPTvyKwHg5ZguXBgYR8-FxhFQXMcADAMTI";

		HashMap<String, String> queryParas = new HashMap<String, String>();
		queryParas.put("scene", "yujinyang");
		queryParas.put("page", "pages/index/index");
		queryParas.put("width", "430");
		queryParas.put("auto_color", "true");
		
		

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + token);
		httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
		String body = JSON.toJSONString(queryParas);
		StringEntity entity;
		
		String result = "";
		try {
			entity = new StringEntity(body);

			entity.setContentType("image/png");

			httpPost.setEntity(entity);
			HttpResponse response;

			response = httpClient.execute(httpPost);
			InputStream inputStream = response.getEntity().getContent();

			File codeFile = new File("/root/apache-tomcat-7.0.85/webapps/yzyserver/yujinyang_code.png");
			FileOutputStream out = new FileOutputStream(codeFile);

			byte[] buffer = new byte[8192];
			int bytesRead = 0;
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				out.write(buffer, 0, bytesRead);
			}

			out.flush();
			out.close();
			
			result = "save png " + codeFile.getAbsolutePath() + " size:" + codeFile.getTotalSpace();
		} catch (Exception e) {
			result = e.getMessage();
			e.printStackTrace();
		}

		renderJson(new Result(200, "query success", result));
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

}