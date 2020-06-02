package SongListsService.infra;

import org.springframework.data.domain.Sort.Direction;

import SongListsService.data.Song;
import SongListsService.data.SongAttributes;
import SongListsService.data.SongList;
import SongListsService.data.SongListAttributes;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SongListService {
	
	public Mono<SongList> createSongList (SongList songsList);
	
	public Mono<SongList> getSongListById (String songListId);
	
	public Mono<Void> updateSongList (String songListId, SongList update);
	
	public Mono<Void> addSongToSongList (String songListId, Song song);
	
	public Mono<Void> markSongsListAsDeleted (String songListId, boolean flag);
	
	public Mono<Void> deleteAllSongLists();
	
	public Mono<Void> deleteSongFromSongList (String songListId, String songId);
	
	public Flux<Song> readSongsBySongListId (String songListId, Direction orderAttr, SongAttributes sortAttr);
 
	public Flux<SongList> readSongListsByUser (String user, Direction orderAttr, SongListAttributes sortAttr);
	
	public Flux<SongList> readSongListsBySongId (String songId, Direction orderAttr, SongListAttributes sortAttr);
	 
	public Flux<SongList> readSongLists(Direction orderAttr, SongListAttributes sortAttr);
}
