package kr.or.kids.global.system.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

@Schema(description = "코드상세정보 VO")
public class CdDtlInfoVO extends CommonVO
{
	/**
	 * 그룹코드
	 */
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "그룹코드")
	private String GRP_CD;

	/**
	 * 상세코드
	 */
	@Schema(description = "상세코드")
	private String DTL_CD;

	/**
	 * 상세코드명
	 */
	@Schema(description = "상세코드명")
	private String DTL_NM;

	/**
	 * 정렬순서
	 */
	@Schema(description = "정렬순서")
	private Integer SORT_NO;

	/**
	 * 사용여부
	 */
	@Schema(description = "사용여부")
	private String USE_YN;

	/**
	 * 반영시작일자
	 */
	@Schema(description = "반영시작일자")
	private String APPLY_STR_DT;

	/**
	 * 반영종료일자
	 */
	@Schema(description = "반영종료일자")
	private String APPLY_END_DT;

	/**
	 * 참조코드01
	 */
	@Schema(description = "참조코드01")
	private String REF_CODE01;

	/**
	 * 참조코드02
	 */
	@Schema(description = "참조코드02")
	private String REF_CODE02;

	/**
	 * 참조코드03
	 */
	@Schema(description = "참조코드03")
	private String REF_CODE03;

	/**
	 * 시스템플래그여부
	 */
	@Schema(description = "시스템플래그여부")
	private String SYS_FLG;

	/**
	 * 추가속성01
	 */
	@Schema(description = "추가속성01")
	private String ATTRIBUTE01;

	/**
	 * 추가속성02
	 */
	@Schema(description = "추가속성02")
	private String ATTRIBUTE02;

	/**
	 * 추가속성03
	 */
	@Schema(description = "추가속성03")
	private String ATTRIBUTE03;

	/**
	 * 추가속성04
	 */
	@Schema(description = "추가속성04")
	private String ATTRIBUTE04;

	/**
	 * 등록자
	 */
	@Schema(description = "등록자")
	private String REG_ID;

	/**
	 * 등록일자
	 */
	@Schema(description = "등록일자")
	private String REG_DATE;

	/**
	 * 수정자
	 */
	@Schema(description = "수정자")
	private String UPD_ID;

	/**
	 * 수정일자
	 */
	@Schema(description = "수정일자")
	private String UPD_DATE;

	private String APPLY_DT_GBN;
	private String REF_CODE01_GBN;
	
	private String TODAY_YYYYMMDD; /* 오늘일자(YYYYMMDD) */
	private String TODAY_DATE; /* 오늘일자만(DD) */
	private String TODAY_WEEK; /* 요일 FULL */
	private String TODAY_WEEK_ABBR; /* 요일 약어 */
	private String TODAY_MONTH; /* 월 FULL */
	private String TODAY_MONTH_ABBR; /* 월 약어 */
	private String TODAY_MONTH_NUM; /* 월 숫자 */
	private String TODAY_YEAR; /* 현재 년도 */
	private String LASTDATE; /* 이번달 마지막일자 */
	
	private String FIRST_DEFAULT_VALUE_YN;
	private String FIRST_DEFAULT_VALUE;
	
	public String getGRP_CD()
	{
		return GRP_CD;
	}
	public void setGRP_CD(String GRP_CD)
	{
		this.GRP_CD = GRP_CD;
	}
	public String getDTL_CD()
	{
		return DTL_CD;
	}
	public void setDTL_CD(String DTL_CD)
	{
		this.DTL_CD = DTL_CD;
	}
	public String getDTL_NM()
	{
		return DTL_NM;
	}
	public void setDTL_NM(String DTL_NM)
	{
		this.DTL_NM = DTL_NM;
	}
	public Integer getSORT_NO()
	{
		return SORT_NO;
	}
	public void setSORT_NO(Integer SORT_NO)
	{
		this.SORT_NO = SORT_NO;
	}
	public String getUSE_YN()
	{
		return USE_YN;
	}
	public void setUSE_YN(String USE_YN)
	{
		this.USE_YN = USE_YN;
	}
	public String getAPPLY_STR_DT()
	{
		return APPLY_STR_DT;
	}
	public void setAPPLY_STR_DT(String APPLY_STR_DT)
	{
		this.APPLY_STR_DT = APPLY_STR_DT;
	}
	public String getAPPLY_END_DT()
	{
		return APPLY_END_DT;
	}
	public void setAPPLY_END_DT(String APPLY_END_DT)
	{
		this.APPLY_END_DT = APPLY_END_DT;
	}
	public String getREF_CODE01()
	{
		return REF_CODE01;
	}
	public void setREF_CODE01(String REF_CODE01)
	{
		this.REF_CODE01 = REF_CODE01;
	}
	public String getREF_CODE02()
	{
		return REF_CODE02;
	}
	public void setREF_CODE02(String REF_CODE02)
	{
		this.REF_CODE02 = REF_CODE02;
	}
	public String getREF_CODE03()
	{
		return REF_CODE03;
	}
	public void setREF_CODE03(String REF_CODE03)
	{
		this.REF_CODE03 = REF_CODE03;
	}
	public String getSYS_FLG()
	{
		return SYS_FLG;
	}
	public void setSYS_FLG(String SYS_FLG)
	{
		this.SYS_FLG = SYS_FLG;
	}
	public String getATTRIBUTE01()
	{
		return ATTRIBUTE01;
	}
	public void setATTRIBUTE01(String ATTRIBUTE01)
	{
		this.ATTRIBUTE01 = ATTRIBUTE01;
	}
	public String getATTRIBUTE02()
	{
		return ATTRIBUTE02;
	}
	public void setATTRIBUTE02(String ATTRIBUTE02)
	{
		this.ATTRIBUTE02 = ATTRIBUTE02;
	}
	public String getATTRIBUTE03()
	{
		return ATTRIBUTE03;
	}
	public void setATTRIBUTE03(String ATTRIBUTE03)
	{
		this.ATTRIBUTE03 = ATTRIBUTE03;
	}
	public String getATTRIBUTE04()
	{
		return ATTRIBUTE04;
	}
	public void setATTRIBUTE04(String ATTRIBUTE04)
	{
		this.ATTRIBUTE04 = ATTRIBUTE04;
	}
	public String getREG_ID()
	{
		return REG_ID;
	}
	public void setREG_ID(String REG_ID)
	{
		this.REG_ID = REG_ID;
	}
	public String getREG_DATE()
	{
		return REG_DATE;
	}
	public void setREG_DATE(String REG_DATE)
	{
		this.REG_DATE = REG_DATE;
	}
	public String getUPD_ID()
	{
		return UPD_ID;
	}
	public void setUPD_ID(String UPD_ID)
	{
		this.UPD_ID = UPD_ID;
	}
	public String getUPD_DATE()
	{
		return UPD_DATE;
	}
	public void setUPD_DATE(String UPD_DATE)
	{
		this.UPD_DATE = UPD_DATE;
	}
	public String getAPPLY_DT_GBN() {
		return APPLY_DT_GBN;
	}
	public void setAPPLY_DT_GBN(String aPPLY_DT_GBN) {
		APPLY_DT_GBN = aPPLY_DT_GBN;
	}
	public String getREF_CODE01_GBN() {
		return REF_CODE01_GBN;
	}
	public void setREF_CODE01_GBN(String rEF_CODE01_GBN) {
		REF_CODE01_GBN = rEF_CODE01_GBN;
	}
	public String getTODAY_YYYYMMDD() {
		return TODAY_YYYYMMDD;
	}
	public void setTODAY_YYYYMMDD(String tODAY_YYYYMMDD) {
		TODAY_YYYYMMDD = tODAY_YYYYMMDD;
	}
	public String getTODAY_DATE() {
		return TODAY_DATE;
	}
	public void setTODAY_DATE(String tODAY_DATE) {
		TODAY_DATE = tODAY_DATE;
	}
	public String getTODAY_WEEK() {
		return TODAY_WEEK;
	}
	public void setTODAY_WEEK(String tODAY_WEEK) {
		TODAY_WEEK = tODAY_WEEK;
	}
	public String getTODAY_WEEK_ABBR() {
		return TODAY_WEEK_ABBR;
	}
	public void setTODAY_WEEK_ABBR(String tODAY_WEEK_ABBR) {
		TODAY_WEEK_ABBR = tODAY_WEEK_ABBR;
	}
	public String getTODAY_MONTH() {
		return TODAY_MONTH;
	}
	public void setTODAY_MONTH(String tODAY_MONTH) {
		TODAY_MONTH = tODAY_MONTH;
	}
	public String getTODAY_MONTH_ABBR() {
		return TODAY_MONTH_ABBR;
	}
	public void setTODAY_MONTH_ABBR(String tODAY_MONTH_ABBR) {
		TODAY_MONTH_ABBR = tODAY_MONTH_ABBR;
	}
	public String getTODAY_MONTH_NUM() {
		return TODAY_MONTH_NUM;
	}
	public void setTODAY_MONTH_NUM(String tODAY_MONTH_NUM) {
		TODAY_MONTH_NUM = tODAY_MONTH_NUM;
	}
	public String getTODAY_YEAR() {
		return TODAY_YEAR;
	}
	public void setTODAY_YEAR(String tODAY_YEAR) {
		TODAY_YEAR = tODAY_YEAR;
	}
	public String getLASTDATE() {
		return LASTDATE;
	}
	public void setLASTDATE(String lASTDATE) {
		LASTDATE = lASTDATE;
	}
	public String getFIRST_DEFAULT_VALUE_YN() {
		return FIRST_DEFAULT_VALUE_YN;
	}
	public void setFIRST_DEFAULT_VALUE_YN(String FIRST_DEFAULT_VALUE_YN) {
		this.FIRST_DEFAULT_VALUE_YN = FIRST_DEFAULT_VALUE_YN;
	}
	public String getFIRST_DEFAULT_VALUE() {
		return FIRST_DEFAULT_VALUE;
	}
	public void setFIRST_DEFAULT_VALUE(String FIRST_DEFAULT_VALUE) {
		this.FIRST_DEFAULT_VALUE = FIRST_DEFAULT_VALUE;
	}
	
    
}