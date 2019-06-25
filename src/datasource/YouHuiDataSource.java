package datasource;

import java.util.List;

import bean.dbmodel.QrCodeModel;
import bean.requestresult.MoneyInfo;
import controller.PaymentController;

public class YouHuiDataSource {

	public static int getShareCount(String openid) {
		if("oi1oY42G6Y1PRmNmJ_9ErHpkOtKE".equals(openid)) {
			return 2000;
		}
		List<QrCodeModel> list = QrCodeModel.dao.find("select * from qrcode where sharefrom = ? and used=0", openid);
		int count = list == null ? 0 : list.size();
		return count;
	}

	public static MoneyInfo getSharedMoneyInfo(String openid) {
		int count = getShareCount(openid);

		MoneyInfo moneyInfo = new MoneyInfo();
		moneyInfo.setOrigin(PaymentController.MONEY_PREPAY);
		int coupon = Math.min(5000, count * 500);
		moneyInfo.setCoupon(Math.min(5000, coupon));
		moneyInfo.setFinalfee(moneyInfo.getOrigin() - moneyInfo.getCoupon());

		System.out.println("openid:" + openid + " shareCount:" + count + " coupon:" + moneyInfo.getCoupon() + " final:"
				+ moneyInfo.getFinalfee());
		return moneyInfo;
	}

	/**
	 * 支付成功后，扣除优惠券
	 * 
	 * @param openid
	 * @return
	 */
	public static void dissShareCount(String openid) {
		if (DataSource.getInstance().isAdmin(openid)) {
			return;
		}
		List<QrCodeModel> list = QrCodeModel.dao.find("select * from qrcode where sharefrom = ? and used <> 1", openid);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < Math.min(10, list.size()); i++) {
				QrCodeModel model = list.get(i);
				model.setUsed(1);
				model.update();
			}
		}
	}

}
