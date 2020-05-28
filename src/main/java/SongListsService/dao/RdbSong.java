package SongListsService.dao;

import java.util.Optional; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import SongListsService.data.Song;

@Repository
public class RdbSong implements SongDao {
 
@Autowired
public RdbSong( ) {
 
}

@Override
public Song create(Song song) {

return null;
}

@Override
public Optional<Song> getSongById(String songId) {
	// TODO Auto-generated method stub
	return null;
}
}
