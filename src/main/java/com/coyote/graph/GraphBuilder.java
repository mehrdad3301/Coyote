package com.coyote.graph;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
GraphBuilder builds the graph by parsing Open Street Map xml data
 */
public class GraphBuilder {
    
    private final XMLStreamReader streamReader ;
    private final Graph graph ;

    /* idMapping is a mapping from OSM id to graph node id */
    private final HashMap<Long, Integer> idMapping ;
    public GraphBuilder(String fileAddress) throws Exception {
        graph = new Graph() ;
        idMapping = new HashMap<>() ;
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in = new FileInputStream(fileAddress);
        streamReader = inputFactory.createXMLStreamReader(in);
        streamReader.nextTag(); // Skip root element
    }

    /**
     * OSM has first <node> tags and then <way> tags. buildGraph
     * first parses nodes and adds them to the underlying graph.
     * it will then parse ways and add arcs to adjacencyList.
     *
     * @return the built graph
     */
    public Graph buildGraph() throws XMLStreamException {
        parseNodes() ;
        parseWays() ;
        return graph;
    }

    private void parseNodes() throws XMLStreamException {
        while (streamReader.hasNext()) {
            streamReader.next() ;
            if (!streamReader.isStartElement())
                continue;
            if (isNode()) {
                Node node = getNode();
                idMapping.put(node.osmId, graph.numNodes);
                graph.addNode(node) ;
            } else if (isWay()) {
                // end of nodes, return
                return;
            }
        }
    }

    private void parseWays() throws XMLStreamException {
        // take first way. this is because it's immediately called after parseNodes
        Way way = getWay() ;
        if (way != null)
            addArcs(way);
        while (streamReader.hasNext()) {
            streamReader.next() ;
            if (!streamReader.isStartElement())
                continue;
            if(isRelation())
                return ;
            way = getWay();
            if (way != null) {
                addArcs(way);
            }
        }
    }


    private Way getWay() throws XMLStreamException {
        ArrayList<Integer> nodesIds = new ArrayList<>() ;
        HighWay type = null ;
        while (streamReader.hasNext() && !(streamReader.isEndElement() && isWay())) {
            streamReader.next();
            if (streamReader.isStartElement()){
                if (isNodeReference()) {
                    long osmId = Long.parseLong(streamReader.getAttributeValue(null, "ref"));
                    nodesIds.add(idMapping.get(osmId));
                } else if (isTagElement()) {
                    if (isHighWayTag()) {
                        type = getHighWay() ;
                    }
                }
            }
        }
        if (type == null) {
            return null ;
        }
        return new Way(nodesIds, type) ;
    }

    private void addArcs(Way way) {
        for (int i = 0; i < way.nodesIds.size() - 1; i++) {
            int sourceNodeId = way.nodesIds.get(i);
            int targetNodeId = way.nodesIds.get(i + 1);
            while (graph.adjacencyList.size() <= sourceNodeId ||
                    graph.adjacencyList.size() <= targetNodeId) {
                graph.adjacencyList.add(new ArrayList<>());
            }
            int cost = way.getCost(graph.nodes.get(sourceNodeId), graph.nodes.get(targetNodeId)) ;
            graph.addEdge(sourceNodeId, targetNodeId, cost) ;
        }
    }

    private Node getNode() {
        long osmId = Long.parseLong(streamReader.getAttributeValue(null, "id"));
        float lat = Float.parseFloat(streamReader.getAttributeValue(null, "lat"));
        float lon = Float.parseFloat(streamReader.getAttributeValue(null, "lon"));
        return new Node(osmId, lat, lon) ;
    }

    private HighWay getHighWay() {
        String highwayName = streamReader.getAttributeValue(null, "v");
        return HighWay.getByName(highwayName) ;
    }

    private boolean isNode() {
        return ("node".equals(streamReader.getLocalName())) ;
    }

    private boolean isWay() {
        return ("way".equals(streamReader.getLocalName())) ;
    }

    private boolean isNodeReference() {
        return ("nd".equals(streamReader.getLocalName())) ;
    }

    private boolean isTagElement() {
        return ("tag".equals(streamReader.getLocalName())) ;
    }

    private boolean isHighWayTag() {
        String key = streamReader.getAttributeValue(null, "k");
        return "highway".equals(key) ;
    }

    private boolean isRelation() {
        String key = streamReader.getAttributeValue(null, "k");
        return "relation".equals(key);
    }

}
