package SongListsService.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import SongListsService.data.SongList;
import reactor.core.publisher.Flux;


public interface SongListDao extends ReactiveMongoRepository<SongList, String> {
	
	public Flux<SongList> findAllByUserEmail(@Param("userEmail") String userEmail);
}
