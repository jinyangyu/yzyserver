package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class PrepayCountModel extends Model<PrepayCountModel> {
	public static final PrepayCountModel dao = new PrepayCountModel().dao();

	private String id;
	private String openid;
	private String out_trade_no;
	private int current_count;
	private int total_count;
	private String time;

	private static final String id_NAME = "id";
	private static final String openid_NAME = "openid";
	private static final String out_trade_no_NAME = "out_trade_no";
	private static final String time_NAME = "time";
	private static final String current_count_NAME = "current_count";
	private static final String total_count_NAME = "total_count";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getOpenid() {
		return getStr(openid_NAME);
	}

	public void setOpenid(String openid) {
		set(openid_NAME, openid);
	}

	public String getTime() {
		return getStr(time_NAME);
	}

	public void setTime(String time) {
		set(time_NAME, time);
	}

	public String getOut_trade_no() {
		return getStr(out_trade_no_NAME);
	}

	public void setOut_trade_no(String out_trade_no) {
		set(out_trade_no_NAME, out_trade_no);
	}

	public int getCurrentCount() {
		return getInt(current_count_NAME);
	}

	public void setCurrentCount(int count) {
		set(current_count_NAME, count);
	}
	
	public int getTotalCount() {
		return getInt(total_count_NAME);
	}

	public void setTotalCount(int count) {
		set(total_count_NAME, count);
	}
	
}
