import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Logger;

public class InferTopology {

	private static int L = 1;

	public Map<Integer, AS> asList = new HashMap<Integer, AS>();

	public AS root = new AS(0);

	private RTPath p = null;

	public long prefixLength = 0;

	private static Logger logger = Logger.getLogger("Main Method");

	public int[] frequency = null;

	public ArrayList<IPAddress> IPList = new ArrayList<IPAddress>();

	public static void main(String[] args) throws IOException {
		logger.info("Start the infer.");
		InferTopology it = new InferTopology(
				"/home/shaoxiaozhe/WORKSPACE/PrepairRT/RTPath");
		int bitLength = 8;
		it.infer(bitLength);
		// it.outAS();
		System.out.println();
		logger.info("End the infer.");
	}

	public void outAS() {
		for (AS a : asList.values()) {
			System.out.println(a.getString());
		}
	}

	public InferTopology(String fileName) throws IOException {
		p = new RTPath(fileName, asList);
		System.out.println("ASList size:" + asList.size());
	}

	public int[] infer(int length) {
		System.out.println("1");
		computeDegree();
		System.out.println("2");
		computeTransit();
		System.out.println("3");
		computeRelationships();
		System.out.println("4");
		buildTopology();
		System.out.println("5");
		rearrangeIP();
		System.out.println("prefixLength:" + prefixLength);
		checkRelations();
		statistics(length);
		return frequency;
	}

	public List<IPAddress> infer_variable() {
		//logger.info("1");
		computeDegree();
		//logger.info("2");
		computeTransit();
		//logger.info("3");
		computeRelationships();
		//logger.info("4");
		buildTopology();
		//logger.info("5");
		rearrangeIP();
		//logger.info("prefixLength:" + prefixLength);
		checkRelations();
		variable_statistics();
		return IPList;
	}

	private void variable_statistics() {
		Queue<AS> q = new LinkedList<AS>();
		q.offer(root);
		int longestAddress = 0;
		int sumLength = 0;
		while (!q.isEmpty()) {
			AS a = q.poll();
			if (!a.isVisited()) {
				a.setVisited(true);
				int i = 0;
				for (AS customer : a.customer) {
					if (customer.customer.isEmpty()) {
						i++;
					} else {
						q.offer(customer);
					}
				}
				if (i > 0) {
					for (IPAddress ip : a.addressList) {
						sumLength = sumLength + ip.size();
						if (ip.size() > longestAddress) {
							longestAddress = ip.size();
						}
						ip.setFrequency(i);
						IPList.add(ip);
					}
				}
			}
		}
		logger.info("LongestLength:" + longestAddress);
		logger.info("sumLength:" + sumLength);
	}

	public void preStatistics(int length) {
		frequency = new int[(int) Math.pow(2, (length + 1))];
		for (int i = 0; i < frequency.length; i++) {
			frequency[i] = 0;
		}
	}

	public void statistics(int length) {
		preStatistics(length);
		Queue<AS> q = new LinkedList<AS>();
		q.add(root);
		AS node = null;
		int i = 0;
		int longestAddress = 0;
		while (!q.isEmpty()) {
			node = q.poll();
			if (!node.isVisited()) {
				i++;
				for (IPAddress address : node.addressList) {
					if (address.size() > longestAddress) {
						longestAddress = address.size();
					}
					countAddress(address, length);
				}
				node.setVisited(true);
				for (AS customer : node.customer) {
					if (!customer.isVisited()) {
						q.add(customer);
					}
				}
			}
		}
		logger.info("LongestAddress:" + longestAddress);
		logger.info("Output AS:" + i);
	}

	private void countAddress(IPAddress address, int length) {
		boolean[] temp = new boolean[length];
		boolean[] allBits = address.bitAddress;
		for (int i = 0; i < address.size(); i++) {
			temp[i % length] = allBits[i];
			if (i % length == length - 1) {
				setFrequency(temp, length - 1);
			}
		}
		if ((address.size() - 1) % length != length - 1) {
			setFrequency(temp, (address.size() - 1) % length);
		}
	}

	public void setFrequency(boolean[] input, int bitNum) {
		int n = 1;
		for (int i = 0; i <= bitNum; i++) {
			if (input[bitNum - i] == false) {
				n = n << 1;
			} else {
				n = (n << 1) + 1;
			}
		}
		frequency[n]++;
	}

	public void checkRelations() {
		for (AS a : asList.values()) {
			// System.out.println("Checking AS:" + a.index);
			for (AS provider : a.providers) {
				if (!provider.customer.contains(a)) {
					System.out.println("missing provider AS:" + a.index + " & "
							+ provider.index);
				}
				if (provider.providers.contains(a)) {
					System.out.println("error provider AS:" + a.index + " & "
							+ provider.index);
				}
			}
			for (AS customer : a.customer) {
				if (!customer.providers.contains(a)) {
					System.out.println("missing customer AS:" + a.index + " & "
							+ customer.index);
				}
				if (customer.customer.contains(a)) {
					System.out.println("erro customer AS:" + a.index + " & "
							+ customer.index);
				}
			}
		}
	}

