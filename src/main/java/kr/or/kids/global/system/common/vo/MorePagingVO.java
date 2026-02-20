package kr.or.kids.global.system.common.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;

/**
 * 사용자가 목록조회시 More처리용 속성
 * 사용자가 More 버튼을 누르거나 스크롤을 바닥까지 내렸을때 More 페이징 처리
 */
@Schema(description = "More Paging VO")
public class MorePagingVO extends CommonVO
{
	/**
	 * 현재  More 페이지 번호(1,2,3,...)
	 */
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "현재  More 페이지 번호")
	private Integer CURRENT_PAGE_NUM = 1;

	/**
	 * More 페이지당 보여지는 데이터 건수
	 * Query LIMIT 에 사용
	 */
	@Schema(requiredMode = RequiredMode.REQUIRED, description = "More 페이지당 보여지는 데이터 건수(Query LIMIT 에 사용)")
	private Integer PAGE_ROW_SCALE = 200;

	/**
	 * More 페이지 마지막 번호(=전체 페이지 갯수)
	 */
	@Schema(description = "More 페이지 마지막 번호(=전체 페이지 갯수)")
	private Integer LAST_PAGE_NUM;

	/**
	 * 전체 데이터 건수
	 */
	@Schema(description = "전체 데이터 건수")
	public Integer TOTAL_ROW_COUNT;

	/**
	 * ROW INDEX(0부터 첫번째 행시작)
	 * 전체 데이터 건수를 기준으로 몇번째 데이터인지 나타냄
	 * ex) 21 - 22번째 데이터 인덱스( Query LIMIT OFFSET에 사용 )
	 */
	@Schema(description = "ROW INDEX(0부터 첫번째 행시작)", hidden = true)
	private Integer ROW_OFFSET;

	public Integer getCURRENT_PAGE_NUM()
	{
		return this.CURRENT_PAGE_NUM;
	}

	public void setCURRENT_PAGE_NUM(Integer cURRENT_PAGE_NUM)
	{
		this.CURRENT_PAGE_NUM = cURRENT_PAGE_NUM;
	}

	public Integer getPAGE_ROW_SCALE()
	{
		return this.PAGE_ROW_SCALE;
	}

	public void setPAGE_ROW_SCALE(Integer pAGE_ROW_SCALE)
	{
		this.PAGE_ROW_SCALE = pAGE_ROW_SCALE;
	}

	public Integer getLAST_PAGE_NUM()
	{
		this.LAST_PAGE_NUM = PAGE_ROW_SCALE != null && PAGE_ROW_SCALE > 0 ? (int) Math.ceil((double)TOTAL_ROW_COUNT / PAGE_ROW_SCALE) : 0;
		return this.LAST_PAGE_NUM;
	}

	public void setLAST_PAGE_NUM(Integer lAST_PAGE_NUM)
	{
		this.LAST_PAGE_NUM = lAST_PAGE_NUM;
	}

	public Integer getROW_OFFSET()
	{
		this.ROW_OFFSET = (CURRENT_PAGE_NUM - 1) * PAGE_ROW_SCALE;
		return this.ROW_OFFSET;
	}

	public void setROW_OFFSET(Integer rOW_OFFSET)
	{
		this.ROW_OFFSET = rOW_OFFSET;
	}

	public Integer getTOTAL_ROW_COUNT()
	{
		return this.TOTAL_ROW_COUNT;
	}

	public void setTOTAL_ROW_COUNT(Integer tOTAL_ROW_COUNT)
	{
		this.TOTAL_ROW_COUNT = tOTAL_ROW_COUNT;
	}
}
