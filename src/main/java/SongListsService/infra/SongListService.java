package SongListsService.infra;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import SongListsService.data.Song;
import SongListsService.data.SongList;

public interface SongListService {

	
	
public void addSongToSongList (String songListId,Song song);
	
	public Song getSongById (String songId);
	
	
	public void createSongsList (SongList songsList);
	
	public SongList getSongListById (String songListId);
	
	public void updateSongList (String songListId, SongList update);
	
	public void markSongsListAsDeletedOrUndeleted (String songListId,boolean flag);
	
	public void deleteAllSongsList ();
	
	public void deleteSongFromSongList (String songListId,String songId);
	
	public List<Song> readSongsBySongListId (String songListId,Direction orderAttr ,String sortAttr);
 
	public List<SongList> readSongListByUser (String user , Direction orderAttr ,String sortAttr );
	 
	public List<SongList> readAllSongList(Direction orderAttr ,String sortAttr);
}
