package SongListsService.infra;

import java.time.LocalDate;
import java.util.ArrayList; 
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import SongListsService.aop.IncompatibleSongDetailsException;
import SongListsService.aop.IncompatibleSongListDetailsException;
import SongListsService.aop.SongAlreadyExistsException;
import SongListsService.aop.SongListAlreadyExistsException;
import SongListsService.aop.SongListNotFoundException;
import SongListsService.aop.SongNotFoundException;
import SongListsService.dao.SongDao;
import SongListsService.dao.SongListDao;
import SongListsService.data.Song;
import SongListsService.data.SongList;

@Service
public class SongListServiceImpl implements SongListService{

	private SongListDao songListDao ;

	private SongDao songDao ;
	
	@Autowired
	public SongListServiceImpl(SongDao songDao ,SongListDao songListDao) {
		this.songListDao = songListDao;
		this.songDao = songDao; 
	}
	
	
	@Override
	public void addSongToSongList(String songListId, Song song) {
		
		SongList songList =this.songListDao.getSongListById(songListId).orElseThrow(() -> 
		  new SongListNotFoundException(songListId));
		
		if ( songList.isDeleted() )
		throw new SongListNotFoundException(songListId) ;
		
		if(song.getSongId().trim().isEmpty() ||song.getSongId() == null )
		
			throw new IncompatibleSongDetailsException(song.getSongId()) ;
		
		if( ! this.songDao.getSongById(song.getSongId()).isPresent() )
		this.songDao.create(song);
		
		if (songList.getSongs().contains(song) )
			throw new SongAlreadyExistsException(song.getSongId());
		else 
		{
			List<Song> songs = songList.getSongs() ;
			songs.add(song);
			songList.setSongs(songs);
		}
			
			
	}

	@Override
	public Song getSongById(String songId) {
		
		 return this.songDao.getSongById(songId).
				 orElseThrow(() -> new SongNotFoundException(songId));
	}

	

	@Override
	public void createSongsList(SongList songList) {
		
		if (this.songListDao.getSongListById(songList.getId()).isPresent())
			throw new SongListAlreadyExistsException(songList.getId());
		
		List<Song> songs =new ArrayList<Song>();
		if(!validateSongList(songList))
			throw new IncompatibleSongListDetailsException(); 
		songList.setDeleted(true);
		songList.setSongs(songs);
		
		this.songListDao.create(songList);
	}

	@Override
	public SongList getSongListById(String songListId) {
		
		SongList songList = this.songListDao.getSongListById(songListId).
				orElseThrow(() -> new SongListNotFoundException(songListId));
		if(songList.isDeleted())
			throw new SongListNotFoundException(songListId) ;
		
		return songList ;
	}

	@Override
	public void updateSongList(String songListId, SongList update) {
		
		SongList songList = this.songListDao.getSongListById(songListId).orElseThrow(	
			() -> new SongListNotFoundException(songListId));
		
		if(songList.isDeleted())
	throw new SongListNotFoundException(songListId) ;
		
		
		if( !update.getName().trim().isEmpty() &&  update.getName() != null )
		songList.setName(update.getName());
		
		if(!update.getUserEmail().trim().isEmpty() &&  update.getUserEmail() != null)
	songList.setUserEmail(update.getUserEmail());
		this.songListDao.updateSongList(songListId,songList);
	}

	@Override
	public void markSongsListAsDeletedOrUndeleted(String songListId, boolean flag) {


		SongList songList = this.songListDao.getSongListById(songListId).orElseThrow(
				() -> new SongListNotFoundException(songListId) ) ;
		
		songList.setDeleted(flag);
	this.songListDao.updateSongList(songListId, songList);
	}

	@Override
	public void deleteAllSongsList() {
		
     this.songListDao.deleteAllSongLists();		
	}

	@Override
	public void deleteSongFromSongList(String songListId, String songId) {
		
		SongList songList = this.songListDao.getSongListById(songListId).orElseThrow(
				() -> new SongListNotFoundException(songListId) ) ;
		
		if (songList.isDeleted())
			throw new SongListNotFoundException(songListId) ;
		
	List<Song> songs = songList.getSongs() ;
	
	if (!songs.contains(new Song(songId)))
		throw new SongNotFoundException(songId) ;
	
	
	songs.remove(new Song(songId)) ;
		
	songList.setSongs(songs);
	this.songListDao.updateSongList(songListId, songList);
	}

	@Override
	public List<Song> readSongsBySongListId(String songListId, Direction orderAttr, String sortAttr) {

return this.songListDao.readAllSongsFromSongList(songListId, orderAttr, sortAttr) ;
	}

	@Override
	public List<SongList> readSongListByUser(String user, Direction orderAttr, String sortAttr) {
	
		return this.readSongListByUser(user, orderAttr, sortAttr);
	
	}

	@Override
	public List<SongList> readAllSongList(Direction orderAttr, String sortAttr) {
	
		return this.readAllSongList(orderAttr, sortAttr);
	
	}

	private boolean validateEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                "[a-zA-Z0-9_+&*-]+)*@" + 
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                "A-Z]{1,}$"; 
		Pattern pat = Pattern.compile(emailRegex); 
		if (email == null) 
			return false; 
		return pat.matcher(email).matches();
	}
	
	
	private boolean validateSongList(SongList songList) {
		return songList.getCreatedTimestamp() != null &&
				songList.getCreatedTimestamp().isBefore(LocalDate.now()) &&
				songList.getCreatedTimestamp().isAfter(LocalDate.now().minusYears(150)) && //we assume no person is older than 150
				songList.getId() != null &&
				!songList.getId().trim().isEmpty() &&
				validateEmail(songList.getUserEmail()) &&
				songList.getName() != null &&
				!songList.getName().trim().isEmpty() ;
			
	}

	
}
