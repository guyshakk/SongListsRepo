package SongListsService.layout;

import SongListsService.data.Song;

public class SongBoundary {
	
	private String songId;
	
	public SongBoundary() {
	}
	
	public SongBoundary(Song song) {
		if (song.getSongId() != null)
			this.songId = song.getSongId().split("#")[0];
	}

	public String getSongId() {
		return songId;
	}

	public void setSongId(String songId) {
		this.songId = songId;
	}
	
	public Song convertToEntity() {
		Song song = new Song();
		if (this.songId != null)
			song.setSongId(this.songId);
		return song;
	}

}
