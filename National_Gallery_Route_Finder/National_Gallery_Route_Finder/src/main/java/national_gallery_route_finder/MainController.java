package national_gallery_route_finder;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.*;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainController {
    Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/national_gallery_route_finder/floorplan-level-2-july-2020.jpg")), 600, 600, false, false);

    @FXML public ImageView imageView;
    @FXML public ComboBox<String> start;
    @FXML public ComboBox<String> end;
    @FXML public ComboBox<String> avoid;
    @FXML public AnchorPane anchorPane;
    private List<Room> rooms;
    private HashMap<String, Node<Room>> roomsHashMap;
    private List<String> names;
    private List<Node<Room>> roomNodes;
    private List<Node<Room>> avoidRooms;

    public void initialize() {
        imageView.setImage(image);
        this.rooms = new LinkedList<>();
        this.names = new LinkedList<>();
        this.roomNodes = new LinkedList<>();
        this.roomsHashMap = new HashMap<>();
        this.avoidRooms = new LinkedList<>();
        read();
        connectRooms();
        end.getItems().addAll(names);
        start.getItems().addAll(names);
        avoid.getItems().addAll(names);
    }

    private void read() {
        String line;
        try {
            File file = new File("C:\\Users\\volga\\IdeaProjects\\National_Gallery_Route_Finder\\National_Gallery_Route_Finder\\src\\main\\resources\\national_gallery_route_finder\\mappings.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Room room = new Room(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                Node<Room> node = new Node<>(room);
                roomNodes.add(node);
                roomsHashMap.put(values[0], node);
                names.add(values[0]);
            }
        } catch(Exception e){
            System.err.println("Error " + e);
        }
    }

    public void connectNodes (String node1, String node2){
        Node<Room> roomA = roomsHashMap.get(node1);
        Node<Room> roomB = roomsHashMap.get(node2);
        roomA.connectToNodeUndirected(roomB, 1);
    }

    private void connectRooms () {
        String line = "";
        try {
            File file = new File("C:\\Users\\volga\\IdeaProjects\\National_Gallery_Route_Finder\\National_Gallery_Route_Finder\\src\\main\\resources\\national_gallery_route_finder\\room-links.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                connectNodes(values[0], values[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Node<Room> getNode(String roomName) {
        return roomsHashMap.get(roomName);
    }

    public void depthFirstSearchAll() {
        clearDrawnPaths();
        List<List<Node<?>>> paths = Algorithms.findAllPathsDepthFirst(getNode(start.getValue()), null, getNode(end.getValue()).data);
        int counter = 0;
        for (List<Node<?>> list : paths) {
            if (counter >= 50) break;
            for (Node<?> node : list) {
                Room room = (Room) node.data;
                Circle dot = new Circle(room.getXCoord(), room.getYCoord(), 10);
                dot.setStroke(Color.RED);
                dot.setFill(Color.TRANSPARENT);
                ((Pane) imageView.getParent()).getChildren().add(dot);
            }
            counter++;
        }
        drawPoints(start.getValue(), end.getValue());
        System.out.println("Total paths: " + paths.size());
    }

    public void depthFirstSearchCheapest() {
        List<Node<?>> newPath;
        Cost pathCost = Algorithms.searchGraphDepthFirstCheapestPath(getNode(start.getValue()), null, 0, getNode(end.getValue()).data);
        assert pathCost != null;
        newPath = pathCost.pathList;
        drawPath(newPath, Color.YELLOW);
    }

    public void dijkstra() {
        List<Node<?>> pathList;
        Cost costOfPath = Algorithms.findCheapestPathDijkstra(getNode(start.getValue()), getNode(end.getValue()).data);
        assert costOfPath != null;
        pathList = costOfPath.pathList;
        drawPath(pathList, Color.BLUE);
        imageView.setImage(image);
    }

    public void drawPath(List<Node<?>> pathList, Color c) {
        clearDrawnPaths();
        for(int i = 0; i < pathList.size(); i++) {
            Node<Room> startNode = (Node<Room>) pathList.get(i);
            if (i + 1 < pathList.size()) {
                Node<Room> nextNode = (Node<Room>) pathList.get(i + 1);
                Line l = new Line(startNode.data.getXCoord(), startNode.data.getYCoord(), nextNode.data.getXCoord(), nextNode.data.getYCoord());
                l.setFill(c);
                l.setStroke(c);
                l.setStrokeWidth(5);
                anchorPane.getChildren().add(l);
            }
        }
        drawPoints(start.getValue(), end.getValue());
    }

    public void avoidRoom(String room) {
        for (Node<Room> n : roomNodes) {
            for (Link l : n.adjList) {
                Node<Room> r = (Node<Room>) l.destNode;
                if (n.data.getRoomName().equals(room) || r.data.getRoomName().equals(room)) {
                    l.cost = 1000; //makes the cost of the room not worth going through
                    avoidRooms.add(r);
                }
            }
        }
    }

    public void avoidThisRoom() {
        if (avoidRooms.contains(getNode(avoid.getValue()))) return;
        avoidRoom(avoid.getValue());
        System.out.println(avoid.getValue());
    }

    public void drawPoints(String start, String end) {
        Circle startPoint = new Circle(getNode(start).data.getXCoord(), getNode(start).data.getYCoord(), 5);
        Circle endPoint = new Circle(getNode(end).data.getXCoord(), getNode(end).data.getYCoord(), 5);
        startPoint.setFill(Color.GREEN);
        endPoint.setFill(Color.RED);
        ((Pane) imageView.getParent()).getChildren().add(startPoint);
        ((Pane) imageView.getParent()).getChildren().add(endPoint);
    }

    public void clearDrawnPaths() {
        ((Pane) imageView.getParent()).getChildren().removeIf(r -> r instanceof Circle);
        ((Pane) imageView.getParent()).getChildren().removeIf(r -> r instanceof Line);
    }
}