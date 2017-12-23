package pacman.game.internal;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class PathTree implements Iterable<PathTree> {
    /**
     * parent is the parent node from a tree, the root has null parent and null next
     */
    private PathTree parent;
    /**
     * the next is the next sibling node in same level
     */
    private PathTree next;

    public final PathStatics statics;

    public final JunctionData junctionData;

    /**
     * the firstChild of a tree node, could be null
     */
    private PathTree firstChild;

    public PathTree getParent() {
        return parent;
    }

    public PathTree getNext() {
        return next;
    }

    public PathTree getFirstChild() {
        return firstChild;
    }

    public PathTree(PathStatics statics, JunctionData junctionData){
        this.statics = statics;
        this.junctionData = junctionData;
    }

    /**
     * @param tobeChild the node need to become child for the parent,
     *                  the node will become the first child for the parent, other children become the
     *                  next of the new added node
     */
    public void addChild(PathTree tobeChild){
        if(tobeChild.parent != null) throw new AssertionError("tobeChild should not have parent");
        if(tobeChild.next != null) throw new AssertionError("tobeChild should not have siblings");
        tobeChild.parent = this;
        tobeChild.next = this.firstChild;
        this.firstChild = tobeChild;
    }

    @Override
    public Iterator<PathTree> iterator() {
        return new Iterator<PathTree>() {
            PathTree currentNode = firstChild;
            @Override
            public boolean hasNext() {
                return currentNode != null;
            }

            @Override
            public PathTree next() {
                PathTree result = currentNode;
                if(currentNode == null) throw new NoSuchElementException();
                currentNode = currentNode.next;
                return result;
            }
        };
    }
}
