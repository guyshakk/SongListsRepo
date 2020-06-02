package SongListsService.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import SongListsService.data.Song;
import reactor.core.publisher.Flux;


public interface SongDao extends ReactiveMongoRepository<Song, String> {
	
    public Flux<Song> findAllBySongIdLike(@Param("songId") String songId);
	
    public Flux<Song> findAllBySongIdStartingWith(@Param("songId") String songId);

    public Flux<Song> findAllBySongIdEndingWith(@Param("songId") String songId);
}
