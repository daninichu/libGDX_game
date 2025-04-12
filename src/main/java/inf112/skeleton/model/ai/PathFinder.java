package inf112.skeleton.model.ai;

import com.badlogic.gdx.math.Rectangle;
import java.util.PriorityQueue;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.model.collision.StaticCollisionHandler;

import java.awt.*;

public class PathFinder{
    private HashGrid<Rectangle> grid;

    public PathFinder(HashGrid<Rectangle> grid) {
        this.grid = grid;
    }

    private static int heuristic(Point start, Point end) {
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }

    private Array<Point> getFreeAdjCells(Point cell) {
        Array<Point> freeAdjCells = new Array<>();
        Point[] adjCells = new Point[]{
            new Point(cell.x + 1, cell.y),
            new Point(cell.x - 1, cell.y),
            new Point(cell.x, cell.y + 1),
            new Point(cell.x, cell.y - 1)
        };
        for (Point adj : adjCells)
            if(grid.getLocalObjects(new Array<>(new Point[]{adj})).isEmpty())
                freeAdjCells.add(adj);
        return freeAdjCells;
    }

    public Queue<Point> findPath(Point start, Point goal) {
        PriorityQueue<Node> heap = new PriorityQueue<>();
        ObjectSet<Point> visited = new ObjectSet<>();

        Node startNode = new Node(start, 0, heuristic(start, goal));
        heap.add(startNode);

        while (!heap.isEmpty()) {
            Node current = heap.poll();
            if (current.pos.equals(goal))
                return retracePath(current);

            visited.add(current.pos);
            for (Point adj : getFreeAdjCells(current.pos)) {
                if (visited.contains(adj))
                    continue;
                Node adjNode = new Node(adj, current.gCost + 1, heuristic(adj, goal));
                adjNode.parent = current;

                heap.add(adjNode);
                if(heap.contains(adjNode)){
                    System.out.println("Found adjacent path");
                }
            }
        }
        return null;
    }

    private static Queue<Point> retracePath(Node goal) {
        Queue<Point> path = new Queue<>();
        for(Node current = goal; current != null; current = current.parent)
            path.addFirst(current.pos);
        return path;
    }

    private static class Node implements Comparable<Node>{
        public Point pos;
        public Node parent;
        public int gCost;
        public int hCost;
        public int fCost;

        public Node(Point pos, int gCost, int hCost) {
            this.pos = pos;
            this.gCost = gCost;
            this.hCost = hCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public int compareTo(Node o){
            return Integer.compare(fCost, o.fCost);
        }
    }
//
//    public static void main(String[] args){
//        Array<Rectangle> r = new Array<>(
//            new Rectangle[]{
//                new Rectangle(20, -50, 16, 200),
//                new Rectangle(-20, -50, 16, 200),
//                new Rectangle(-20, -50, 40, 16),
//                new Rectangle(-20, 50, 40, 16),
//            }
//        );
//        PathFinder pathFinder = new PathFinder(new StaticCollisionHandler(r));
//        Queue<Point> path = pathFinder.findPath(new Point(0, 0), new Point(5, 0));
//        System.out.println(path);
//    }
}
