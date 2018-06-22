package util.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

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
import bean.requestresult.CollegeChanceResult;
import datasource.ChanceUtil;
import datasource.DataSource;
import datasource.SortAndFilter;
import util.MyXMLWorkerHelper;

public class PDFUtil extends Controller {
	public static Logger logger1 = Logger.getLogger(PDFUtil.class);

	private static final String PDF_PATH = "/root/apache-tomcat-7.0.85/webapps/resources_yzy/user_pdf_dir/";
	private static final String ICON_PATH = "/root/apache-tomcat-7.0.85/webapps/resources_yzy/icon/";

	private List<CollegeModel> colleges;
	private int score;
	private boolean isWen;
	private String userName;

	private String pdfName;

	public PDFUtil(List<CollegeModel> colleges, int score, boolean isWen, String userName) {
		this.colleges = colleges;
		this.score = score;
		this.isWen = isWen;
		this.userName = userName;

		pdfName = System.currentTimeMillis() + "_" + score + ".pdf";
	}

	public String createPDF() {
		System.out.println("create PDF");
		Rectangle rect = new Rectangle(PageSize.A4);
		rect.setBackgroundColor(BaseColor.WHITE);
		Document document = new Document(rect, 0, 0, 0, 0);

		String tmpPdfName = System.currentTimeMillis() + ".pdf";
		File file = new File(PDF_PATH + tmpPdfName);
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
			createTable(table);

			document.add(table);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			document.close();
		}

		// 为PDF添加水印
		try {
			manipulatePdf(PDF_PATH + tmpPdfName, PDF_PATH + pdfName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		File pdfFile = new File(PDF_PATH + pdfName);
		File tmpFile = new File(PDF_PATH + tmpPdfName);
		if (pdfFile.exists()) {
			tmpFile.delete();
			return pdfName;
		} else if (tmpFile.exists()) {
			return tmpPdfName;
		}
		return "";
	}

	private void createTable(PdfPTable table) throws IOException {
		table.addCell(getTitleCell());
		table.addCell(getInfoCell());
		table.addCell(getRankCell());

		ChanceUtil chanceUtil = new ChanceUtil();
		List<CollegeChanceResult> chanceCollegeList = chanceUtil.getChanceList(colleges, isWen, score);

		// 对含概率的院校列表进行排序+筛选
		SortAndFilter sortAndFilter = new SortAndFilter(chanceCollegeList, "probability", null, null, isWen);
		chanceCollegeList = sortAndFilter.getResults();
		Collections.reverse(chanceCollegeList);

		for (CollegeChanceResult chanceCollege : chanceCollegeList) {
			table.addCell(getCollegeCell(chanceCollege.getCollege(), chanceCollege.getChance(), isWen));
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
		sb.append("<img src='");
		sb.append(ICON_PATH + "icon_user_normal.png' style='width:34px; height:34px;'></img>");
		sb.append("</div>");

		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			cell.addElement(element);
		}

		cell.setMinimumHeight(50);
		cell.setBorder(0);
		descTable.addCell(cell);

		sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<font style='color:black;font-size:20px;text-align:left;'>");
		sb.append("查询人：");
		sb.append(userName);
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("查询分数：</font>");
		sb.append("<font style='color:#ff4800;font-size:20px;'>");
		sb.append(score);
		sb.append("</font>");
		sb.append("&nbsp;&nbsp;&nbsp;");
		sb.append("<font style='color:#FA3C1B;font-size:20px; font-weight:bold; font-family:msyhbd;'>·</font>");
		sb.append("&nbsp;");
		sb.append("<font style='color:black;font-size:20px;'>");
		sb.append(isWen ? "文科" : "理科");
		sb.append("</font>");
		sb.append("</div>");

		cell = new PdfPCell();

		cell.setUseAscender(true);
		cell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 水平居中
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // 垂直居中

		cell.setMinimumHeight(50);

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
		sb.append("<img src='");
		sb.append(ICON_PATH + "icon_score.png' style='width:34px; height:34px;'></img>");
		sb.append("</div>");

		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			cell.addElement(element);
		}

		cell.setMinimumHeight(60);
		cell.setBorder(0);
		descTable.addCell(cell);

		sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<font style='color:#87868A;font-size:20px;'>");
		sb.append("全省排名 &nbsp;&nbsp;</font>");

		sb.append("<font style='color:#111111;font-size:20px;'>");
		sb.append(DataSource.getInstance().get2017RankByScore(score, isWen));
		sb.append("</font>");
		sb.append("<font style='color:#B8B5BD;font-size:20px;'>&nbsp;/&nbsp;2017年</font>");

		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		sb.append("<font style='color:#111111;font-size:20px;'>");
		sb.append(DataSource.getInstance().get2016RankByScore(score, isWen));
		sb.append("</font>");
		sb.append("<font style='color:#B8B5BD;font-size:20px;'>&nbsp;/&nbsp;2016年</font>");

		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");

		sb.append("<font style='color:#111111;font-size:20px;'>");
		sb.append(DataSource.getInstance().get2015RankByScore(score, isWen));
		sb.append("</font>");
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

	private PdfPCell getCollegeCell(CollegeModel college, int chance, boolean isWen) throws IOException {
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
		sb.append("<img src='");
		sb.append(college.getLogo());
		sb.append("' style='width:80px; height:80px;'></img>");
		sb.append("</div>");

		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
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

		sb.append("<img src='");
		sb.append(ICON_PATH + "icon_location.png' style='width:10px; height:15px;'></img>");

		sb.append("<font style='color:#B8B5BD;font-size:13px; height:20px;'>");
		sb.append(college.getProvince());
		sb.append("</font>");

		sb.append("<br/>");

		sb.append("<font style='color:#B8B5BD;font-size:13px; height:20px;'>历年录取分数线 &nbsp;&nbsp;</font>");

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

	public static final String IMG = ICON_PATH + "water.png";

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {

		File destFile = new File(dest);

		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(destFile));
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

}
