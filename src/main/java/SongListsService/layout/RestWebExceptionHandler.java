package SongListsService.layout;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import SongListsService.aop.IncompatibleSearchInputException;
import SongListsService.aop.IncompatibleSongDetailsException;
import SongListsService.aop.IncompatibleSongListDetailsException;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
class RestWebExceptionHandler implements WebExceptionHandler{

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    	
        if (ex instanceof IncompatibleSongDetailsException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            // marks the response as complete and forbids writing to it
            return exchange.getResponse().setComplete();
        }
        else  if (ex instanceof IncompatibleSongListDetailsException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return exchange.getResponse().setComplete();
        }
        else if (ex instanceof IncompatibleSearchInputException) {
        	exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        	return exchange.getResponse().setComplete();
        }
        return Mono.error(ex);
    }
}
