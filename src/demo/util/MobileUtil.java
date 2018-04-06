package demo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 三大运营商号码均可验证(不含卫星通信1349)
 */
public class MobileUtil {
	/*
	 * <br> 2018年3月已知 中国电信号段 133,149,153,173,177,180,181,189,199 中国联通号段
	 * 130,131,132,145,155,156,166,175,176,185,186 中国移动号段
	 * 134(0-8),135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,
	 * 188,198 其他号段 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。 虚拟运营商 电信：1700,1701,1702
	 * 移动：1703,1705,1706 联通：1704,1707,1708,1709,171 卫星通信：148(移动) 1349
	 */
	public static void main(String[] args) {
		System.out.println("是正确格式的手机号:" + isMobile("18210512166"));
	}
	
	private static final String PHONE_NUMBER_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

	public static boolean isMobile(String str) {
		Pattern pattern = null;
		Matcher matcher = null;
		boolean result = false;
		if (str != null && !"".equals(str)) {
			pattern = Pattern.compile(PHONE_NUMBER_REG);
			matcher = pattern.matcher(str);
			result = matcher.matches();
		}
		return result;
	}
}
