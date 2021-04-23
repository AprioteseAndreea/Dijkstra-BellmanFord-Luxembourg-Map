import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Arc2D;

public class Nod {

	private Integer idNode;
	private double latitudine;
	private double longitudine;

	public Nod(Integer idNode, double latitudine, double longitudine) {
		this.idNode = idNode;
		this.latitudine = latitudine;
		this.longitudine = longitudine;
	}

	public Nod() {
		this.idNode = 0;
		this.latitudine = 0;
		this.longitudine = 0;
	}

	public Integer getIdNode() {
		return idNode;
	}

	public void setIdNode(Integer idNode) {
		this.idNode = idNode;
	}

	public double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}

	@Override
	public String toString() {
		return "Node : idNode=" + idNode + ", latitudine=" + latitudine + ", longitudine=" + longitudine + "]";
	}
     
	public void drawNode(Graphics g, double coefLat, double coefLong, double latitudineMIN, double longitudineMIN, Color color) {
		Graphics2D g2D = (Graphics2D) g;
		double xCoord = (this.latitudine - latitudineMIN )* coefLat;
		double yCoord = (this.longitudine - longitudineMIN) * coefLong;
		
		Shape circle = new Arc2D.Double(xCoord, yCoord, 2, 2, 0, 360, Arc2D.CHORD);
		g2D.setColor(color);
		g2D.fill(circle);
		g2D.draw(circle);
		setLatitudine(xCoord);
		setLongitudine(yCoord);
		
		
	}


}
