package controller;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

import bean.dbmodel.CollegeModel;
import bean.dbmodel.UserInfoModel;
import bean.dbmodel.YzyOrderModel;
import bean.requestresult.PdfResult;
import bean.requestresult.Result;
import constant.ResultCode;
import datasource.DataSource;
import util.pdf.PDFUtil;

public class CollegePDFController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegePDFController.class);

	public void createPDF() {
		String clientSession = getPara("clientSession");
		String out_trade_no = getPara("out_trade_no");
		String scoreStr = getPara("score");
		String subject = getPara("subject");

		if (out_trade_no == null || "".equals(out_trade_no)) {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "支付信息缺失", null));
			return;
		}

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			renderJson(new Result(ResultCode.LOGIN_ERROR, "登陆信息失效", null));
			return;
		}

		UserInfoModel user = users.get(0);

		if (scoreStr == null || "".equals(scoreStr)) {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "查询信息缺失", null));
			return;
		}

		if (subject == null || "".equals(subject)) {
			renderJson(new Result(ResultCode.PARAMS_ERROR, "查询信息缺失", null));
			return;
		}

		int score = Integer.parseInt(scoreStr);

		List<YzyOrderModel> yzyOrders = YzyOrderModel.dao.find(
				"select * from orders_yzy where out_trade_no = ? and type = 1 and openid = ? and score=? and subject = ?",
				out_trade_no, user.getOpenid(), score, subject);
		System.out.println("out_trade_no:" + out_trade_no + " openId:" + user.getOpenid() + " score:" + score
				+ " subject:" + subject);

		if (yzyOrders.size() == 0) {
			renderJson(new Result(ResultCode.PDF_ERROR, "未找到查询记录", null));
			return;
		}

		YzyOrderModel order = yzyOrders.get(0);

		String pdfUrl = "";
		if (order.getPdf() != null && !"".equals(order.getPdf())) {
			pdfUrl = order.getPdf();
			File pdfFile = new File("/root/apache-tomcat-7.0.85/webapps/resources_yzy/user_pdf_dir/" + pdfUrl);
			if (!pdfFile.exists()) {
				pdfUrl = create(order, subject, score, user.getNickName());
			}
		} else {
			pdfUrl = create(order, subject, score, user.getNickName());
			if ("".equals(pdfUrl)) {
				renderJson(new Result(ResultCode.PDF_ERROR, "生成失败,请稍后重试", null));
				return;
			}
		}

		renderJson(new Result(ResultCode.SUCCESS, "create success",
				new PdfResult("https://www.yujinyang.cn/resources_yzy/user_pdf_dir/" + pdfUrl)));
	}

	private String create(YzyOrderModel order, String subject, int score, String userName) {
		boolean isWen = "wenke".equals(subject);
		List<CollegeModel> colleges = DataSource.getInstance().recommendCollege(score, isWen);
		PDFUtil pdfUtil = new PDFUtil(colleges, score, isWen, userName);
		String pdfName = pdfUtil.createPDF();

		order.setPdf(pdfName);
		order.update();

		return pdfName;
	}

}
