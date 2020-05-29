package SongListsService.data;

import java.util.Comparator;

import org.springframework.data.domain.Sort.Direction;

public class SongListComparator implements Comparator<SongList> {
	
	private SongListAttributes attr;
	private int dir;
	
	public SongListComparator(SongListAttributes attr, Direction dir) {
		this.dir = dir.equals(Direction.ASC) ? 1 : -1;
		this.attr = attr;
	}
	
	@Override
	public int compare(SongList o1, SongList o2) {
		if (this.attr.equals(SongListAttributes.id)) {
			return o1.getId().compareTo(o2.getId())*dir;
		}
		else if (this.attr.equals(SongListAttributes.name)) {
			return o1.getName().compareTo(o2.getName())*dir;
		}
		else if (this.attr.equals(SongListAttributes.userEmail)) {
			return o1.getUserEmail().compareTo(o2.getUserEmail())*dir;
		}
		else {//createdTimeStamp
			return o1.getCreatedTimestamp().compareTo(o2.getCreatedTimestamp())*dir;
		}
	}

}
