package SongListsService.dao;

import java.util.Optional;

import SongListsService.data.Song;


public interface SongDao {
	
    public Song create (Song song);
	
	public Optional<Song> getSongById (String songId);
	
	
}
