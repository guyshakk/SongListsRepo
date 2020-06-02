package SongListsService.data;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "SongsLists")
public class SongList {

	@Id
	private String id ;
	private String name;
	private String userEmail;
	private Instant createdTimestamp;
	private boolean deleted;
		
	public SongList() {
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

	public Instant getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(Instant createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
