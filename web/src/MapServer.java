import com.routerunner.algorithms.Dijkstra;
import com.routerunner.geo.Point;
import com.routerunner.graph.Graph;
import com.routerunner.graph.Path;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * Sample Server class.
 * */
public class MapServer {

  private final int port ;
  private final Dijkstra dijkstra;

  private final ServerSocket server ;

  public MapServer(int port, String address) throws Exception {
    this.port = port ;
    server = new ServerSocket(port);
    System.out.printf("\u001b[2;32mBUILDING GRAPH FOR %s DATA\n", address);
    Graph graph = Graph.buildFromOSM(address);
    System.out.println("REDUCING THE GRAPH TO LARGEST CONNECTED COMPONENTS\u001b[0m\n");
    graph.reduceToLargestConnectedComponent();
    dijkstra = new Dijkstra(graph);
  }
  private String getHeaders(JSONObject json) {
      // Prepare the headers
      return "HTTP/1.1 200 OK\r\n" +
      "Content-Type: application/json\r\n" +
      "Content-Length: " + json.toString().getBytes(StandardCharsets.UTF_8).length + "\r\n" +
      "Connection: Keep-Alive\r\n" +
      "Access-Control-Allow-Origin : *\r\n" + "\r\n";
    }
  private Point[] parsePoints(String request) {
    System.out.println("Request string is \"\u001b[2;32m"
            + (request.length() < 99 ? request : request.substring(0, 97) + "...")
            + "\"\u001b[0m") ;
    // Extract coordinates from GET request string.
    request = request.split("\\?")[0].substring(5) ;
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
  private String getSettledPoints() {
    StringBuilder points = new StringBuilder("[");
    for (int i = 0; i < dijkstra.getNumVisitedNodes(); i++) {
      if (dijkstra.getVisited().get(i) != 0) {
        // Send JSONP results string back to client.
        points.append("[ ").append(dijkstra.graph.getNode(i).getLat())
                .append(",").append(dijkstra.graph.getNode(i).getLon())
                .append("]").append(",");
      }
    }
    //deleting the last comma
    points.deleteCharAt(points.length() - 1);
    points.append("]");
    return points.toString() ;
  }

  private String getPath(Point[] points) {
    Path path = dijkstra.computeShortestPath(points[0], points[1]);
    StringBuilder pathString = new StringBuilder("[");
    for (Path.Intersection intersection: path.getPath()) {
      // Send JSONP results string back to client.
      pathString.append("[ ").append(intersection.node.getLat())
              .append(",").append(intersection.node.getLon())
              .append("]").append(",") ;
    }
    //deleting the last comma
    pathString.deleteCharAt(pathString.length() - 1) ;
    pathString.append("]") ;

    return pathString.toString() ;
  }
  private JSONObject getResponse(Point[] points, boolean debug) {
    JSONObject json = new JSONObject() ;
    json.put("path", getPath(points)) ;
    if (debug) {
      json.put("points", getSettledPoints());
    }
    return json ;
  }
  public static String getParam(String request, String param) {
    String[] parts = request.split("\\?");
    if (parts.length > 1) {
      String queryParams = parts[1];
      String[] paramPairs = queryParams.split(" ")[0].split("&");
      for (String paramPair : paramPairs) {
        String[] keyValue = paramPair.split("=");
        if (keyValue.length == 2) {
          String key = keyValue[0];
          String value = keyValue[1];
          if (key.equals(param)) {
            return value;
          }
        }
      }
    }
    return null;
  }
  private void serve() throws IOException {

    BufferedReader in ;

    int i = 0;
    while (true) {
      System.out.println("\u001b[1m\u001b[34m[" + (++i) + "] "
              + "Waiting for query on port " + port + " ...\u001b[0m");

      Socket client = server.accept();
      in = new BufferedReader(new InputStreamReader(client.getInputStream()));

      try {
        // Get the request string.
        String request = in.readLine();
        boolean isDebug = Objects.equals(getParam(request, "debug"), "true");
        Point[] points = parsePoints(request);
        JSONObject json = getResponse(points, isDebug) ;
        String headers = getHeaders(json) ;
        OutputStreamWriter out = new OutputStreamWriter(
                client.getOutputStream(), StandardCharsets.UTF_8);
          out.write(headers);
          out.write(json.toString());
          out.flush() ;
          out.close() ;
      } catch (Exception e) {
        System.out.println("\u001b[1;31m" + e + "\u001b[0m");
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
      System.out.println("\u001b[1;31minvalid arguments provided\u001b[0m");
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
