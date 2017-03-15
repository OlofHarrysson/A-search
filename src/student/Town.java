package student;

import cz.cvut.atg.zui.astar.Utils;
import eu.superhub.wp5.planner.planningstructure.GraphNode;

public class Town implements Comparable<Town> {
	
	private double gCost;
	private double hCost;
	private double fCost;
	private GraphNode location;
	private GraphNode destination;
	
	public Town(GraphNode location, GraphNode destination, double gCost) {
		this.location = location;
		this.destination = destination;
		this.hCost = Utils.distanceInKM(location, destination);
		this.gCost = gCost; // TODO: change
		this.fCost = gCost + hCost;
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

	



}
