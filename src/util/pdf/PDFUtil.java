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

import util.MyXMLWorkerHelper;

public class PDFUtil extends Controller {
	public static Logger logger1 = Logger.getLogger(PDFUtil.class);

	public void createPDF() {

		System.out.println("create PDF");
		Rectangle rect = new Rectangle(PageSize.A4);
		rect.setBackgroundColor(new BaseColor(234, 234, 234));
		Document document = new Document(rect, 0, 0, 0, 0);

		File file = new File("demo.pdf");
		try {
			// step 2
			PdfWriter.getInstance(document, new FileOutputStream(file));
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

	}

	private void createTable(PdfPTable table) throws IOException {
			table.addCell(getTitleCell());
			table.addCell(getInfoCell());
			table.addCell(getRankCell());
	}

	private PdfPCell getTitleCell() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<div><p align=\"center\">");
		sb.append("<b>&nbsp;<font color=\"#000\">河南高考志愿填报助手</font></b>");
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
		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<font color=\"#000\">");
		sb.append("查询人：于金洋");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("查询分数：</font>");
		sb.append("<font color=\"#ff4800\">475</font>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("<font>理科</font>");
		sb.append("</div>");

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderColorBottom(new BaseColor(219, 212, 212));
		cell.setPaddingTop(10);
		cell.setPaddingBottom(20);
		cell.setPaddingLeft(20);
		cell.setPaddingRight(20);
		cell.setBorderWidthBottom(1);
		

		// ElementList list = XMLWorkerHelper.parseToElementList(sb.toString(), null);
		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			cell.addElement(element);
		}
		return cell;
	}
	
	private PdfPCell getRankCell() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("<div>");
		sb.append("<font color=\"#87868A\"");
		sb.append("全省排名：</font>");
		sb.append("<font color=\"#111111\">12512</font>");
		sb.append("<font color=\"#B8B5BD\">&nbsp;/&nbsp;2017年</font>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("<font color=\"#111111\">13541</font>");
		sb.append("<font color=\"#B8B5BD\">&nbsp;/&nbsp;2016年</font>");
		sb.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
		sb.append("<font color=\"#111111\">11319</font>");
		sb.append("<font color=\"#B8B5BD\">&nbsp;/&nbsp;2015年</font>");
		sb.append("</div>");

		PdfPCell cell = new PdfPCell();
		cell.setBorder(0);
		cell.setBackgroundColor(BaseColor.WHITE);
		cell.setBorderColorBottom(new BaseColor(250, 248, 247));
		cell.setPaddingTop(10);
		cell.setPaddingBottom(35);
		cell.setPaddingLeft(20);
		cell.setPaddingRight(20);
		cell.setBorderWidthBottom(10);

		// ElementList list = XMLWorkerHelper.parseToElementList(sb.toString(), null);
		ElementList list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
		for (Element element : list) {
			cell.addElement(element);
		}
		return cell;
	}

	public static final String IMG = "shuiyin.jpeg";

	public void manipulatePdf(String src, String dest) throws IOException, DocumentException {
		PdfReader reader = new PdfReader(src);
		PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
		Image image = Image.getInstance(IMG);
		PdfImage stream = new PdfImage(image, "", null);
		stream.put(new PdfName("ITXT_SpecialId"), new PdfName("123456789"));
		PdfIndirectObject ref = stamper.getWriter().addToBody(stream);

		image.setDirectReference(ref.getIndirectReference());
		image.setAbsolutePosition(200, 650);
		image.setRotation(-20.0f);
		image.scalePercent(20);// 依照比例缩放

		int total = reader.getNumberOfPages() + 1;

		for (int i = 1; i < total; i++) {
			PdfContentByte over = stamper.getOverContent(i);
			PdfGState gs = new PdfGState();
			gs.setFillOpacity(0.3f);
			over.setGState(gs);

			over.addImage(image);
		}
		stamper.close();
		reader.close();
	}

	public static void main(String[] args) {
		PDFUtil util = new PDFUtil();
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
