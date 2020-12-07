package org.andrey.test_chat.domain;

public class IDRegDTO {

	private String newID;
	private String oldID;

	public IDRegDTO() {
		
	}
	
	public IDRegDTO(String newID, String oldID) {
		this.newID = newID;
		this.oldID = oldID;
	}
	
	
	public String getOldID() {
		return oldID;
	}

	public void setOldID(String oldID) {
		this.oldID = oldID;
	}

	public String getNewID() {
		return newID;
	}

	public void setNewID(String newID) {
		this.newID = newID;
	}

}
