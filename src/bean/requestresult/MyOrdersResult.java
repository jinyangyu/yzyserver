package bean.requestresult;

import java.util.ArrayList;
import java.util.List;

import bean.dbmodel.YzyOrderModel;
import util.TimeUtil;

public class MyOrdersResult {

	private static final int RECOMMEND_FEE = 49;
	private static final int EXPERT_FEE = 6888;

	private List<MyOrder> recommend_orders;
	private List<MyOrder> expert_orders;
	private int total_count;
	private int current_count;

	public MyOrdersResult(List<YzyOrderModel> yzyOrders) {
		recommend_orders = new ArrayList<MyOrdersResult.MyOrder>();
		expert_orders = new ArrayList<MyOrdersResult.MyOrder>();
		for (int i = 0; i < yzyOrders.size(); i++) {
			if (yzyOrders.get(i).getType() == 1) {
				recommend_orders.add(new MyOrder(yzyOrders.get(i)));
			} else {
				expert_orders.add(new MyOrder(yzyOrders.get(i)));
			}
		}
	}

	public List<MyOrder> getRecommend_orders() {
		return recommend_orders;
	}

	public void setRecommend_orders(List<MyOrder> recommend_orders) {
		this.recommend_orders = recommend_orders;
	}

	public List<MyOrder> getExpert_orders() {
		return expert_orders;
	}

	public void setExpert_orders(List<MyOrder> expert_orders) {
		this.expert_orders = expert_orders;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getCurrent_count() {
		return current_count;
	}

	public void setCurrent_count(int current_count) {
		this.current_count = current_count;
	}

	public class MyOrder {
		private String title;
		private String out_trade_no;
		private int score;
		private int total_fee;
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

			this.title = score + " - " + subject + " - " + " 院校推荐查询";
			if (type == 1) {
				this.total_fee = RECOMMEND_FEE;
			} else {
				this.total_fee = EXPERT_FEE;
			}

			try {
				this.time = TimeUtil.getFormatTimeFromTimestamp(Long.parseLong(yzyOrder.getTime()),
						TimeUtil.FORMAT_DATE);
			} catch (NumberFormatException e) {
			}
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public int getTotal_fee() {
			return total_fee;
		}

		public void setTotal_fee(int total_fee) {
			this.total_fee = total_fee;
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
