package student;

import cz.cvut.atg.zui.astar.AbstractOpenList;
import cz.cvut.atg.zui.astar.PlannerInterface;
import cz.cvut.atg.zui.astar.RoadGraph;
import eu.superhub.wp5.planner.planningstructure.GraphEdge;
import eu.superhub.wp5.planner.planningstructure.GraphNode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Planner implements PlannerInterface {
	
	private OpenList openList; 
	
	public Planner() {
		this.openList = new OpenList();
	}

    @Override
    public List<GraphEdge> plan(RoadGraph graph, GraphNode origin, GraphNode destination) {
    	
    	Collection<GraphEdge> allEdges = graph.getAllEdges();
    	double maxSpeed = getMaxSpeed(allEdges);
    	double gCost = 0;
    	Town parentTown = null;
    	Town originTown = new Town(origin, destination, gCost, parentTown, maxSpeed);
    	openList.add(originTown);
    	
    	List<GraphEdge> path = null;
    	int maxStop = 0;
    	while ((! openList.isEmpty()) && maxStop < 900000) {
    		maxStop += 1;
    		Town currTown = openList.getSmallest();
    		openList.addExpandedTown(currTown);
    		
    		if (currTown.isDestination()) {
    			path = generatePath(graph, currTown);
    			break;
    			
    		} else {
    			addNeighboursToOpenList(graph, currTown, destination, openList, maxSpeed);
    		}
    	}
    	
    	return path;
    }

	private double getMaxSpeed(Collection<GraphEdge> allEdges) {
		double maxSpeed = 0;
		for (GraphEdge edge: allEdges) {
			if (edge.getAllowedMaxSpeedInKmph() > maxSpeed) maxSpeed = edge.getAllowedMaxSpeedInKmph();
		}
		return maxSpeed;
	}

	private List<GraphEdge> generatePath(RoadGraph graph, Town currTown) {
		List<GraphEdge> path = new ArrayList<GraphEdge>();
		while (currTown.getParent() != null) {
			GraphEdge edge = graph.getEdge(currTown.getParent().getId(), currTown.getId());

			path.add(edge);
			currTown = currTown.getParent();
		}
		Collections.reverse(path); // One directional graph
		return path;
	}

	private void addNeighboursToOpenList(RoadGraph graph, Town currTown, GraphNode destination,
			OpenList openList, double maxSpeed) {
		long townId = currTown.getId();
		List<GraphEdge> currTownEdges = graph.getNodeOutcomingEdges(townId);
		
		if (currTownEdges != null) {
			for (GraphEdge edge: currTownEdges) {
				double gCost = calcGCost(edge, currTown);
				long nodeId = edge.getToNodeId();
				GraphNode node = graph.getNodeByNodeId(nodeId);

				if (openList.alreadyExpanded(nodeId)) {
					continue;
				}
				
				Town town = new Town(node, destination, gCost, currTown, maxSpeed);
				
				Town existingTown = openList.getExistingTown(nodeId); // Checks if the town already exists in the openList
				if (existingTown == null) {
					openList.add(town);
				} else {
					if (town.getFCost() < existingTown.getFCost()) {
						openList.remove(existingTown.getId());
						openList.addItem(town); // Doesn't increase the counter
					}
				}
			}
		}
	}

    private double calcGCost(GraphEdge edge, Town currTown) {
		double kmph = edge.getAllowedMaxSpeedInKmph();
		double km = edge.getLengthInMetres() / 1000;
		
		return (km / kmph) + currTown.getGCost();
	}

	@Override
    public AbstractOpenList<Town> getOpenList() {
        return openList;
    }
}