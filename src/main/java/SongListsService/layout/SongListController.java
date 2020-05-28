package SongListsService.layout;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import SongListsService.aop.IncompatibleSearchInputException;
import SongListsService.aop.IncompatibleSongDetailsException;
import SongListsService.aop.IncompatibleSongListDetailsException;
import SongListsService.aop.SongAlreadyExistsException;
import SongListsService.aop.SongListAlreadyExistsException;
import SongListsService.aop.SongListNotFoundException;
import SongListsService.aop.SongNotFoundException;
import SongListsService.aop.TooManyRequestParametersException;
import SongListsService.data.CriteriaType;
import SongListsService.data.Song;
import SongListsService.data.SongList;
import SongListsService.data.SongListWithDeleted;
import SongListsService.infra.SongListService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SongListController {
	

private final String baseUrl = "/lists";
private SongListService songListService;


@Autowired
public SongListController(SongListService songsListService ) 
{   super();
	this.songListService = songsListService;
}


@RequestMapping(
		path = baseUrl +"/{listId}",
		method = RequestMethod.POST,
		produces = MediaType.APPLICATION_JSON_VALUE)
public Mono <Void> createSongList(@RequestBody SongListBoundry songBoundry){

 this.songListService.createSongsList(songBoundry.convertToEntity());

return Mono.empty() ;
}
@RequestMapping(
		path = baseUrl +"/{listId}",
		method = RequestMethod.GET,
		produces = MediaType.APPLICATION_JSON_VALUE)
public Mono <SongListBoundry> getSongList(@PathVariable("listId") String listId){
	SongList s=songListService.getSongListById(listId);
	return Mono.delay(Duration.ofMillis(200)).map( value -> new SongListBoundry(s));

}





@RequestMapping(
		path = baseUrl +"/{listId}",
		method = RequestMethod.PUT)
public Mono <Void> updateSongList(@PathVariable("listId") String listId,
		@RequestBody SongListWithDeleted songListWithDeleted){
	
	if(songListWithDeleted.songList != null)
	this.songListService.updateSongList(listId, songListWithDeleted.songList.convertToEntity());
	if(songListWithDeleted.deleted != null && !songListWithDeleted.deleted.isDeleted())
		this.songListService.markSongsListAsDeletedOrUndeleted (listId , 
				songListWithDeleted.deleted.isDeleted());

	return Mono.empty();
}


@RequestMapping(
		path = baseUrl +"/{listId}/songs",
		method = RequestMethod.PUT)
public Mono <Void> addSongToSongList(@PathVariable("listId") String listId,
		@RequestBody Song song){
	
	this.songListService.addSongToSongList(listId, song);
	return Mono.empty() ;
}
@RequestMapping(
		path = baseUrl +"/{listId}/songs/{songId}",
		method = RequestMethod.PUT)
public Mono <Void> deleteSongFromSongList(@PathVariable("listId") String listId, 
		@PathVariable("songId") String songId){
	
	
	this.songListService.deleteSongFromSongList(listId, songId);
	
	
	return Mono.empty() ;
}




@RequestMapping(
path = baseUrl +"/{listId}",
method = RequestMethod.DELETE)
public Mono <Void> markSongListAsDeleted(@PathVariable("listId") String listId) {
	
	this.songListService.markSongsListAsDeletedOrUndeleted(listId , true);
	
	return Mono.empty() ;

}



@RequestMapping(
		path = baseUrl +"/{listId}/songs", 
		method = RequestMethod.GET,
		produces=MediaType.TEXT_EVENT_STREAM_VALUE)
public  Flux<Song> getSongsBy (
		@PathVariable("listId") String listId ,
		@RequestParam(name="orderAttr", required = false, defaultValue = "") String orderAttr,
		@RequestParam(name="sortAttr", required = false, defaultValue = "") String sortAttr ){
	
	Direction dir =Direction.DESC;
	
	if ( (orderAttr.trim().isEmpty() || orderAttr == null) || orderAttr.toLowerCase().equals("asc") )
		dir = Direction.ASC;
	
	if( sortAttr.trim().isEmpty() || sortAttr == null)
	    sortAttr = "id" ;
 
	
	List<Song> songs=	this.songListService.readSongsBySongListId(listId, dir, sortAttr);
	
		return Flux.fromIterable(songs).
				delayElements(Duration.ofMillis(300));
}



@RequestMapping(
		path = baseUrl, 
		method = RequestMethod.GET,
		produces=MediaType.TEXT_EVENT_STREAM_VALUE)
public  Flux<SongList> getSongListBy (
		@RequestParam(name="byUser", required = false, defaultValue = "") String byUser,
		@RequestParam(name="bySongId", required = false, defaultValue = "") String bySongListId,
		@RequestParam(name="sortAttr", required = false, defaultValue = "") String sortAttr ,
		@RequestParam(name="orderAttr", required = false, defaultValue = "") String orderAttr)  {
	
	List<SongList> songLists ;
	
Direction dir =Direction.DESC;
	
	if ( (orderAttr.trim().isEmpty() || orderAttr == null) || orderAttr.toLowerCase().equals("asc") )
		dir = Direction.ASC;
	
	if( sortAttr.trim().isEmpty() || sortAttr == null)
	    sortAttr = "id" ;
 
	
	CriteriaType criteria = checkRequestValidity(byUser,bySongListId) ;

	 if ( criteria.equals(CriteriaType.byUser) ) {
		
		songLists = this.songListService.readSongListByUser( byUser, dir, sortAttr ) ;
	
	
	} else {
		
		songLists = this.songListService.readAllSongList(dir, sortAttr) ;
	}

	
	return Flux.fromIterable(songLists).
			delayElements(Duration.ofMillis(300));
	
	
}





private CriteriaType checkRequestValidity( String byUser, String bySongId) {
	 CriteriaType  selectedCriteria = null;
	if (byUser.length() > 0) {
		selectedCriteria = CriteriaType.byUser;
		if (byUser.trim().length() == 0)
			throw new IncompatibleSearchInputException();
	}
	
	if (bySongId.length() > 0) {
		if (selectedCriteria != null)
			throw new TooManyRequestParametersException();
		else
			selectedCriteria = CriteriaType.bySongListId ;
		if (bySongId.trim().length() == 0)
			throw new IncompatibleSearchInputException();
	}
	if (selectedCriteria == null)
		selectedCriteria = CriteriaType.NONE;
	return selectedCriteria;
}



@ExceptionHandler
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public Map<String, Object> handleError (SongNotFoundException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "Song not found";
	}
	return Collections.singletonMap("error", message);
}

