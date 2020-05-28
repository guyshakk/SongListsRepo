package SongListsService.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection = "Songs")
public class Song {
	@Id
private String songId;
	

public Song() {

}


public Song(String songId) {
 	this.songId=songId;
}

public String getSongId() {
	return songId;
}

public void setSongId(String songId) {
	this.songId = songId;
}

}
