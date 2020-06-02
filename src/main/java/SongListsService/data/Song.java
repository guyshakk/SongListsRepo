package SongListsService.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Songs")
public class Song {

	@Id
	private String songId;
	
	public Song() {
	}
	
	public String getSongId() {
		return songId;
	}
	
	public void setSongId(String songId) {
		this.songId = songId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((songId == null) ? 0 : songId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Song other = (Song) obj;
		if (songId == null) {
			if (other.songId != null)
				return false;
		} else if (!songId.equals(other.songId))
			return false;
		return true;
	}	
}