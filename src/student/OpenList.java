package student;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import cz.cvut.atg.zui.astar.AbstractOpenList;

public class OpenList extends AbstractOpenList<Town>  {
	
	HashMap<Long, Town> nodes;
	HashSet<Long> visitedNodes;
	
	public OpenList(){
		super();
		nodes = new HashMap<Long,Town>();
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
		double smallestCost = Double.MAX_VALUE;
		long smallestId = -1;
		
		for (Map.Entry<Long,Town> entry: nodes.entrySet()) {
			if (entry.getValue().getFCost() < smallestCost) {
				smallestCost = entry.getValue().getFCost();
				smallestId = entry.getKey();
			}
		}
		
		return nodes.remove(smallestId);
	}

	public boolean alreadyExpanded(long nodeId) {
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
