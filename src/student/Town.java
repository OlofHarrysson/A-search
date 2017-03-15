package student;

import cz.cvut.atg.zui.astar.Utils;
import eu.superhub.wp5.planner.planningstructure.GraphNode;

public class Town implements Comparable<Town> {
	
	private double gCost;
	private double hCost;
	private double fCost;
	private GraphNode location;
	private GraphNode destination;
	private Town parentTown;
	
	public Town(GraphNode location, GraphNode destination, double gCost, Town parentTown, double maxSpeed) {
		this.location = location;
		this.destination = destination;
		this.hCost = Utils.distanceInKM(location, destination) / maxSpeed; // Maxspeed of any road is used so hCost is never overestimated
		this.gCost = gCost; // TODO: change
		this.fCost = gCost + hCost;
		this.parentTown = parentTown;
	}

	public boolean isDestination() {
		return location.getId() == destination.getId();
	}
	
	public long getId() {
		return location.getId();
	}
	
	public double getFCost() {
		return this.fCost;
	}
	
	public double getGCost() {
		return this.gCost;
	}

	@Override
	public int compareTo(Town o) {
		return Double.compare(this.getFCost(), o.getFCost());
	}
	
	public void printFCost() {
		System.out.println(fCost);
	}

	public Town getParent() {
		return this.parentTown;
	}

	public void setParent(Town town) {
		this.parentTown = town;
	}

}
