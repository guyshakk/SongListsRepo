package SongListsService.layout ; 

import java.time.LocalDate; 
import java.time.format.DateTimeFormatter;

import SongListsService.data.SongList;

public class SongListBoundry {
	private String id ;
	private String name;
	private String userEmail;
	private String createdTimestamp;
	private boolean deleted;

	public SongListBoundry() {
	}
	
	public SongListBoundry(SongList songslist) {
		if(songslist.getCreatedTimestamp() != null) {    
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			String formattedString = songslist.getCreatedTimestamp().format(formatter);
			this.createdTimestamp=formattedString;
		}
		if(songslist.getId() != null)
			this.id=songslist.getId();
		if(songslist.getName() != null)
			this.name = songslist.getName();
		if(songslist.getUserEmail() != null)
			this.userEmail = songslist.getUserEmail();
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
	
	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	public SongList convertToEntity () {	
		SongList sList = new SongList();
		DateTimeFormatter dtf = DateTimeFormatter
				.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		LocalDate dt;
		dt = LocalDate.parse(this.getCreatedTimestamp(), dtf);
	    sList.setCreatedTimestamp(dt);
	    if (getUserEmail() != null)
	    	sList.setUserEmail(this.userEmail);
	    if(getId() != null )
	    	sList.setId(this.id);
	    if(getName() != null)
	    	sList.setName(this.name);
	    return sList;
	}	
}