package inf112.skeleton.model.ai;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BinaryHeap;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Queue;
import inf112.skeleton.model.collision.CollisionHandler;
import inf112.skeleton.model.collision.HashGrid;
import inf112.skeleton.util.Box;

import java.awt.*;

/**
 * Uses the A* algorithm.
 */
public class Pathfinder{
    private static final int MAX_SEARCH_BOUND = 400;
    private HashGrid<Rectangle> grid;
    private BinaryHeap<Node> heap = new BinaryHeap<>();
    private ObjectSet<Point> done = new ObjectSet<>();

    public Pathfinder(HashGrid<Rectangle> grid) {
        this.grid = grid;
    }

    private static class Node extends BinaryHeap.Node{
        Point pos;
        Node parent;
        int gCost;

        public Node(Point pos, int gCost, int hCost) {
            super(gCost + hCost);
            this.pos = pos;
            this.gCost = gCost;
        }
    }

    private static int hCost(Point p1, Point p2) {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
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
            Node curr = heap.pop();
            if(done.add(curr.pos))
                for(Point adj : adjCells(curr.pos)){
                    Node node = new Node(adj, curr.gCost + 1, hCost(adj, goal));
                    node.parent = curr;
                    if(adj.equals(goal))
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
