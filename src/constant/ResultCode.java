package constant;

public interface ResultCode {
	int SUCCESS = 200;
	int SMS_ERROR = 201;
	int LOGIN_ERROR = 301;
	int PDF_ERROR = 401;
	int PREPAY_ERROR = 501;
	
	// 现在不能支付
	int PREPAY_CANNOT_PAY = 502;
	// 可以预购
	int PREPAY_CAN_PREPAY = 503;
	
	int PARAMS_ERROR = 601;
	int SIGN_ERROR = 701;
	int PAY_ERROR = 801;
	int PREPAY_COUNT_0 = 901;
	int PREPAY_COUNT_NULL = 902;
}
