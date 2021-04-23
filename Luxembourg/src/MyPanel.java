import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private ArrayList<Nod> listaNoduri;
	private ArrayList<Arc> listaArce;

	Point pointStart = null;
	Point pointEnd = null;
	ArrayList<Nod> startSiEnd = new ArrayList<Nod>();
	ArrayList<Integer> noduriVizitate = new ArrayList<Integer>();
	boolean isDragging = false;
	public static int optiune;

	double longitudineMIN;
	double longitudineMAX;
	double latitudineMIN;
	double latitudineMAX;

	double coefLong;
	double coefLat;

	public MyPanel() {

		setBorder(BorderFactory.createLineBorder(Color.black));
		System.out.println("Cu ce algoritm doriti sa gasim drumul minim? ");
		System.out.println("1 - Djkstra");
		System.out.println("2 - Bellman-Ford");
		try (Scanner console = new Scanner(System.in)) {
			optiune = console.nextInt();
		}
		readNodesFromXMLFileUsingSAXParser();
		readArcsFromXMLFileUsingSAXParser();
		determinateMINsiMax();

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pointStart = e.getPoint();

			}

			public void mouseReleased(MouseEvent e) {

				if (!isDragging) {
					startSiEnd.add(searchNode(e.getPoint()));
					System.out.println("Nod apasat: " + searchNode(e.getPoint()).toString());

					if (startSiEnd.size() >= 2) {

						if (optiune == 1) {
							noduriVizitate.clear();
							DjkstraAlgorithm();
							repaint();
						}
						if (optiune == 2) {
							BellmanFord();
						// Bellman_Ford();
						   repaint();
						}
					}

				}

				pointStart = null;
				isDragging = false;
			}
		});

		
	}

	private void readNodesFromXMLFileUsingSAXParser() {
		try {
			listaNoduri = new ArrayList<Nod>();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				boolean idNode = false;
				boolean longitude = false;
				boolean latitude = false;

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
					// System.out.println("Start Element :" + qName);
					if (qName.equalsIgnoreCase("node")) {
						Integer id = Integer.parseInt(attributes.getValue("id"));
						idNode = true;
						Integer longit = Integer.parseInt(attributes.getValue("longitude"));
						longitude = true;
						Integer lat = Integer.parseInt(attributes.getValue("latitude"));
						latitude = true;
						Nod currNod = new Nod(id, lat, longit);
						listaNoduri.add(currNod);

					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					// System.out.println("End Element:" + qName);
				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (idNode) {
						// System.out.println("ID : " + new String(ch, start, length));
						idNode = false;
					}
					if (longitude) {
						// System.out.println("First Name: " + new String(ch, start, length));
						longitude = false;
					}
					if (latitude) {
						// System.out.println("Last Name: " + new String(ch, start, length));
						latitude = false;
					}

				}
			};
			saxParser.parse("src/map2.xml", handler);
			System.out.println("Number of nodes: " + listaNoduri.size());
		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	private void readArcsFromXMLFileUsingSAXParser() {
		try {
			listaArce = new ArrayList<Arc>();
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			DefaultHandler handler = new DefaultHandler() {
				boolean from = false;
				boolean to = false;
				boolean arcLength = false;

				public void startElement(String uri, String localName, String qName, Attributes attributes)
						throws SAXException {
					// System.out.println("Start Element :" + qName);
					if (qName.equalsIgnoreCase("arc")) {
						Integer currentfrom = Integer.parseInt(attributes.getValue("from"));
						from = true;
						Integer currentto = Integer.parseInt(attributes.getValue("to"));
						to = true;
						Integer currentlength = Integer.parseInt(attributes.getValue("length"));
						arcLength = true;
						Arc currArc = new Arc(currentfrom, currentto, currentlength);
						listaArce.add(currArc);

					}

				}

				public void endElement(String uri, String localName, String qName) throws SAXException {
					// System.out.println("End Element:" + qName);
				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if (from) {
						// System.out.println("ID : " + new String(ch, start, length));
						from = false;
					}
					if (to) {
						// System.out.println("First Name: " + new String(ch, start, length));
						to = false;
					}
					if (arcLength) {
						// System.out.println("Last Name: " + new String(ch, start, length));
						arcLength = false;
					}

				}
			};
			saxParser.parse("src/map2.xml", handler);
			System.out.println("Number of arcs: " + listaArce.size());

		} catch (

		Exception e) {
			e.printStackTrace();

		}
	}

	private void determinateMINsiMax() {
		longitudineMIN = listaNoduri.get(0).getLongitudine();
		longitudineMAX = listaNoduri.get(0).getLongitudine();

		latitudineMIN = listaNoduri.get(0).getLatitudine();
		latitudineMAX = listaNoduri.get(0).getLatitudine();
		for (int i = 1; i < listaNoduri.size(); i++) {
			if (listaNoduri.get(i).getLatitudine() < latitudineMIN) {
				latitudineMIN = listaNoduri.get(i).getLatitudine();
			}
			if (listaNoduri.get(i).getLatitudine() > latitudineMAX) {
				latitudineMAX = listaNoduri.get(i).getLatitudine();
			}

			if (listaNoduri.get(i).getLongitudine() < longitudineMIN) {
				longitudineMIN = listaNoduri.get(i).getLongitudine();
			}
			if (listaNoduri.get(i).getLongitudine() > longitudineMAX) {
				longitudineMAX = listaNoduri.get(i).getLongitudine();
			}

		}
		coefLat = (double) 800 / (latitudineMAX - latitudineMIN);
		coefLong = (double) 800 / (longitudineMAX - longitudineMIN);
		System.out.println("Coef lat: " + coefLat);
		System.out.println("Coef long: " + coefLong);
		System.out.println("Latitudinea Min: " + latitudineMIN + " si Max: " + latitudineMAX);
		System.out.println("Longitudinea Min: " + longitudineMIN + " si Max: " + longitudineMAX);

	}

	private double distance(Nod a, Point b) {
		return Math.sqrt((a.getLatitudine() - (double) b.x) * (a.getLatitudine() - (double) b.x)
				+ (a.getLongitudine() - (double) b.y) * (a.getLongitudine() - (double) b.y));
	}

	private Nod searchNode(Point x) {
		Nod aux = null;
		double distMin = Double.MAX_VALUE;
		for (int i = 0; i < listaNoduri.size(); i++) {
			if (distance(listaNoduri.get(i), x) < distMin) {
				distMin = distance(listaNoduri.get(i), x);
				aux = listaNoduri.get(i);

			}

		}
		return aux;
	}

	private void BellmanFord() {
		Collections.sort(listaArce);

		ArrayList<ArrayList<Pair>> adj = new ArrayList<>(listaNoduri.size());
		for (int i = 0; i < listaNoduri.size(); i++)
			adj.add(new ArrayList<>());
		for (int i = 0; i < listaArce.size(); i++) {
			Pair toPair = new Pair(listaArce.get(i).getToId(), listaArce.get(i).getLength());
			adj.get(listaArce.get(i).getFromId()).add(toPair);

		}
//		System.out.println("Lista de adiacenta este: ");
//		for (int i = 0; i < listaNoduri.size(); i++) {
//			System.out.print(i);
//			Iterator<Pair> it = adj.get(i).iterator();
//
//			while (it.hasNext()) {
//
//				System.out.print("->" + it.next());
//			}
//			System.out.println();
//		}

		ArrayList<Integer> P = new ArrayList<Integer>(listaNoduri.size());

		Integer sourceId = startSiEnd.get(0).getIdNode();
		Integer destinationId = startSiEnd.get(1).getIdNode();

		ArrayList<Integer> D = new ArrayList<Integer>(listaNoduri.size());
		for (int index = 0; index < listaNoduri.size(); index++) {
			P.add(0);
			if (index == sourceId)
				D.add(0);
			else
				D.add(Integer.MAX_VALUE);
		}
		for (int i = 0; i < listaNoduri.size(); ++i) {
			for (int index1 = 0; index1 < adj.size(); ++index1) {
				for (int index2 = 0; index2 < adj.get(index1).size(); ++index2) {

					Integer s = adj.get(index1).get(index2).getFirst();
					Integer length = adj.get(index1).get(index2).getSecond();
		
					if (D.get(index1) != Integer.MAX_VALUE && D.get(s) > D.get(index1) + length) {
						D.set(s, D.get(index1) + length);
						P.set(s, index1);
						if (s == destinationId)
							i = listaNoduri.size();

					}

				}
			}
		}
//		 for (int j = 0; j <listaArce.size() ; ++j) { 
//	            int u = listaArce.get(j).getFromId(); 
//	            int v = listaArce.get(j).getToId();
//	            int weight = listaArce.get(j).getLength();
//	            if (D.get(u) != Integer.MAX_VALUE && D.get(u) + weight < D.get(v)) { 
//	                System.out.println("Graful contine circuite negative"); 
//	                return; 
//	            } 
//	        }
		System.out.println("Distanta de la sursa la destinatie este: " + D.get(destinationId));

		noduriVizitate.add(destinationId);

		Integer parinteActual = P.get(destinationId);
		Integer nodActual = destinationId;
		noduriVizitate.add(parinteActual);

		while (parinteActual != 0) {
			nodActual = parinteActual;
			// System.out.println("Nod actual: " + nodActual);

			parinteActual = P.get(nodActual);
			// System.out.println("Parinte Actual: " + parinteActual);
			noduriVizitate.add(nodActual);
		}
	}

	private void Bellman_Ford() {
		Collections.sort(listaArce);

		ArrayList<ArrayList<Pair>> adj = new ArrayList<>(listaNoduri.size());
		for (int i = 0; i < listaNoduri.size(); i++)
			adj.add(new ArrayList<>());
		for (int i = 0; i < listaArce.size(); i++) {
			Pair toPair = new Pair(listaArce.get(i).getFromId(), listaArce.get(i).getLength());
			adj.get(listaArce.get(i).getToId()).add(toPair);

		}
		Integer[] P = new Integer[listaNoduri.size()];
		Integer[] D = new Integer[listaNoduri.size()];
		Integer[] DPrim = new Integer[listaNoduri.size()];

		Integer sourceId = startSiEnd.get(0).getIdNode();
		Integer destinationId = startSiEnd.get(1).getIdNode();

		for (int index = 0; index < listaNoduri.size(); index++) {
			P[index] = 0;
			if (index == sourceId)
				D[index] = 0;
			else
				D[index] = Integer.MAX_VALUE;
		}
		do {
			for (int index = 0; index < listaNoduri.size(); ++index) {
				DPrim[index] = D[index];
			}
			for (int y = 0; y < listaNoduri.size(); ++y) {
				if (!adj.get(y).isEmpty()) {
					Integer x = adj.get(y).get(0).getFirst();
					Integer length = adj.get(y).get(0).getSecond();
					if (DPrim[x] + length < DPrim[y]) {
						D[y] = DPrim[x] + length;
						P[y] = x;
					}
				}
			}

		} while (DPrim[destinationId] == D[destinationId]);

		System.out.println("Distanta de la sursa la destinatie este: " + D[destinationId]);
//		 for (int j = 0; j <listaArce.size() ; ++j) { 
//        int u = listaArce.get(j).getFromId(); 
//        int v = listaArce.get(j).getToId();
//        int weight = listaArce.get(j).getLength();
//        if (D[u] != Integer.MAX_VALUE && D[u] + weight < D[v]) { 
//            System.out.println("Graful contine circuite negative"); 
//            return; 
//        } 
//    }
//		noduriVizitate.add(destinationId);
//
		Integer parinteActual = P[destinationId];
		Integer nodActual = destinationId;
		noduriVizitate.add(parinteActual);

		while (parinteActual != 0) {
			nodActual = parinteActual;
			// System.out.println("Nod actual: " + nodActual);

			parinteActual = P[nodActual];
			// System.out.println("Parinte Actual: " + parinteActual);
			noduriVizitate.add(nodActual);
		}

	}

	private void DjkstraAlgorithm() {
		Collections.sort(listaArce);

		ArrayList<ArrayList<Pair>> adj = new ArrayList<>(listaNoduri.size());
		for (int i = 0; i < listaNoduri.size(); i++)
			adj.add(new ArrayList<>());
		for (int i = 0; i < listaArce.size(); i++) {
			Pair toPair = new Pair(listaArce.get(i).getToId(), listaArce.get(i).getLength());
			adj.get(listaArce.get(i).getFromId()).add(toPair);
		}

		PriorityQueue<Pair> W = new PriorityQueue<Pair>(new ComparePair());
		ArrayList<Integer> P = new ArrayList<Integer>(listaNoduri.size());

		Integer sourceId = startSiEnd.get(0).getIdNode();
		Integer destinationId = startSiEnd.get(startSiEnd.size() - 1).getIdNode();

		W.add(new Pair(sourceId, 0));
		ArrayList<Integer> D = new ArrayList<Integer>(listaNoduri.size());
		for (int index = 0; index < listaNoduri.size(); index++) {
			P.add(0);
			if (index == sourceId)
				D.add(0);
			else
				D.add(Integer.MAX_VALUE);
		}

		while (!W.isEmpty()) {
			Integer x = W.peek().getFirst();
			W.poll();
			if (x == destinationId)
				break;
			for (int index = 0; index < adj.get(x).size(); index++) {
				Integer y = adj.get(x).get(index).getFirst();
				Integer length = adj.get(x).get(index).getSecond();
				if (D.get(y) > D.get(x) + length) {
					D.set(y, D.get(x) + length);
					W.add(new Pair(y, D.get(y)));
					P.set(y, x);
				}
			}

		}
		System.out.println("Distanta de la sursa la destinatie este: " + D.get(destinationId));

		noduriVizitate.add(destinationId);

		Integer parinteActual = P.get(destinationId);
		Integer nodActual = destinationId;
		noduriVizitate.add(parinteActual);

		while (parinteActual != 0) {
			nodActual = parinteActual;
			// System.out.println("Nod actual: " + nodActual);

			parinteActual = P.get(nodActual);
			// System.out.println("Parinte Actual: " + parinteActual);
			noduriVizitate.add(nodActual);
		}

	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (noduriVizitate.size() > 0) {
			Graphics2D g2D = (Graphics2D) g;

			for (int i = 0; i < listaNoduri.size(); i++) {
				Shape circle2 = new Arc2D.Double(listaNoduri.get(i).getLatitudine(),
						listaNoduri.get(i).getLongitudine(), 2, 2, 0, 360, Arc2D.CHORD);
				g2D.setColor(Color.BLACK);
				g2D.fill(circle2);
				g2D.draw(circle2);

			}
			for (int i = 0; i < listaArce.size(); i++) {
				Nod n1 = new Nod();
				n1 = listaNoduri.get(listaArce.get(i).getFromId());
				Nod n2 = new Nod();
				n2 = listaNoduri.get(listaArce.get(i).getToId());
				listaArce.get(i).drawNode(g, n1, n2, Color.BLACK);

			}
			for (int i = 0; i < noduriVizitate.size() - 1; i++) {
				g2D.setColor(Color.RED);
				Shape circle1 = new Arc2D.Double(listaNoduri.get(noduriVizitate.get(i)).getLatitudine(),
						listaNoduri.get(noduriVizitate.get(i)).getLongitudine(), 2, 2, 0, 360, Arc2D.CHORD);
				g2D.fill(circle1);
				g2D.draw(circle1);
				g2D.draw(new Line2D.Double(listaNoduri.get(noduriVizitate.get(i)).getLatitudine(),
						listaNoduri.get(noduriVizitate.get(i)).getLongitudine(),
						listaNoduri.get(noduriVizitate.get(i + 1)).getLatitudine(),
						listaNoduri.get(noduriVizitate.get(i + 1)).getLongitudine()));

			}

		}
		for (int i = 0; i < listaNoduri.size(); i++) {

			listaNoduri.get(i).drawNode(g, coefLat, coefLong, latitudineMIN, longitudineMIN, Color.BLACK);

		}
		for (int i = 0; i < listaArce.size(); i++) {
			Nod n1 = new Nod();
			n1 = listaNoduri.get(listaArce.get(i).getFromId());
			Nod n2 = new Nod();
			n2 = listaNoduri.get(listaArce.get(i).getToId());
			listaArce.get(i).drawNode(g, n1, n2, Color.BLACK);

		}

	}

}
