package student;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import cz.cvut.atg.zui.astar.AbstractOpenList;

public class OpenList extends AbstractOpenList<Town>  {
	
	TreeMap<Long,Town> nodes;
	HashSet<Long> visitedNodes;
	
	public OpenList(){
		super();
		nodes = new TreeMap<Long,Town>();
		visitedNodes = new HashSet<Long>();
	}

	@Override
	protected boolean addItem(Town item) {
		nodes.put(item.getId(), item);
		return true;
	}

	public boolean isEmpty() {
		return nodes.isEmpty();
	}

	public Town getSmallest() {
		Entry<Long, Town> smallest = nodes.pollFirstEntry();
		return smallest.getValue();
	}
	
	public void printFCosts() {
		System.out.println("The fcosts in open list are");
		for (Map.Entry<Long,Town> entry: nodes.entrySet()) {
			Town town = entry.getValue();
			town.printFCost();
		}
	}

	public boolean alreadyVisited(long nodeId) {
		return visitedNodes.contains(nodeId);
	}

	public Town getExistingTown(long nodeId) {
		return nodes.get(nodeId);
	}

	public void remove(long nodeId) {
		nodes.remove(nodeId);
	}

	public void addExpandedTown(Town currTown) {
		visitedNodes.add(currTown.getId());
	}

	
}
