package bean.requestresult;

public class InfoUserResult {

	public InfoUserResult(String title, String content, String button_text, int hasphone, String phone_num,
			String page1_config, String page2_config, int shareCount, String shareTip) {
		super();
		this.title = title;
		this.content = content;
		this.button_text = button_text;
		this.hasphone = hasphone;
		this.phone_num = phone_num;
		this.page1_config = page1_config;
		this.page2_config = page2_config;
		this.shareCount = shareCount;
		this.shareTip = shareTip;
	}

	public String title;
	public String content;
	public String button_text;
	public int hasphone;
	public String phone_num;
	public String page1_config;
	public String page2_config;
	public int shareCount;
	public String shareTip;
}
