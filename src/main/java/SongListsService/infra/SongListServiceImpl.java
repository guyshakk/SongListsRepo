package SongListsService.infra;

import java.time.Instant;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import SongListsService.aop.IncompatibleSongDetailsException;
import SongListsService.aop.IncompatibleSongListDetailsException;
import SongListsService.dao.SongDao;
import SongListsService.dao.SongListDao;
import SongListsService.data.Song;
import SongListsService.data.SongAttributes;
import SongListsService.data.SongComparator;
import SongListsService.data.SongList;
import SongListsService.data.SongListAttributes;
import SongListsService.data.SongListComparator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
	public Mono<SongList> createSongList(SongList songList) {
		
		if (!validateSongList(songList))
			return Mono.error(new IncompatibleSongListDetailsException());
		songList.setId(null);
	    songList.setCreatedTimestamp(Instant.now());
		songList.setDeleted(false);
		return this.songListDao.save(songList);	
	}
	
	@Override
	public Mono<SongList> getSongListById(String songListId) {
		
		return this.songListDao.findById(songListId).filter(s -> !s.isDeleted());
	}
	
	@Override
	public Mono<Void> addSongToSongList(String songListId, Song song) {
		
		if (!validateSong(song))
			return Mono.error(new IncompatibleSongDetailsException());
		
		song.setSongId(song.getSongId()+"#"+songListId);
		
		//Save song in DB
		return this.songDao.findById(song.getSongId())
			.flatMap(old -> {
				//In the future maybe certain updates to the song would be handled here
				return this.songDao.save(song);
			})
			.switchIfEmpty(this.songDao.save(song))
			.flatMap(d -> Mono.empty());		
	}

	private boolean validateSong(Song song) {
		return song.getSongId() != null &&
				!song.getSongId().trim().isEmpty();
	}

	@Override
	public Mono<Void> updateSongList(String songListId, SongList update) {
		
		return this.getSongListById(songListId)
				.flatMap(oldSongList -> {
					if (update.getName() != null && !update.getName().trim().isEmpty())
						oldSongList.setName(update.getName());
					if (validateEmail(update.getUserEmail()))
						oldSongList.setUserEmail(update.getUserEmail());
					return this.songListDao.save(oldSongList);
				})
				.flatMap(d -> Mono.empty());		
	}

	@Override
	public Mono<Void> markSongsListAsDeleted(String songListId, boolean flag) {

		return this.songListDao.findById(songListId)
			.flatMap(oldSongList -> {
				oldSongList.setDeleted(flag);
				return this.songListDao.save(oldSongList);
			})
			.flatMap(d -> Mono.empty());
	}

	@Override
	public Mono<Void> deleteAllSongLists() {
		
		return this.songListDao.deleteAll();		
	}

	@Override
	public Mono<Void> deleteSongFromSongList(String songListId, String songId) {
		
		return this.songDao.deleteById(songId+"#"+songListId);
	}

	@Override
	public Flux<Song> readSongsBySongListId(String songListId, Direction orderAttr, SongAttributes sortAttr) {

		SongComparator comp = new SongComparator(sortAttr, orderAttr);
		return this.getSongListById(songListId)
				.flux()
				.flatMap(list -> this.songDao.findAllBySongIdEndingWith("#"+list.getId()))
				.sort(comp);
	}

	@Override
	public Flux<SongList> readSongListsByUser(String user, Direction orderAttr, SongListAttributes sortAttr) {
	
		SongListComparator comp = new SongListComparator(sortAttr, orderAttr);
		return this.songListDao
				.findAllByUserEmail(user)
				.filter(list -> !list.isDeleted())
				.sort(comp);
	}

	@Override
	public Flux<SongList> readSongListsBySongId(String songId, Direction orderAttr, SongListAttributes sortAttr) {
		SongListComparator comp = new SongListComparator(sortAttr, orderAttr);
		return this.songDao.findAllBySongIdStartingWith(songId+"#")
				.flatMap(s -> this.songListDao
							.findById(s.getSongId()
									.split("#")[1]))
				.filter(list -> !list.isDeleted())
				.sort(comp);
	}
	
	@Override
	public Flux<SongList> readSongLists(Direction orderAttr, SongListAttributes sortAttr) {
	
		SongListComparator comp = new SongListComparator(sortAttr, orderAttr);
		return this.songListDao
				.findAll()
				.filter(a -> !a.isDeleted())
				.sort(comp);
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
		return validateEmail(songList.getUserEmail()) &&
				songList.getName() != null &&
				!songList.getName().trim().isEmpty() ;
	}
}
