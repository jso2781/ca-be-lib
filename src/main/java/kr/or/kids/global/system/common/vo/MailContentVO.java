package kr.or.kids.global.system.common.vo;

public class MailContentVO extends CommonVO
{
	/**
	 * 메일 본문
	 */
	private String CONTENTS;

	/**
	 * 메일 제목
	 */
	private String TITLE;

	/**
	 * FROM 메일 주소
	 */
	private String FROM_ADDRESS;

	/**
	 * TO 메일 주소 
	 */
	private String[] TO_ADDRESS;

	/**
	 * CC 메일 주소
	 */
	private String[] CC_ADDRESS;

	/**
	 * BCC 메일 주소
	 */
	private String[] BCC_ADDRESS;

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public String getCONTENTS() {
		return CONTENTS;
	}

	public void setCONTENTS(String cONTENTS) {
		CONTENTS = cONTENTS;
	}

	public String getFROM_ADDRESS() {
		return FROM_ADDRESS;
	}

	public void setFROM_ADDRESS(String fROM_ADDRESS) {
		FROM_ADDRESS = fROM_ADDRESS;
	}

	public String [] getTO_ADDRESS() {
		return TO_ADDRESS;
	}

	public void setTO_ADDRESS(String [] tO_ADDRESS) {
		TO_ADDRESS = tO_ADDRESS;
	}

	public String [] getCC_ADDRESS() {
		return CC_ADDRESS;
	}

	public void setCC_ADDRESS(String [] cC_ADDRESS) {
		CC_ADDRESS = cC_ADDRESS;
	}

	public String [] getBCC_ADDRESS() {
		return BCC_ADDRESS;
	}

	public void setBCC_ADDRESS(String [] bCC_ADDRESS) {
		BCC_ADDRESS = bCC_ADDRESS;
	}
}