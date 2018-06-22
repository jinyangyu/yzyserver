package util.pdf;

import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;

import util.MyXMLWorkerHelper;

public class HeaderFooter extends PdfPageEventHelper {
	protected ElementList header;
	protected ElementList footer;

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		try {
			ColumnText ct = new ColumnText(writer.getDirectContent());
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
								"<img src='/root/apache-tomcat-7.0.85/webapps/resources_yzy/icon/logo.png' style='width:60px; height:30px;'></img>" + 
							"</td>" + 
							"<td align=\"center\" >" +
								"<font style='color:black;font-size:14px;'>" + 
									+ currentPage + "</font>" + 
							"</td>" + 
							"<td align=\"right\" style='width:220px; padding-right: 40px'> " +
								"<font style='color:black;font-size:14px;'>河南高考志愿填报助手</font>" + 
							"</td>" + 
						"</tr>" + 
					"</table>");

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
