import java.util.Comparator;

public class ComparePair implements Comparator<Pair> {

	@Override
	public int compare(Pair p1, Pair p2) {
		
		if(p1.getSecond()>p2.getSecond()) return 1;
		else if(p1.getSecond()<p2.getSecond()) return -1;
		return 0;
	}

}
