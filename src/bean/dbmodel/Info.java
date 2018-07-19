package bean.dbmodel;

import com.jfinal.plugin.activerecord.Model;

public class Info extends Model<Info> {
	public static final Info dao = new Info().dao();

	private String id;
	private String title;
	private String content;
	private String button_text;
	private int hasphone;
	private String phone_num;

	private static final String id_NAME = "id";
	private static final String title_NAME = "title";
	private static final String content_NAME = "content";
	private static final String button_text_NAME = "button_text";
	private static final String hasphone_NAME = "hasphone";
	private static final String phone_num_NAME = "phone_num";

	public String getId() {
		return getStr(id_NAME);
	}

	public void setId(String id) {
		set(id_NAME, id);
	}

	public String getTitle() {
		return getStr(title_NAME);
	}

	public void setTitle(String title) {
		this.title = title;
		set(title_NAME, title);
	}

	public String getContent() {
		return getStr(content_NAME);
	}

	public void setContent(String content) {
		this.content = content;
		set(content_NAME, content);
	}

	public String getButton_text() {
		return getStr(button_text_NAME);
	}

	public void setButton_text(String button_text) {
		this.button_text = button_text;
		set(button_text_NAME, button_text);
	}

	public int getHasphone() {
		return getInt(hasphone_NAME);
	}

	public void setHasphone(int hasphone) {
		set(hasphone_NAME, hasphone);
	}

	public String getPhone_num() {
		return getStr(phone_num_NAME);
	}

	public void setPhone_num(String phone_num) {
		set(phone_num_NAME, phone_num);
	}
	
}
