package demo.result;

public class Result {
	private int result_code;
	private String result_msg;
	private Object data;

	public Result(int result_code, String result_msg, Object data) {
		super();
		this.result_code = result_code;
		this.result_msg = result_msg;
		this.data = data;
	}

	public int getResult_code() {
		return result_code;
	}

	public void setResult_code(int result_code) {
		this.result_code = result_code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResult_msg() {
		return result_msg;
	}

	public void setResult_msg(String result_msg) {
		this.result_msg = result_msg;
	}

}
