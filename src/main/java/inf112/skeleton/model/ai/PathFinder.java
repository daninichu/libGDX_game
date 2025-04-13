package inf112.skeleton.model.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.model.collision.HashGrid;

import java.awt.*;
import java.util.PriorityQueue;

/**
 * Uses the A* algorithm.
 */
public class PathFinder{
    private HashGrid<Rectangle> grid;
    private PriorityQueue<Node> heap = new PriorityQueue<>();
    private ObjectSet<Point> done = new ObjectSet<>();

    public PathFinder(HashGrid<Rectangle> grid) {
        this.grid = grid;
    }

    private static class Node implements Comparable<Node>{
        Point pos;
        Node parent;
        int gCost, fCost;

        public Node(Point pos, int gCost, int hCost) {
            this.pos = pos;
            this.gCost = gCost;
            this.fCost = gCost + hCost;
        }

        @Override
        public int compareTo(Node o){
            return Integer.compare(fCost, o.fCost);
        }
    }

    private static int hCost(Point start, Point end) {
        return Math.abs(start.x - end.x) + Math.abs(start.y - end.y);
    }

    private Array<Point> getFreeAdjCells(Point cell) {
        Array<Point> freeAdjCells = new Array<>();
        Point[] adjCells = new Point[]{
            new Point(cell.x + 1, cell.y), new Point(cell.x, cell.y + 1),
            new Point(cell.x - 1, cell.y), new Point(cell.x, cell.y - 1)
        };
        for (Point adj : adjCells)
            if(!done.contains(adj) && grid.getLocalObjects(new Array<>(new Point[]{adj})).isEmpty())
                freeAdjCells.add(adj);
        return freeAdjCells;
    }

    public Queue<Point> findPath(Vector2 startPos, Vector2 goalPos) {
        return findPath(HashGrid.toCell(startPos.x, startPos.y), HashGrid.toCell(goalPos.x, goalPos.y));
    }

    public Queue<Point> findPath(Point start, Point goal) {
        heap.clear();
        done.clear();

        heap.add(new Node(start, 0, hCost(start, goal)));
        while (!heap.isEmpty()) {
            Node curr = heap.poll();
            if (curr.pos.equals(goal))
                return retracePath(curr);

            done.add(curr.pos);
            for (Point cell : getFreeAdjCells(curr.pos)) {
                Node node = new Node(cell, curr.gCost + 1, hCost(cell, goal));
                node.parent = curr;
                heap.add(node);
            }
        }
        return null;
    }

    private static Queue<Point> retracePath(Node goal) {
        Queue<Point> path = new Queue<>();
        for(Node curr = goal; curr != null; curr = curr.parent)
            path.addFirst(curr.pos);
        return path;
    }
}
