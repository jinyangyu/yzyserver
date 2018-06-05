package util;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class FontUtil {
	private static BaseFont baseFont = null;
	private static Font weiRuanYaHeiFont = null;

	public static BaseFont getBaseFont() {
		if (baseFont != null) {
			return baseFont;
		}
		try {
			baseFont = BaseFont.createFont("font/msyhbd.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baseFont;
	}

	public static Font getWeiRuanYaHeiFont() {
		if (weiRuanYaHeiFont != null) {
			return weiRuanYaHeiFont;
		}

		weiRuanYaHeiFont = new Font(getBaseFont(), 20, Font.NORMAL);
		weiRuanYaHeiFont.setFamily("微软雅黑");

		return weiRuanYaHeiFont;
	}
	
	public static Font getITextSong() {
		BaseFont bfChinese = null;  
        try {  
            bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
            //也可以使用Windows系统字体(TrueType)  
            //bfChinese = BaseFont.createFont("C:/WINDOWS/Fonts/SIMYOU.TTF", BaseFont.IDENTITY_H,BaseFont.NOT_EMBEDDED);   
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        Font FontChinese = new Font(bfChinese, 20, Font.NORMAL);  
        return FontChinese;
	}
}
