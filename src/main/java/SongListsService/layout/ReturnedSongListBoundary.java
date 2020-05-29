package SongListsService.layout;

import SongListsService.data.SongList;

public class ReturnedSongListBoundary {

	private String id ;
	private String name;
	private String userEmail;
	private String createdTimestamp;

	public ReturnedSongListBoundary() {
	}
	
	public ReturnedSongListBoundary(SongList songslist) {
		if(songslist.getCreatedTimestamp() != null) {    
			this.createdTimestamp = songslist.getCreatedTimestamp().toString();
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
}
