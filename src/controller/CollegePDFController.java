package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.jfinal.core.Controller;
import demo.bean.CollegeModel;
import demo.bean.UserInfoModel;
import demo.result.PdfResult;
import demo.result.Result;
import demo.util.FontUtil;

public class CollegePDFController extends Controller {
	public static Logger logger1 = Logger.getLogger(CollegePDFController.class);
	private static final String PDF_OUTPUT_PATH = "user_pdf_dir";

	private String userId = "YUJINYANG_001";
	private long requestTime;

	public void createPDF() {

		String clientSession = getPara("clientSession");

		logger1.info("clientSession:" + clientSession);

		List<UserInfoModel> users = UserInfoModel.dao.find("select * from userinfo where clientSession = ?",
				clientSession);
		if (users == null || users.isEmpty()) {
			renderJson(new Result(201, "登陆信息失效", null));
			return;
		}
		
		UserInfoModel user = users.get(0);
		
		if(user.getPdfPath() != null && !"".equals(user.getPdfPath())) {
			renderJson(new Result(203, "之前已经生成过pdf", new PdfResult(user.getPdfPath())));
		}

		requestTime = System.currentTimeMillis();

		List<CollegeModel> colleges = CollegeModel.dao.find("select * from college where id < 4");

		for (CollegeModel model : colleges) {
			String[] names = model._getAttrNames();
			for (String name : names) {
				System.out.println(name + " " + model.getStr(name));
			}
		}

		File pdfFile = null;
		try {
			pdfFile = getPdfOutputFile();

			File tempPdfFile = File.createTempFile("tempPdfFile", ".pdf");
			createPdfFile(tempPdfFile);
			waterMark(tempPdfFile.getPath(), "image/yuzhiyuan.png", pdfFile.getPath(), "豫志愿-正版", 16);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger1.error("create pdf " + getPdfName() + " failed", e);
		} catch (DocumentException e) {
			e.printStackTrace();
			logger1.error("create pdf " + getPdfName() + " failed", e);
		} catch (IOException e) {
			e.printStackTrace();
			logger1.error("create pdf " + getPdfName() + " failed", e);
		}
		if (pdfFile != null) {
			renderJson(new Result(200, "create success", new PdfResult(pdfFile.getPath())));
		} else {
			renderJson(new Result(202, "生成PDF失败", null));
		}
	}

	private void createPdfFile(File pdfFile) throws FileNotFoundException, DocumentException {
		// 新建Document对象
		Document document = new Document(PageSize.A4, 20, 20, 40, 40);

		long start = System.currentTimeMillis();

		// 建立一个书写器（Writer） 与 Document 对象关联，通过书写器（Writer）可以将文档写入到磁盘中
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		// 打开文档
		document.open();

		// 写入属性信息
		setPdfFileProperties(document);

		// 创建一个段落内容
		Paragraph paragraph;
		if (FontUtil.getWeiRuanYaHeiFont() == null) {
			paragraph = new Paragraph("豫志愿");
		} else {
			paragraph = new Paragraph("豫志愿", new Font(FontUtil.getBaseFont(), 40));
		}
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		// 添加段落到文档中
		document.add(paragraph);

		Paragraph paragraph_content = new Paragraph(
				"\n\n\t北京大学创办于1898年，初名京师大学堂，是中国第一所国立综合性大学，也是当时中国最高教育行政机关。辛亥革命后，于1912年改为现名。作为新文化运动的中心和“五四”运动的策源地，作为中国最早传播马克思主义和民主科学思想的发祥地，作为中国共产党最早的活动基地，北京大学为民族的振兴和解放、国家的建设和发展、社会的文明和进步做出了不可替代的贡献，在中国走向现代化的进程中起到了重要的先锋作用。爱国、进步、民主、科学的传统精神和勤奋、严谨、求实、创新的学风在这里生生不息、代代相传。1917年，著名教育家蔡元培出任北京大学校长，他“循思想自由原则，取兼容并包主义”，对北京大学进行了卓有成效的改革，促进了思想解放和学术繁荣。陈独秀、李大钊、毛泽东以及鲁迅、胡适等一批杰出人才都曾在北京大学任职或任教。1937年卢沟桥事变后，北京大学与清华大学、南开大学南迁长沙，共同组成长沙临时大学。不久，临时大学又迁到昆明，改称国立西南联合大学。抗日战争胜利后，北京大学于1946年10月在北平复学。中华人民共和国成立后，全国高校于1952年进行院系调整，北京大学成为一所以文理基础教学和研究为主的综合性大学，为国家培养了大批人才。据不完全统计，北京大学的校友和教师有400多位两院院士，中国人文社科界有影响的人士相当多也出自北京大学。改革开放以来，北京大学进入了一个前所未有的大发展、大建设的新时期，并成为国家“211工程”重点建设的两所大学之一。1998年5月4日，北京大学百年校庆之际，国家主席江泽民在庆祝北京大学建校一百周年大会上发表讲话，发出了“为了实现现代化，我国要有若干所具有世界先进水平的一流大学”的号召。在国家的支持下，北京大学适时启动“创建世界一流大学计划”，从此，北京大学的历史翻开了新的一页。2000年4月3日，北京大学与原北京医科大学合并，组建了新的北京大学。原北京医科大学的前身是国立北京医学专门学校，创建于1912年10月26日。20世纪三、四十年代，学校一度名为北平大学医学院，并于1946年7月并入北京大学。1952年在全国高校院系调整中，北京大学医学院脱离北京大学，独立为北京医学院。1985年更名为北京医科大学，1996年成为国家首批“211工程”重点支持的医科大学。两校合并进一步拓宽了北京大学的学科结构，为促进医学与人文社会科学及理科的结合，改革医学教育奠定了基础。近年来，在“211工程”和“985工程”的支持下，北京大学进入了一个新的历史发展阶段，在学科建设、人才培养、师资队伍建设、教学科研等各方面都取得了显著成绩，为将北大建设成为世界一流大学奠定了坚实的基础。今天的北京大学已经成为国家培养高素质、创造性人才的摇篮、科学研究的前沿和知识创新的重要基地和国际交流的重要桥梁和窗口。现任校党委书记朱善璐教授、校长林建华教授.",
				new Font(FontUtil.getBaseFont(), 10));
		paragraph.setAlignment(Paragraph.ALIGN_MIDDLE);
		document.add(paragraph_content);

		// 关闭文档
		document.close();

		logger1.info("create pdf document " + getPdfName() + " cost:" + (System.currentTimeMillis() - start) + " ms");

	}

