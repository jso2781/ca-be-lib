package kr.or.kids.global.system.common.vo;

public class CommonVO
{
	private String LANGUAGE_CODE = "en";
	private String MESSAGE_CODE = null;
	private String MESSAGE_CONTENT = null;
	private String RES_CODE;

	public String getLANGUAGE_CODE() {
		return LANGUAGE_CODE;
	}

	public void setLANGUAGE_CODE(String lANGUAGE_CODE) {
		LANGUAGE_CODE = lANGUAGE_CODE;
	}

	public String getMESSAGE_CODE() {
		return MESSAGE_CODE;
	}

	public void setMESSAGE_CODE(String mESSAGE_CODE) {
		MESSAGE_CODE = mESSAGE_CODE;
	}

	public String getMESSAGE_CONTENT() {
		return MESSAGE_CONTENT;
	}

	public void setMESSAGE_CONTENT(String mESSAGE_CONTENT) {
		MESSAGE_CONTENT = mESSAGE_CONTENT;
	}

	public String getRES_CODE() {
		return RES_CODE;
	}

	public void setRES_CODE(String rES_CODE) {
		RES_CODE = rES_CODE;
	}
	

}