@ExceptionHandler
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public Map<String, Object> handleError (SongListNotFoundException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "SongList not found";
	}
	return Collections.singletonMap("error", message);
}


@ExceptionHandler
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public Map<String, Object> handleError (SongAlreadyExistsException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "Song already exists in this songList";
	}
	return Collections.singletonMap("error", message);
}

@ExceptionHandler
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public Map<String, Object> handleError (SongListAlreadyExistsException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "songList already exists";
	}
	return Collections.singletonMap("error", message);
}




@ExceptionHandler
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public Map<String, Object> handleError (IncompatibleSongDetailsException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "Song details supplied are incorrect";
	}
	return Collections.singletonMap("error", message);
}




@ExceptionHandler
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public Map<String, Object> handleError (IncompatibleSongListDetailsException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "SongList details supplied are incorrect";
	}
	return Collections.singletonMap("error", message);
}





@ExceptionHandler
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public Map<String, Object> handleError (IncompatibleSearchInputException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "Search details supplied are incorrect";
	}
	return Collections.singletonMap("error", message);
}

@ExceptionHandler
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public Map<String, Object> handleError (TooManyRequestParametersException e){
	String message = e.getMessage();
	if (message == null || message.trim().length() == 0) {
		message = "Too many request parameters supplied";
	}
	return Collections.singletonMap("error", message);
}



}
