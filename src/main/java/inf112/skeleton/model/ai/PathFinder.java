package inf112.skeleton.model.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.model.collision.CollisionHandler;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.util.Box;

import java.awt.*;
import java.util.PriorityQueue;

/**
 * Uses the A* algorithm.
 */
public class PathFinder{
    private static final int MAX_SEARCH_BOUND = 400;
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

    private static Point[] adjCells(Point cell) {
        return new Point[]{
            new Point(cell.x + 1, cell.y), new Point(cell.x, cell.y + 1),
            new Point(cell.x - 1, cell.y), new Point(cell.x, cell.y - 1)
        };
    }

    public Queue<Point> findPath(Vector2 startPos, Vector2 goalPos) {
        return findPath(HashGrid.toCell(startPos.x, startPos.y), HashGrid.toCell(goalPos.x, goalPos.y));
    }

    public Queue<Point> findPath(Point start, Point goal) {
        done.clear();
        heap.clear();
        heap.add(new Node(start, 0, hCost(start, goal)));
        int i = 0;
        while (!heap.isEmpty() && i++ < MAX_SEARCH_BOUND) {
            Node curr = heap.poll();
            if(!done.add(curr.pos))
                continue;
            for (Point adj : adjCells(curr.pos)) {
                Node node = new Node(adj, curr.gCost + 1, hCost(adj, goal));
                node.parent = curr;
                if (node.pos.equals(goal))
                    return retracePath(node);

                ObjectSet<Rectangle> boxes = grid.getLocalObjects(adj);
                if(!done.contains(adj) && !CollisionHandler.collidesAny(Box.cell(adj), boxes))
                    heap.add(node);
            }
        }
        return new Queue<>();
    }

    private static Queue<Point> retracePath(Node goal) {
        Queue<Point> path = new Queue<>();
        for(Node curr = goal; curr != null; curr = curr.parent)
            path.addFirst(curr.pos);
        return path;
    }
}
