package bean.requestresult;

public class InfoUserResult {

	public InfoUserResult(String title, String content, String button_text, int hasphone, String phone_num) {
		super();
		this.title = title;
		this.content = content;
		this.button_text = button_text;
		this.hasphone = hasphone;
		this.phone_num = phone_num;
	}
	public String title;
	public String content;
	public String button_text;
	public int hasphone;
	public String phone_num;
}
