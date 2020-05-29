package SongListsService.data;

import java.util.Comparator;

import org.springframework.data.domain.Sort.Direction;

public class SongComparator implements Comparator<Song> {
	
	private SongAttributes attr;
	private int dir;
	
	public SongComparator(SongAttributes attr, Direction dir) {
		this.attr = attr;
		this.dir = dir.equals(Direction.ASC) ? 1 : -1;
	}
	
	@Override
	public int compare(Song o1, Song o2) {
		return o1.getSongId().compareTo(o2.getSongId())*dir;
	}

}
