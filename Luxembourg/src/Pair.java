

public class Pair  {
	private int first;
	private int second;

	public Pair(int first, int second) {

		this.first = first;
		this.second = second;
	}

	public Pair() {
		this.first = 0;
		this.second = 0;
	}

	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	public int getFirst() {
		return first;
	}

	public void setFirst(int first) {
		this.first = first;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	

	
}