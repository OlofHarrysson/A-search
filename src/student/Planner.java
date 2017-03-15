package student;

import cz.cvut.atg.zui.astar.AbstractOpenList;
import cz.cvut.atg.zui.astar.PlannerInterface;
import cz.cvut.atg.zui.astar.RoadGraph;
import eu.superhub.wp5.planner.planningstructure.GraphEdge;
import eu.superhub.wp5.planner.planningstructure.GraphNode;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public class Planner implements PlannerInterface {
	
	private OpenList openList;
	
	public Planner() {
		this.openList = new OpenList();
	}

    @Override
    public List<GraphEdge> plan(RoadGraph graph, GraphNode origin, GraphNode destination) {
    	
    	double gCost = 0;
    	Town originTown = new Town(origin, destination, gCost);
    	openList.add(originTown);
    	
    	int maxStop = 0;
    	while ((! openList.isEmpty()) && maxStop < 9000000) {
    		maxStop += 1;
//    		System.out.println(maxStop);
//    		openList.printFCosts();
    		Town currTown = openList.getSmallest();
    		openList.addExpandedTown(currTown);
//    		System.out.println(currTown.getFCost());
    		
    		if (currTown.isDestination()) {
    			System.out.println("Current town is destination town");
    			System.exit(0); // TODO: Change
    		} else {
    			addNeighboursToOpenList(graph, currTown, destination, openList);
    		}
    	}
    	
    	System.out.println("Finished");
    	
    	
    	
        throw new NotImplementedException();
    }

	private void addNeighboursToOpenList(RoadGraph graph, Town currTown, GraphNode destination,
			OpenList openList) {
		long townId = currTown.getId();
		List<GraphEdge> currTownEdges = graph.getNodeOutcomingEdges(townId);
		
		if (currTownEdges != null) {
			for (GraphEdge edge: currTownEdges) {
				double gCost = calcGCost(edge, currTown);
				long nodeId = edge.getToNodeId();
				GraphNode node = graph.getNodeByNodeId(nodeId);
				
				if (openList.alreadyVisited(nodeId)) { // TODO: Already expanded. Correctly implemented? In openList poll instead? Should this method check here?
					continue;
				}
				
				Town town = new Town(node, destination, gCost);
				
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