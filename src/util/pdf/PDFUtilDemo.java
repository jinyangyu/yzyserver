package util.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfImage;
import com.itextpdf.text.pdf.PdfIndirectObject;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.jfinal.core.Controller;

import bean.dbmodel.CollegeModel;
import bean.dbmodel.CollegeModel2;
import bean.requestresult.CollegeChanceResult;
import util.MyXMLWorkerHelper;

public class PDFUtilDemo extends Controller {
	public static Logger logger1 = Logger.getLogger(PDFUtilDemo.class);

	public String createPDF() {

		System.out.println("create PDF");
		Rectangle rect = new Rectangle(PageSize.A4);
		rect.setBackgroundColor(BaseColor.WHITE);
		Document document = new Document(rect, 0, 0, 0, 0);

		File file = new File("demo.pdf");
		try {
			// step 2
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
			writer.setPageEvent(new HeaderFooter());
			// step 3
			document.open();

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

			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100.0f);

			// step 4
			System.out.println("createTable");
			createTable(table);

			document.add(table);

			System.out.println("finish");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// 第五步，关闭Document
			document.close();
		}
		
		return "";

	}

	private void createTable(PdfPTable table) throws IOException {
		table.addCell(getTitleCell());
		table.addCell(getInfoCell());
		table.addCell(getRankCell());

		CollegeModel2 college = new CollegeModel2();
		college.setLogo("http://img.gaokaoq.com/college2/5948cbfc5b632.jpg");
		college.setName("复旦大学");
		college.setIs211(true);
		college.setIs985(true);
		college.setCollege_id("12311");
		college.setProvince("上海");

		college.setWen_2015("612");
		college.setWen_2016("625");
		college.setWen_2017("640");

		college.setLi_2015("669");
		college.setLi_2016("676");
		college.setLi_2017("656");

		for (int i = 0; i < 10; i++) {
			table.addCell(getCollegeCell(college, 98, true));
		}
	}

	private PdfPCell getTitleCell() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<div><p align=\"center\">");
		sb.append(
				"<b>&nbsp;<font style='color:black;font-size:26px; font-weight:bold; font-family:STSong-Light;'>河南高考志愿填报助手</font></b>");
		sb.append("</p>\n</div>");

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderColorBottom(new BaseColor(250, 248, 247));
		cell.setPaddingTop(20);
		cell.setBorderWidthBottom(10);
		cell.setPaddingBottom(35);

		// ElementList list = XMLWorkerHelper.parseToElementList(sb.toString(), null);
		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);

		for (Element element : list) {
			cell.addElement(element);
		}
		return cell;
	}

	private PdfPCell getInfoCell() throws IOException {

		PdfPTable descTable = new PdfPTable(2);
		descTable.setWidthPercentage(100.0f);

		try {
			descTable.setWidths(new float[] { 5.2f, 94.8f });
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		PdfPCell cell = new PdfPCell();
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:34px; height:50px; padding-top:13px;'>");
		sb.append("<img src='./icon_user_normal.png' style='width:34px; height:34px;'></img>");
		sb.append("</div>");

		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			System.out.println("add element");
			cell.addElement(element);
		}

		cell.setMinimumHeight(50);
		cell.setBorder(0);
		descTable.addCell(cell);

		sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<font style='color:black;font-size:20px;text-align:left;'>");
		sb.append("查询人：于金洋");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("查询分数：</font>");
		sb.append("<font style='color:#ff4800;font-size:20px;'>475</font>");
		sb.append("&nbsp;&nbsp;&nbsp;");
		sb.append("<font style='color:#FA3C1B;font-size:20px; font-weight:bold; font-family:msyhbd;'>·</font>");
		sb.append("&nbsp;");
		sb.append("<font style='color:black;font-size:20px;'>理科</font>");
		sb.append("</div>");

		cell = new PdfPCell();

		cell.setUseAscender(true);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 水平居中
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 垂直居中

		cell.setMinimumHeight(50);

		list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			System.out.println("add element");
			cell.addElement(element);
		}

		cell.setBorder(0);
		descTable.addCell(cell);

		PdfPCell tableCell = new PdfPCell(descTable);
		tableCell.setPaddingLeft(20);
		tableCell.setBackgroundColor(BaseColor.WHITE);

		tableCell.setBorder(0);
		tableCell.setBorderColorBottom(new BaseColor(219, 212, 212));
		tableCell.setBorderWidthBottom(1);

		return tableCell;
	}

	private PdfPCell getRankCell() throws IOException {

		PdfPTable descTable = new PdfPTable(2);
		descTable.setWidthPercentage(100.0f);

		try {
			descTable.setWidths(new float[] { 5.2f, 94.8f });
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		PdfPCell cell = new PdfPCell();
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:34px; height:50px; padding-top:13px;'>");
		sb.append("<img src='./icon_score.png' style='width:34px; height:34px;'></img>");
		sb.append("</div>");

		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			System.out.println("add element");
			cell.addElement(element);
		}

		cell.setMinimumHeight(60);
		cell.setBorder(0);
		descTable.addCell(cell);

		sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<font style='color:#87868A;font-size:20px;'>");
		sb.append("全省排名 &nbsp;&nbsp;</font>");

		sb.append("<font style='color:#111111;font-size:20px;'>12512</font>");
		sb.append("<font style='color:#B8B5BD;font-size:20px;'>&nbsp;/&nbsp;2017年</font>");

		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		sb.append("<font style='color:#111111;font-size:20px;'>13541</font>");
		sb.append("<font style='color:#B8B5BD;font-size:20px;'>&nbsp;/&nbsp;2016年</font>");

		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		sb.append("<font style='color:#111111;font-size:20px;'>11319</font>");
		sb.append("<font style='color:#B8B5BD;font-size:20px;'>&nbsp;/&nbsp;2015年</font>");

		sb.append("</div>");

		cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 水平居中
		cell.setVerticalAlignment(Element.ALIGN_TOP); // 垂直居中

		cell.setMinimumHeight(60);
		cell.setPaddingTop(18);

		list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			cell.addElement(element);
		}

		cell.setBorder(0);
		descTable.addCell(cell);

		PdfPCell tableCell = new PdfPCell(descTable);
		tableCell.setPaddingLeft(20);
		tableCell.setBackgroundColor(BaseColor.WHITE);

		tableCell.setBorder(0);
		tableCell.setBorderColorBottom(new BaseColor(250, 248, 247));
		tableCell.setBorderWidthBottom(10);

		return tableCell;
	}

	private static final int COLLEGE_CELL_HEIGHT = 80;

	private PdfPCell getCollegeCell(CollegeModel2 college, int chance, boolean isWen) throws IOException {
		PdfPTable descTable = new PdfPTable(3);
		descTable.setWidthPercentage(100.0f);

		try {
			descTable.setWidths(new float[] { 14f, 71f, 16f });
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		PdfPCell cell = new PdfPCell();
		StringBuilder sb = new StringBuilder();
		sb.append("<div style='width:100px; height:120px; padding-top:20px;'>");
		sb.append(
				"<img src='http://img.gaokaoq.com/college2/5948cbfc5b632.jpg' style='width:80px; height:80px;'></img>");
		sb.append("</div>");

		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			System.out.println("add element");
			cell.addElement(element);
		}

		cell.setMinimumHeight(COLLEGE_CELL_HEIGHT);
		cell.setBorder(0);
		descTable.addCell(cell);

		sb = new StringBuilder();
		sb.append("<div style='line-height:22px;'>");
		sb.append("<font style='color:black;font-size:20px;'>");
		sb.append(college.getName());
		sb.append("</font>");

		String type = "";
		if (college.isIs985()) {
			type = "985 &nbsp;";
		}
		if (college.isIs211()) {
			type += "211";
		}

		if (!"".equals(type)) {
			sb.append("<font style='color:#B8B5BD;font-size:13px;'>&nbsp;/&nbsp;</font>");
			sb.append("<font style='color:#F4A82C;font-size:13px;'>");
			sb.append(type);
			sb.append("</font>");
		}

		sb.append("<br/>");

		sb.append("<font style='color:#B8B5BD;font-size:13px;'>学校代码：");
		sb.append(college.getCollege_id());
		sb.append("</font>&nbsp;&nbsp;");

		sb.append("<img src='./icon_location.png' style='width:10px; height:15px;'></img>");

		sb.append("<font style='color:#B8B5BD;font-size:13px; height:20px;'>");
		sb.append(college.getProvince());
		sb.append("</font>");

		sb.append("<br/>");

		sb.append("<font style='color:#B8B5BD;font-size:13px; height:20px;'>历年录取分数线 &nbsp;&nbsp;</font>");

		String line_2017 = "";
		if (isWen) {
			if (!"".equals(college.getWen_2017())) {
				sb.append("<font style='color:#ff4800;font-size:13px;'>");
				sb.append(college.getWen_2017());
				sb.append("</font>");
				sb.append("<font style='color:#B8B5BD;font-size:13px;'>/2017年</font>");
				sb.append("&nbsp;&nbsp;");
			}

			if (!"".equals(college.getWen_2016())) {
				sb.append("<font style='color:#ff4800;font-size:13px;'>");
				sb.append(college.getWen_2016());
				sb.append("</font>");
				sb.append("<font style='color:#B8B5BD;font-size:13px;'>/2016年</font>");
				sb.append("&nbsp;&nbsp;");
			}

			if (!"".equals(college.getWen_2015())) {
				sb.append("<font style='color:#ff4800;font-size:13px;'>");
				sb.append(college.getWen_2015());
				sb.append("</font>");
				sb.append("<font style='color:#B8B5BD;font-size:13px;'>/2015年</font>");
				sb.append("&nbsp;&nbsp;");
			}
		} else {
			if (!"".equals(college.getLi_2017())) {
				sb.append("<font style='color:#ff4800;font-size:13px;'>");
				sb.append(college.getLi_2017());
				sb.append("</font>");
				sb.append("<font style='color:#B8B5BD;font-size:13px;'>/2017年</font>");
				sb.append("&nbsp;&nbsp;");
			}

			if (!"".equals(college.getLi_2016())) {
				sb.append("<font style='color:#ff4800;font-size:13px;'>");
				sb.append(college.getLi_2016());
				sb.append("</font>");
				sb.append("<font style='color:#B8B5BD;font-size:13px;'>/2016年</font>");
				sb.append("&nbsp;&nbsp;");
			}

			if (!"".equals(college.getLi_2015())) {
				sb.append("<font style='color:#ff4800;font-size:13px;'>");
				sb.append(college.getLi_2015());
				sb.append("</font>");
				sb.append("<font style='color:#B8B5BD;font-size:13px;'>/2015年</font>");
				sb.append("&nbsp;&nbsp;");
			}
		}

		sb.append("</div>");

		cell = new PdfPCell();
		cell.setUseAscender(true);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 水平居中
		cell.setVerticalAlignment(Element.ALIGN_TOP); // 垂直居中

		cell.setMinimumHeight(COLLEGE_CELL_HEIGHT);
		cell.setPaddingTop(24);
		cell.setPaddingLeft(0);

		list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			cell.addElement(element);
		}

		cell.setBorder(0);
		descTable.addCell(cell);

		cell = new PdfPCell();
		cell.setPaddingTop(39);

		sb = new StringBuilder();

		sb.append("<div style='line-height:22px;'>");
		sb.append("<font style='color:#FB4416;font-size:50px;font-weight:bold; font-family:STSong-Light;'>");
		sb.append(chance);
		sb.append("</font>");

		sb.append("<font style='color:#FB4416;font-size:30px;'>%</font>");

		sb.append("<br/>");

		sb.append("<font style='color:#B8B5BD;font-size:13px;'> &nbsp; 录取概率</font>");

		sb.append("</div>");

		list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			System.out.println("add element");
			cell.addElement(element);
		}

		cell.setMinimumHeight(COLLEGE_CELL_HEIGHT);
		cell.setBorder(0);
		descTable.addCell(cell);

		PdfPCell tableCell = new PdfPCell(descTable);
		tableCell.setBackgroundColor(BaseColor.WHITE);
		tableCell.setPaddingLeft(20);
		tableCell.setBorder(0);

		tableCell.setBorderColorBottom(new BaseColor(219, 212, 212));
		tableCell.setBorderWidthBottom(1);

		return tableCell;
	}

	public static final String IMG = "./water2.png";

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		Image image = Image.getInstance(IMG);
		PdfImage stream = new PdfImage(image, "", null);
		stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
		PdfIndirectObject ref = stamper.getWriter().addToBody(stream);

		image.setDirectReference(ref.getIndirectReference());
		image.setAbsolutePosition(0, 0);
		image.setRotation(0);
		image.scalePercent(100);// 依照比例缩放

		int total = reader.getNumberOfPages() + 1;

		for (int i = 1; i < total; i++) {
			PdfContentByte over = stamper.getOverContent(i);
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.2f);
			over.setGState(gs);

			over.addImage(image);
		}
		stamper.close();
		reader.close();
	}

	public static void main(String[] args) {
		PDFUtilDemo util = new PDFUtilDemo();
		util.createPDF();

		// 为PDF添加水印
		try {
			util.manipulatePdf("demo.pdf", "water.pdf");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

	}

}
