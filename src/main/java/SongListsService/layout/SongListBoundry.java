package SongListsService.layout ; 

import SongListsService.data.SongList;

public class SongListBoundry {
	private String id ;
	private String name;
	private String userEmail;
	private String createdTimestamp;
	private String deleted;

	public SongListBoundry() {
	}
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(String createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}
	
	public String getDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}
	
	public SongList convertToEntity () {	
		SongList sList = new SongList();
		if (getUserEmail() != null)
	    	sList.setUserEmail(this.userEmail);
	    if (getId() != null)
	    	sList.setId(this.id);
	    if (getName() != null)
	    	sList.setName(this.name);
	    return sList;
	}	
}