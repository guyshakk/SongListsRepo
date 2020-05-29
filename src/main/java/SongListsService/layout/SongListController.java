package SongListsService.layout;

import java.util.Collections;
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
import SongListsService.data.SongAttributes;
import SongListsService.data.SongList;
import SongListsService.data.SongListAttributes;
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
			path = baseUrl,
			method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ReturnedSongListBoundary> createSongList(@RequestBody SongListBoundry songListBoundry){
		
		return this.songListService
				.createSongList(songListBoundry.convertToEntity())
				.map(ReturnedSongListBoundary::new);
	}
	
	@RequestMapping(
			path = baseUrl +"/{listId}",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ReturnedSongListBoundary> getSongList(@PathVariable("listId") String listId){
		
		return this.songListService
				.getSongListById(listId)
				.map(ReturnedSongListBoundary::new);
	}
	
	@RequestMapping(
			path = baseUrl +"/{listId}",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Void> updateSongList(@PathVariable("listId") String listId,
			@RequestBody SongListBoundry songListBoundry) {
		
		SongList s = songListBoundry.convertToEntity();
		return !determineUpdateSongListPath(songListBoundry) ? 
				this.songListService.updateSongList(listId, s) : 
					this.songListService.markSongsListAsDeleted(listId, false);
	}
	
	private boolean determineUpdateSongListPath(SongListBoundry songList) {
		return songList.getId() == null &&
				songList.getCreatedTimestamp() == null &&
				songList.getName() == null &&
				songList.getUserEmail() == null &&
				(songList.getDeleted() == null || songList.getDeleted().equals("false"));
	}
	
	@RequestMapping(
			path = baseUrl +"/{listId}/songs",
			method = RequestMethod.PUT,
			consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono <Void> addSongToSongList(@PathVariable("listId") String listId,
			@RequestBody SongBoundary songBoundary){
		
		return this.songListService
				.addSongToSongList(listId, songBoundary.convertToEntity());
	}
	
	@RequestMapping(
			path = baseUrl +"/{listId}/songs/{songId}",
			method = RequestMethod.DELETE)
	public Mono <Void> deleteSongFromSongList(@PathVariable("listId") String listId, 
			@PathVariable("songId") String songId){
		
		return this.songListService.deleteSongFromSongList(listId, songId);
	}
	
	@RequestMapping(
			path = baseUrl,
			method = RequestMethod.DELETE)
	public Mono <Void> deleteSongLists(){
		return this.songListService.deleteAllSongLists();
	}
	
	@RequestMapping(
	path = baseUrl +"/{listId}",
	method = RequestMethod.DELETE)
	public Mono <Void> markSongListAsDeleted(@PathVariable("listId") String listId) {
		
		return this.songListService.markSongsListAsDeleted(listId, true);
	}
	
	@RequestMapping(
			path = baseUrl +"/{listId}/songs", 
			method = RequestMethod.GET,
			produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public  Flux<SongBoundary> getSongs (
			@PathVariable("listId") String listId ,
			@RequestParam(name="sortAttr", required = false, defaultValue = "") String sortAttr,
			@RequestParam(name="orderAttr", required = false, defaultValue = "") String orderAttr){
		
		return getSortOrder(orderAttr).flux()
				.flatMap(o -> {
			return getSortBySongAttributes(sortAttr).flux()
					.flatMap(s -> {
						return this.songListService
								.readSongsBySongListId(listId, o, s)
								.map(SongBoundary::new);
					});
		});
	}
		
	@RequestMapping(
			path = baseUrl, 
			method = RequestMethod.GET,
			produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public  Flux<ReturnedSongListBoundary> getSongLists (
			@RequestParam(name="sortAttr", required = false, defaultValue = "") String sortAttr ,
			@RequestParam(name="orderAttr", required = false, defaultValue = "") String orderAttr)  {
		
		return getSortOrder(orderAttr).flux()
				.flatMap(o -> {
			return getSortByListAttributes(sortAttr).flux()
					.flatMap(s -> {
						return this.songListService
								.readSongLists(o, s)
								.map(ReturnedSongListBoundary::new);
					});
		});
	}

	@RequestMapping(
			path = baseUrl + "/byUser/{userEmail}", 
			method = RequestMethod.GET,
			produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public  Flux<ReturnedSongListBoundary> getSongListsByUserEmail (
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name="sortAttr", required = false, defaultValue = "") String sortAttr ,
			@RequestParam(name="orderAttr", required = false, defaultValue = "") String orderAttr)  {
		
		return getSortOrder(orderAttr).flux()
				.flatMap(o -> {
			return getSortByListAttributes(sortAttr).flux()
					.flatMap(s -> {
						return this.songListService
								.readSongListsByUser(userEmail, o, s)
								.map(ReturnedSongListBoundary::new);
					});
		});
	}

	@RequestMapping(
			path = baseUrl + "/bySongId/{songId}", 
			method = RequestMethod.GET,
			produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public  Flux<ReturnedSongListBoundary> getSongListsBySongId (
			@PathVariable("songId") String songId,
			@RequestParam(name="sortAttr", required = false, defaultValue = "") String sortAttr ,
			@RequestParam(name="orderAttr", required = false, defaultValue = "") String orderAttr)  {
		
		
		return getSortOrder(orderAttr).flux()
				.flatMap(o -> {
			return getSortByListAttributes(sortAttr).flux()
					.flatMap(s -> {
						return this.songListService
								.readSongListsBySongId(songId, o, s)
								.map(ReturnedSongListBoundary::new);
					});
		});
	}
	
	private Mono<SongAttributes> getSortBySongAttributes(String sortAttr) {
		SongAttributes sortBy = SongAttributes.songId;
		if(sortAttr.equals("") || sortAttr.equals(SongAttributes.songId.toString()))
			sortBy = SongAttributes.songId;
		else
			return Mono.error(new IncompatibleSearchInputException());
		return Mono.just(sortBy);
	}
	
	private Mono<Direction> getSortOrder(String orderAttr) {
		Direction dir =Direction.DESC;
		if (orderAttr.equals(Direction.ASC.toString()) || orderAttr.equals(""))
			dir = Direction.ASC;
		else if (orderAttr.equals(Direction.DESC.toString()))
			dir = Direction.DESC;
		else
			return Mono.error(new IncompatibleSearchInputException());
		return Mono.just(dir);
	}
	
	private Mono<SongListAttributes> getSortByListAttributes(String sortAttr) {
		SongListAttributes sortBy = SongListAttributes.id;
		if(sortAttr.equals("") || sortAttr.equals(SongListAttributes.id.toString()))
			sortBy = SongListAttributes.id;
		else if (sortAttr.equals(SongListAttributes.createdTimestamp.toString()))
			sortBy = SongListAttributes.createdTimestamp;
		else if (sortAttr.equals(SongListAttributes.name.toString()))
			sortBy = SongListAttributes.name;
		else if (sortAttr.equals(SongListAttributes.userEmail.toString()))
			sortBy = SongListAttributes.userEmail;
		else
			return Mono.error(new IncompatibleSearchInputException());
		return Mono.just(sortBy);
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
