# Dijkstra-BellmanFord-Luxembourg-Map
The application of this algorithm will be done on the map of Luxembourg, the one given by the xml file.

The application will have to visually display the map in a window. The start and end points of the road will be selected by mouse click in 2 different areas of the map. Because there are many nodes / arcs represented graphically, a node will be chosen as the closest to the point where we clicked (you will calculate the distance from the coordinates of the point where you clicked to the coordinates of the node).

To choose between Dijkstra or Bellman-Ford, you will be able to enter the algorithm option in the console.
After the execution of the chosen algorithm, the minimum path between the 2 selected nodes will be displayed in red.
The time to load the graph from the file and display it in the window should be about one second.
Also, the execution time of the minimum path algorithm and the resulting path coloring should be about one second.
