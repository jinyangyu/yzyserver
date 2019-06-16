package datasource;

import java.util.List;

import bean.dbmodel.QrCodeModel;

public class YouHuiDataSource {

	public static int getShareCount(String openid) {
		List<QrCodeModel> list = QrCodeModel.dao.find("select * from qrcode where sharefrom = ?", openid);
		int count = list == null ? 0 : list.size();
		return count * 5;
	}

}
