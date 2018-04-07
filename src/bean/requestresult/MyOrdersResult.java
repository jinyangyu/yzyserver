package bean.requestresult;

import java.util.ArrayList;
import java.util.List;

import bean.dbmodel.YzyOrderModel;
import util.TimeUtil;

public class MyOrdersResult {

	private List<MyOrder> orders;

	public MyOrdersResult(List<YzyOrderModel> yzyOrders) {
		orders = new ArrayList<MyOrdersResult.MyOrder>();
		for (int i = 0; i < yzyOrders.size(); i++) {
			orders.add(new MyOrder(yzyOrders.get(i)));
		}
	}

	public List<MyOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<MyOrder> orders) {
		this.orders = orders;
	}

	public class MyOrder {

		private String out_trade_no;
		private int score;
		private String subject;
		private int type;
		private String phone;
		private String time;

		public MyOrder(YzyOrderModel yzyOrder) {
			super();
			this.out_trade_no = yzyOrder.getOut_trade_no();
			this.score = yzyOrder.getScore();
			this.subject = yzyOrder.getSubject();
			this.type = yzyOrder.getType();
			this.phone = yzyOrder.getPhone();

			try {
				this.time = TimeUtil.getFormatTimeFromTimestamp(Long.parseLong(yzyOrder.getTime()),
						TimeUtil.FORMAT_DATE);
			} catch (NumberFormatException e) {
			}
		}

		public String getOut_trade_no() {
			return out_trade_no;
		}

		public void setOut_trade_no(String out_trade_no) {
			this.out_trade_no = out_trade_no;
		}

		public int getScore() {
			return score;
		}

		public void setScore(int score) {
			this.score = score;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		@Override
		public String toString() {
			return "MyOrder [out_trade_no=" + out_trade_no + ", score=" + score + ", subject=" + subject + ", type="
					+ type + ", phone=" + phone + ", time=" + time + "]";
		}

	}

}
