package util.pdf;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import util.MyXMLWorkerHelper;

public class HeaderFooter extends PdfPageEventHelper {

	public static final String DEST = "results/events/html_header_footer.pdf";
	public static final String HEADER = "<table width=\"100%\" border=\"0\"><tr><td>Header</td><td align=\"right\">Some title</td></tr></table>";
	public static final String FOOTER = "<table width=\"100%\" border=\"0\">" + "<tr>"
			+ "<td><img src='./logo.png' style='width:80px; height:40px;'></img></td>" + "<td align=\"right\"></td>"
			+ "</tr>" + "</table>";

	protected ElementList header;
	protected ElementList footer;

	public HeaderFooter() throws IOException {
		header = XMLWorkerHelper.parseToElementList(HEADER, null);
		footer = XMLWorkerHelper.parseToElementList(FOOTER, null);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			ColumnText ct = new ColumnText(writer.getDirectContent());
			// ct.setSimpleColumn(new Rectangle(36, 832, 559, 810));
			// for (Element e : header) {
			// ct.addElement(e);
			// }
			// ct.go();
			int totalPage = document.getPageNumber();
			int currentPage = writer.getCurrentPageNumber();
			
			ct.setSimpleColumn(new Rectangle(0, 0, 593, 33));
			StringBuilder sb = new StringBuilder();
			
			sb.append(
					"<table width=\"100%\" border=\"0\">" +
						"<tr style='height:40; background-color:#FAF8F7'>" +
							"<td style='height:10px;' colspan=\"3\">" + 
							"</td>" + 
						"</tr>" + 
						"<tr border=\"0\">" + 
							"<td align=\"left\" style='width:220px; padding-left:40px'>" + 
								"<img src='./logo.png' style='width:60px; height:30px;'></img>" + 
							"</td>" + 
							"<td align=\"center\" >" +
								"<font style='color:black;font-size:14px; font-weight:bold; font-family:STSong-Light;'>" + 
									+ currentPage + "</font>" + 
							"</td>" + 
							"<td align=\"right\" style='width:220px; padding-right: 40px'> " +
								"<font style='color:black;font-size:14px; font-weight:bold; font-family:STSong-Light;'>河南高考志愿填报助手</font>" + 
							"</td>" + 
						"</tr>" + 
					"</table>");
			
			
//			sb.append("<div style='background-color:red; height:30px;'>");
//			
//			sb.append("<img src='./logo.png' style='width:60px; height:30px;'></img>");
//			
//			sb.append("<font style='color:black;font-size:20px;text-align:left;'>");
//			sb.append("查询人：于金洋");
//			sb.append("</font>");
//			
//			sb.append("</div>");

			ElementList list;
			try {
				list = MyXMLWorkerHelper.parseToElementList(sb.toString(), null);
				for (Element element : list) {
					ct.addElement(element);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			ct.go();
		} catch (DocumentException de) {
			throw new ExceptionConverter(de);
		}
	}
}
