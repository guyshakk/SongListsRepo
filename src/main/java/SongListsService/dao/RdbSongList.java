package SongListsService.dao;

import java.util.List; 
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import SongListsService.data.Song;
import SongListsService.data.SongList;

@Repository
public class RdbSongList implements SongListDao{
 
@Autowired
public RdbSongList( ) {

}

@Override
public void create(SongList songList) {
	// TODO Auto-generated method stub
	
}

@Override
public Optional<SongList> getSongListById(String songListId) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public void updateSongList(String songListId, SongList update) {
	// TODO Auto-generated method stub
	
}

@Override
public void deleteAllSongLists() {
	// TODO Auto-generated method stub
	
}

@Override
public void deleteSongFromSongList(String listId, String songId) {
	// TODO Auto-generated method stub
	
}

@Override
public void markSongList(String listId) {
	// TODO Auto-generated method stub
	
}

@Override
public List<SongList> readSongListByUserEmail(String userEmail, String sortBy) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<SongList> readAllSongLists(Direction dir, String sortBy) {
	// TODO Auto-generated method stub
	return null;
}

@Override
public List<Song> readAllSongsFromSongList(String listId, Direction dir, String sortBy) {
	// TODO Auto-generated method stub
	return null;
}


}
