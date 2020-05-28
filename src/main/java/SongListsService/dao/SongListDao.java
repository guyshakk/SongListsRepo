package SongListsService.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort.Direction;

import SongListsService.data.Song;
import SongListsService.data.SongList;


public interface SongListDao {

	
public void create (SongList songList);
	
	public Optional<SongList> getSongListById (String songListId);
	
	public void updateSongList (String songListId, SongList update);

	public void deleteAllSongLists ();
	
	public void deleteSongFromSongList (String listId ,String songId);

	public void markSongList (String listId );

	public List<SongList> readSongListByUserEmail (String userEmail, String sortBy);
	
	public List<SongList> readAllSongLists (Direction dir , String sortBy);
	
	public List<Song> readAllSongsFromSongList (String listId , Direction dir , String sortBy);

}
