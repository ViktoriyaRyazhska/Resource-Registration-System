package org.registrator.community.enumeration;

public enum CommunityStatus {
	INACTIVE(0),
	ACTIVE(1);
	private int status;
	CommunityStatus(int status){
		this.status = status;
	}
	public int getStatusId(){
		return status;
	}
}
