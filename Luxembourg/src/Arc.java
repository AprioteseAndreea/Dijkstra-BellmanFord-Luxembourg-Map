import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class Arc implements Comparable<Arc> {

	private Integer fromId;
	private Integer toId;
	private Integer length;

	public Arc(Integer fromId, Integer toId, Integer length) {
		this.toId = toId;
		this.fromId = fromId;
		this.length = length;
	}

	public Arc() {
		this.fromId = 0;
		this.toId = 0;
		this.length = 0;
	}

	public Integer getToId() {
		return toId;
	}

	public void setToId(Integer toId) {
		this.toId = toId;
	}

	public Integer getFromId() {
		return fromId;
	}

	public void setFromId(Integer fromId) {
		this.fromId = fromId;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "Arc [fromId=" + fromId + ", toId=" + toId + ", length=" + length + "]";
	}

	public int compare(Arc a, Arc b) {
		return a.length.compareTo(b.length);
	}

	@Override
	public int compareTo(Arc a) {
		return length.compareTo(a.length);
	}
	public void drawNode(Graphics g,Nod n1, Nod n2, Color color) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setColor(color);
		g2D.draw(new Line2D.Double(n1.getLatitudine(),
				n1.getLongitudine(),
				n2.getLatitudine(),
				n2.getLongitudine()));
			
			
			
		}
		
		
		
	

}