	public static int log2X(int bits)// returns 0 for bits=0
	{
		bits--;
		int log = 0;
		if ((bits & 0xffff0000) != 0) {
			bits >>>= 16;
			log = 16;
		}
		if (bits >= 256) {
			bits >>>= 8;
			log += 8;
		}
		if (bits >= 16) {
			bits >>>= 4;
			log += 4;
		}
		if (bits >= 4) {
			bits >>>= 2;
			log += 2;
		}
		return log + (bits >>> 1) + 1;
	}

	public void rearrangeIP() {
		Queue<AS> q = new LinkedList<AS>();
		root.addressList.add(new IPAddress());
		int allTraveledAs = 0;
		int allIP = 0;
		q.add(root);
		while (!q.isEmpty()) {
			AS now = q.poll();
			allTraveledAs++;
			int bitsNum = log2X(now.customer.size());
			// System.out.println("Traverse " + now.getString());
			// System.out.println("Queue size:" + q.size());
			for (IPAddress ip : now.addressList) {
				if (!ip.flag) {
					prefixLength = prefixLength + ip.size();
					int index = 0;
					for (AS customer : now.customer) {
						customer.addressList.add(new IPAddress(ip.bitAddress,
								ip.size, bitsNum, index));
						allIP++;
						index++;
						if (q.contains(customer)) {
							q.remove(customer);
						}
						q.add(customer);
					}
					ip.flag = true;
				}
			}
		}
		logger.info("average IP Length:" + prefixLength / allIP);
		System.out.println("AllTraveledAS:" + allTraveledAs);
		System.out.println("AllIP:" + allIP);
	}

	public void buildTopology() {
		for (AS a : asList.values()) {
			for (Integer i : a.edge.keySet()) {
				if (a.edge.get(i) == 2) {
					// System.out.println("one 2 type edge");
					a.customer.add(asList.get(i));
					asList.get(i).providers.add(a);
				} else if (a.edge.get(i) == 3) {
					// System.out.println("one 3 type edge");
					a.providers.add(asList.get(i));
					asList.get(i).customer.add(a);
				} else if (a.edge.get(i) != 1) {
					System.out
							.println("--------------------------------------");
				}
			}
		}
		int num = 0;
		for (AS a : asList.values()) {
			/**
			 * clear up memory
			 */
			a.edge.clear();
			a.transit.clear();
			a.neighbor.clear();
			if (a.providers.isEmpty()) {
				a.providers.add(root);
				root.customer.add(a);
				num++;
			}
		}
		System.out.println("root has " + num + " customer.");
	}

	private void computeRelationships() {
		for (ArrayList<Integer> path : p.pathList) {
			for (int i = 0; i < path.size() - 1; i++) {
				int transitI = asList.get(path.get(i)).transit.get(path
						.get(i + 1));
				int transitIPlus = asList.get(path.get(i + 1)).transit.get(path
						.get(i));
				if ((transitI > L && transitIPlus > L)
						|| (transitI <= L && transitI > 0 && transitIPlus <= L && transitIPlus > 0)) {
					asList.get(path.get(i)).edge.put(path.get(i + 1), 1);
				} else if (transitIPlus > L || transitI == 0) {
					asList.get(path.get(i)).edge.put(path.get(i + 1), 2);
				} else if (transitI > L || transitIPlus == 0) {
					asList.get(path.get(i)).edge.put(path.get(i + 1), 3);
				}
			}
		}

	}

	public void computeTransit() {
		// for (Integer[] path: p.pathList){
		for (int j = 0; j < p.pathList.size(); j++) {
			// int maxIndex = path.intValue();
			ArrayList<Integer> path = p.pathList.get(j);
			int maxIndex = 0;
			int maxValue = path.get(maxIndex);
			// int maxIndex = path.get(0).intValue();
			// for (Integer index : path) {
			for (int i = 1; i < path.size(); i++) {
				if (asList.get(path.get(i)).degree > asList.get(maxValue).degree) {
					maxIndex = i;
					maxValue = path.get(maxIndex);
				}
			}
			boolean flip = false;
			for (int i = 0; i < path.size() - 1; i++) {
				int pathI = Integer.valueOf(path.get(i));
				int pathIP = Integer.valueOf(path.get(i + 1));
				if (!asList.get(pathI).transit.containsKey(pathIP)) {
					asList.get(pathI).transit.put(pathIP, 0);
				}
				if (!asList.get(pathIP).transit.containsKey(pathI)) {
					asList.get(pathIP).transit.put(pathI, 0);
				}
				if (!flip) {
					if (maxValue != Integer.valueOf(pathI)) {
						asList.get(pathI).transit.put(pathIP,
								asList.get(pathI).transit.get(pathIP) + 1);
					} else {
						flip = true;
						asList.get(pathIP).transit.put(pathI,
								asList.get(pathIP).transit.get(pathI) + 1);
					}
				} else {
					asList.get(pathIP).transit.put(pathI,
							asList.get(pathIP).transit.get(pathI) + 1);
				}
			}
		}
	}

	public void computeDegree() {
		for (ArrayList<Integer> path : p.pathList) {
			for (int i = 0; i < path.size() - 1; i++) {
				try {
					asList.get(path.get(i)).neighbor.add(path.get(i + 1));
					asList.get(path.get(i + 1)).neighbor.add(path.get(i));
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("i:" + i + " path[i]:" + path.get(i)
							+ " path[i+1]:" + path.get(i + 1));
				}
			}
		}
		for (AS as : asList.values()) {
			as.degree = as.neighbor.size();
		}
	}
}
