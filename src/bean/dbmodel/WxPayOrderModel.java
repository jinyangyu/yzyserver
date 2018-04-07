package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class WxPayOrderModel extends Model<WxPayOrderModel> {
	public static final WxPayOrderModel dao = new WxPayOrderModel().dao();

	private String id;

	// ----- 订单类型 院校推荐 || 专家辅导
	private String payType;

	// ----- 预下单发送字段
	private String appid;
	private String mch_id;
	private String body;
	private String out_trade_no;
	private int total_fee;
	private String spbill_create_ip;
	private String notify_url;
	private String trade_type;
	private String openid;

	// ----- 预下单返回字段
	private String prepay_id;

	// ----- 支付结果返回字段
	private String bank_type;
	private String fee_type;
	private String is_subscribe;
	private String time_end;
	private String transaction_id;

	// ----- 状态字段
	private String status;

	// ----- 检查订单状态返回字段
	private String trade_state;
	private String trade_state_desc;

	private static final String id_NAME = "id";

	private static final String payType_NAME = "payType";

	private static final String appid_NAME = "appid";
	private static final String mch_id_NAME = "mch_id";
	private static final String body_NAME = "body";
	private static final String out_trade_no_NAME = "out_trade_no";
	private static final String total_fee_NAME = "total_fee";
	private static final String spbill_create_ip_NAME = "spbill_create_ip";
	private static final String notify_url_NAME = "notify_url";
	private static final String trade_type_NAME = "trade_type";
	private static final String openid_NAME = "openid";
	private static final String prepay_id_NAME = "prepay_id";

	private static final String bank_type_NAME = "bank_type";
	private static final String fee_type_NAME = "fee_type";
	private static final String is_subscribe_NAME = "is_subscribe";
	private static final String time_end_NAME = "time_end";
	private static final String transaction_id_NAME = "transaction_id";
	private static final String status_NAME = "status";

	private static final String trade_state_NAME = "trade_state";
	private static final String trade_state_desc_NAME = "trade_state_desc";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getTrade_state() {
		return getStr(trade_state_NAME);
	}

	public void setTrade_state(String trade_status) {
		set(trade_state_NAME, trade_status);
	}

	public String getTrade_state_desc() {
		return getStr(trade_state_desc_NAME);
	}

	public void setTrade_state_desc(String trade_status_desc) {
		set(trade_state_desc_NAME, trade_status_desc);
	}

	public String getPayType() {
		return getStr(payType_NAME);
	}

	public void setPayTypeRecommend() {
		set(payType_NAME, "RECOMMEND");
	}

	public void setPayTypeExpert() {
		set(payType_NAME, "EXPERT");
	}

	public String getAppid() {
		return getStr(appid_NAME);
	}

	public void setAppid(String appid) {
		set(appid_NAME, appid);
	}

	public String getMch_id() {
		return getStr(mch_id_NAME);
	}

	public void setMch_id(String mch_id) {
		set(mch_id_NAME, mch_id);
	}

	public String getBody() {
		return getStr(body_NAME);
	}

	public void setBody(String body) {
		set(body_NAME, body);
	}

	public String getOut_trade_no() {
		return getStr(out_trade_no_NAME);
	}

	public void setOut_trade_no(String out_trade_no) {
		set(out_trade_no_NAME, out_trade_no);
	}

	public int getTotal_fee() {
		return getInt(total_fee_NAME);
	}

	public void setTotal_fee(int total_fee) {
		set(total_fee_NAME, total_fee);
	}

	public String getSpbill_create_ip() {
		return getStr(spbill_create_ip_NAME);
	}

	public void setSpbill_create_ip(String spbill_create_ip) {
		set(spbill_create_ip_NAME, spbill_create_ip);
	}

	public String getNotify_url() {
		return getStr(notify_url_NAME);
	}

	public void setNotify_url(String notify_url) {
		set(notify_url_NAME, notify_url);
	}

	public String getTrade_type() {
		return getStr(trade_type_NAME);
	}

	public void setTrade_type(String trade_type) {
		set(trade_type_NAME, trade_type);
	}

	public String getOpenid() {
		return getStr(openid_NAME);
	}

	public void setOpenid(String openid) {
		set(openid_NAME, openid);
	}

	public String getPrepay_id() {
		return getStr(prepay_id_NAME);
	}

	public void setPrepay_id(String prepay_id) {
		set(prepay_id_NAME, prepay_id);
	}

	public String getBank_type() {
		return getStr(bank_type_NAME);
	}

	public void setBank_type(String bank_type) {
		set(bank_type_NAME, bank_type);
	}

	public String getFee_type() {
		return getStr(fee_type_NAME);
	}

	public void setFee_type(String fee_type) {
		set(fee_type_NAME, fee_type);
	}

	public String getIs_subscribe() {
		return getStr(is_subscribe_NAME);
	}

	public void setIs_subscribe(String is_subscribe) {
		set(is_subscribe_NAME, is_subscribe);
	}

	public String getTime_end() {
		return getStr(time_end_NAME);
	}

	public void setTime_end(String time_end) {
		set(time_end_NAME, time_end);
	}

	public String getTransaction_id() {
		return getStr(transaction_id_NAME);
	}

	public void setTransaction_id(String transaction_id) {
		set(transaction_id_NAME, transaction_id);
	}

	public String getStatus() {
		return getStr(status_NAME);
	}

	public void setStatusPrepaySuccess() {
		set(status_NAME, "PREPAY_SUCCESS");
	}

	public void setStatusPrepayFailed() {
		set(status_NAME, "PREPAY_Failed");
	}

	public void setStatusPaySuccess() {
		set(status_NAME, "PAY_SUCCESS");
	}

	public void setStatusPayFailed() {
		set(status_NAME, "PAY_Failed");
	}

	@Override
	public String toString() {
		return "WxPayOrderModel [id=" + id + ", payType=" + payType + ", appid=" + appid + ", mch_id=" + mch_id
				+ ", body=" + body + ", out_trade_no=" + out_trade_no + ", total_fee=" + total_fee
				+ ", spbill_create_ip=" + spbill_create_ip + ", notify_url=" + notify_url + ", trade_type=" + trade_type
				+ ", openid=" + openid + ", prepay_id=" + prepay_id + ", bank_type=" + bank_type + ", fee_type="
				+ fee_type + ", is_subscribe=" + is_subscribe + ", time_end=" + time_end + ", transaction_id="
				+ transaction_id + ", status=" + status + ", trade_state=" + trade_state + ", trade_state_desc="
				+ trade_state_desc + "]";
	}

	
}
