package demo.result;

public class PrepayResult {
	
	public String timeStamp;
	public String nonceStr;
	public String package_str;
	public String signType;
	public String paySign;
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getPackage_str() {
		return package_str;
	}
	public void setPackage_str(String package_str) {
		this.package_str = package_str;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	
	
}
