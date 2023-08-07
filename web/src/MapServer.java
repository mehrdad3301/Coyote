import com.routerunner.algorithms.Dijkstra;
import com.routerunner.geo.Point;
import com.routerunner.graph.Graph;
import com.routerunner.graph.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Sample Server class.
 * */
public class MapServer {

  private final int port ;
  private final Graph graph ;

  private final ServerSocket server ;

  public MapServer(int port, String address) throws Exception {
    this.port = port ;
    server = new ServerSocket(port);
    System.out.printf("building graph for %s data\n", address);
    graph = Graph.buildFromOSM(address) ;
    System.out.println("reducing graph to largest connected components\n");
    graph.reduceToLargestConnectedComponent();
  }

  private Point[] parseRequest(String request) {

    System.out.println("Request string is \""
            + (request.length() < 99 ? request : request.substring(0, 97) + "...")
            + "\"");
    // Extract coordinates from GET request string.
    int pos = request.indexOf("?");
    if (pos != -1) {
      request = request.substring(5, pos);
    }
    String[] parts = request.split(",");
    float sourceLat = Float.parseFloat(parts[0]);
    float sourceLng = Float.parseFloat(parts[1]);
    float targetLat = Float.parseFloat(parts[2]);
    float targetLng = Float.parseFloat(parts[3]);
    Point[] points = new Point[2] ;
    points[0] = new Point(sourceLat, sourceLng) ;
    points[1] = new Point(targetLat, targetLng) ;
    return points ;
  }

  private String getResponse(Path path) {

    StringBuilder jsonp = new StringBuilder("redrawLineServerCallback({\n") ;
    jsonp.append("  path: [") ;
    for (Path.Intersection intersection: path.getPath()) {
      // Send JSONP results string back to client.
      jsonp.append("[ ").append(intersection.node.getLat())
              .append(",").append(intersection.node.getLon())
              .append("]").append(",") ;
    }
    //deleting the last comma
    jsonp.deleteCharAt(jsonp.length() - 1) ;
    jsonp.append("]\n" + "})\n");

      return "HTTP/1.0 200 OK\r\n"
            + "Content-Length: " + jsonp.length() + "\r\n"
            + "Content-Type: application/javascript" + "\r\n"
            + "Connection: close\r\n"
            + "\r\n" + jsonp;
  }
  private void serve() throws IOException {

    Dijkstra dijkstra = new Dijkstra(graph) ;
    BufferedReader in = null;
    PrintWriter out = null;

    int i = 0;
    while (true) {
      System.out.println("\u001b[1m\u001b[34m[" + (++i) + "] "
              + "Waiting for query on port " + port + " ...\u001b[0m");
      Socket client = server.accept();
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));

      try {
        // Get the request string.
        String request = in.readLine();
        Point[] points = parseRequest(request);
        Path path = dijkstra.computeShortestPath(points[0], points[1]);
        String response = getResponse(path);
        out = new PrintWriter(client.getOutputStream(), true);
        out.println(response);
        out.close();
      } catch (Exception e) {
        System.out.println(e);
      }
      in.close();
      client.close();
    }


  }

  public static void main(String[] args) throws Exception {

    // parse command line arguments
    int port = 8888;
    String fileAddress = "";
    if (args.length != 4) {
      System.out.println("invalid arguments provided");
      return;
    }

    for (int i = 0; i < args.length; i++) {
      if ("-f".equals(args[i])) {
        fileAddress = args[++i];
      } else if ("-p".equals(args[i])) {
        port = Integer.parseInt(args[++i]);
      }
    }

    MapServer server = new MapServer(port, fileAddress) ;
    server.serve() ;
  }
}