	/**
	 * 设置 PDF 文件属性
	 */
	private void setPdfFileProperties(Document document) {
		// 设置标题
		document.addTitle("豫志愿为您竭诚服务");
		// 作者
		document.addAuthor("豫志愿");
		// 主题
		document.addSubject("高考志愿推荐");
		// 关键字
		document.addKeywords("豫志愿,高考,高校,推荐");
		// 创建时间
		document.addCreationDate();
		// 应用程序
		document.addCreator("daoxing.com.cn");
	}

	private File getPdfOutputFile() throws FileNotFoundException {
		File pdfDir = new File(PDF_OUTPUT_PATH);
		if (!pdfDir.exists()) {
			pdfDir.mkdir();
		}
		String pdfName = getPdfName();
		File pdfFile = new File(pdfDir, pdfName);
		if (pdfFile.exists()) {
			pdfFile.delete();
		}
		logger1.info("create pdf file success in:" + pdfFile.getAbsolutePath());
		return pdfFile;
	}

	private String getPdfName() {
		return userId + "_" + requestTime + ".pdf";
	}

	public static void waterMark(String inputFile, String imageFile, String outputFile, String waterMarkName,
			int permission) {
		try {
			PdfReader reader = new PdfReader(inputFile);
			PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(outputFile));

			int total = reader.getNumberOfPages() + 1;
			Image image = Image.getInstance(imageFile);

			// 图片位置
			image.setAbsolutePosition(50, 400);
			image.setRotation(-20);
			image.setRotation(-20);// 旋转 弧度
			image.scalePercent(50);// 依照比例缩放

			PdfContentByte under;
			int j = waterMarkName.length();
			char c = 0;
			int rise = 0;
			for (int i = 1; i < total; i++) {
				rise = 400;
				under = stamper.getUnderContent(i);

				PdfGState gs = new PdfGState();
				gs.setFillOpacity(0.3f);// 设置透明度为0.2
				under.setGState(gs);

				under.beginText();
				under.setFontAndSize(FontUtil.getBaseFont(), 30);

				if (j >= 15) {
					under.setTextMatrix(200, 120);
					for (int k = 0; k < j; k++) {
						under.setTextRise(rise);
						c = waterMarkName.charAt(k);
						under.showText(c + "");
					}
				} else {
					under.setTextMatrix(240, 100);
					for (int k = 0; k < j; k++) {
						under.setTextRise(rise);
						c = waterMarkName.charAt(k);
						under.showText(c + "");
						rise -= 20;

					}
				}

				// 添加水印文字
				under.endText();

				// 添加水印图片
				under.addImage(image);
				// 画个圈
				under.ellipse(250, 450, 350, 550);
				under.setLineWidth(1f);
				under.stroke();
			}
			stamper.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
