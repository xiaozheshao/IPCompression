import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AS {
	public Set<Integer> neighbor = new HashSet<Integer>();
	private boolean visited = false;
	public int index;
	public int degree;
	public Map<Integer, Integer> transit = new HashMap<Integer, Integer>();
	// public int[] transit = new int[65535];
	public Map<Integer, Integer> edge = new HashMap<Integer, Integer>();
	// public int[] edge = new int[65535];
	public Set<AS> providers = new HashSet<AS>();

	public Set<AS> customer = new HashSet<AS>();

	public ArrayList<IPAddress> addressList = new ArrayList<IPAddress>();

	public AS(int index) {
		this.index = index;
		visited = false;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean bool) {
		visited = bool;
	}

	public String getString() {
		return "As index(" + index + ") Providers(" + providers.size()
				+ ") Customers(" + customer.size() + ") degree(" + degree
				+ ")　IPAddress(" + addressList.size() + ")";
	}

}
