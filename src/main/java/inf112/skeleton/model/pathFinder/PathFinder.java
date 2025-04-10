package inf112.skeleton.model.pathFinder;

import inf112.skeleton.model.collision.StaticCollisionHandler;

public class PathFinder{
    private StaticCollisionHandler collisionHandler;

    public PathFinder(StaticCollisionHandler collisionHandler) {
        this.collisionHandler = collisionHandler;
    }
}
