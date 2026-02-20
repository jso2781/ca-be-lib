package kr.or.kids.global.system.common.vo;

/**
 * The Class AttachFile.
 * 
 * @author JungmokKim, Commoninfra
 */
public class AttachFileVO extends CommonVO {
	
	/** The file uuid. */
	protected String fileUUID;
	
	/** The group uuid. */
	protected String groupUUID;
	
	/** The user id. */
	protected String userId;
	
	/** The upload date. */
	protected String uploadDate;
	
	/** The original name. */
	protected String originalName;
	
	/** The size. */
	protected long size;
	
	/** The root upload path. */
	private String rootUploadPath;
	
	/** The relate upload path. */
	private String relateUploadPath;
	
	/** The relate upload path. */
	private String orignalServerIP;
	
	private String orignalServerUser;
	
	private String orignalServerPassWd;

	public String getOrignalServerUser() {
		return orignalServerUser;
	}

	public void setOrignalServerUser(String orignalServerUser) {
		this.orignalServerUser = orignalServerUser;
	}

	public String getOrignalServerPassWd() {
		return orignalServerPassWd;
	}

	public void setOrignalServerPassWd(String orignalServerPassWd) {
		this.orignalServerPassWd = orignalServerPassWd;
	}

	public String getOrignalServerIP() {
		return orignalServerIP;
	}

	public void setOrignalServerIP(String orignalServerIP) {
		this.orignalServerIP = orignalServerIP;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Gets the original name.
	 * 
	 * @return the original name
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * Sets the original name.
	 * 
	 * @param originalName the new original name
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * Sets the size.
	 * 
	 * @param size the new size
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * Gets the group uuid.
	 * 
	 * @return the group uuid
	 */
	public String getGroupUUID() {
		return groupUUID;
	}

	/**
	 * Sets the group uuid.
	 * 
	 * @param groupUUID the new group uuid
	 */
	public void setGroupUUID(String groupUUID) {
		this.groupUUID = groupUUID;
	}

	/**
	 * Gets the file uuid.
	 * 
	 * @return the file uuid
	 */
	public String getFileUUID() {
		return fileUUID;
	}

	/**
	 * Sets the file uuid.
	 * 
	 * @param uuid the new file uuid
	 */
	public void setFileUUID(String uuid) {
		this.fileUUID = uuid;
	}

	/**
	 * Sets the upload date.
	 * 
	 * @param uploadDate the new upload date
	 */
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * Gets the upload date.
	 * 
	 * @return the upload date
	 */
	public String getUploadDate() {
		return uploadDate;
	}

	/**
	 * Gets the upload path.
	 * 
	 * @return the upload path
	 */
	public String getUploadPath() {
		StringBuffer finalUploadPath = new StringBuffer();
		finalUploadPath.append(rootUploadPath).append(relateUploadPath);
		return finalUploadPath.toString();
	}
	
	/**
	 * Gets the smart upload path.
	 * 
	 * @return the upload path
	 */
	public String getSmartUploadPath() {
		StringBuffer finalUploadPath = new StringBuffer();
		finalUploadPath.append(rootUploadPath);
		return finalUploadPath.toString();
	}
	
	/**
	 * Gets the root upload path.
	 * 
	 * @return the root upload path
	 */
	public String getRootUploadPath() {
		return rootUploadPath;
	}

	/**
	 * Sets the root upload path.
	 * 
	 * @param rootUploadPath the new root upload path
	 */
	public void setRootUploadPath(String rootUploadPath) {
		this.rootUploadPath = rootUploadPath;
	}

	/**
	 * Sets the relate upload path.
	 * 
	 * @param relateUploadPath the new relate upload path
	 */
	public void setRelateUploadPath(String relateUploadPath) {
		this.relateUploadPath = relateUploadPath;
	}

	/**
	 * Gets the relate upload path.
	 * 
	 * @return the relate upload path
	 */
	public String getRelateUploadPath() {
		return relateUploadPath;
	}
}
