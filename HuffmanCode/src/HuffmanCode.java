import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.logging.Logger;

public class HuffmanCode {
	private static Logger logger = Logger.getLogger(HuffmanCode.class
			.getSimpleName());
	public CodeComparator comparator = new CodeComparator();

	private Queue<HuffmanNode> nodesList = new PriorityQueue<HuffmanNode>(100,
			comparator);

	private HuffmanNode root = null;

	public static void main(String[] args) throws IOException {
		// fix_length();
		variable_length();
	}

	public static void variable_length() throws IOException {
		logger.info("Start the infer.");
		InferTopology it = new InferTopology(
				"/home/shaoxiaozhe/WORKSPACE/PrepairRT/RTPath");
		List<IPAddress> IPList = it.infer_variable();
		HuffmanCode hc = new HuffmanCode();
		hc.preFrequency(IPList);
		logger.info("preFrequency over");
		hc.buildTree();
		logger.info("buildTree over");
		hc.output(hc.root);
		logger.info("output over");
		hc.compute(hc.root);
		logger.info("End");
	}

	public void preFrequency(List<IPAddress> IPList) {
		for (IPAddress ip : IPList) {
			nodesList.add(new Prefix(ip.getFrequency()));
		}
		logger.info("Number of entries:" + IPList.size());
		// TODO
	}

	public static void fix_length() throws IOException {
		logger.info("Start the infer.");
		InferTopology it = new InferTopology(
				"/home/shaoxiaozhe/WORKSPACE/PrepairRT/RTPath");
		int bitLength = 18;
		int[] frequency = it.infer(bitLength);

		HuffmanCode hc = new HuffmanCode();
		hc.preFrequency(frequency);
		// hc.prepareData();

		hc.buildTree();
		hc.output(hc.root);
		hc.compute(hc.root);
		logger.info("End");
	}

	public void compute(HuffmanNode root) {
		long sum = 0;
		Queue<HuffmanNode> q = new LinkedList<HuffmanNode>();
		HuffmanNode result = null;
		q.offer(root);
		while (!q.isEmpty()) {
			result = q.poll();
			if (result instanceof Prefix) {
				sum = sum + result.getFrequency() * result.getCodeLength();
			}
			if (result.getLeftChild() != null) {
				q.offer(result.getLeftChild());
			}
			if (result.getRightChild() != null) {
				q.offer(result.getRightChild());
			}
		}
		logger.info("HuffmanCode length:" + sum);
	}

	private void output(HuffmanNode root) {
		int index = 0;
		Queue<HuffmanNode> q = new LinkedList<HuffmanNode>();
		HuffmanNode result = null;
		q.offer(root);
		while (!q.isEmpty()) {
			result = q.poll();
			// print(result, index);
			if (result.getLeftChild() != null) {
				q.offer(result.getLeftChild());
				result.getLeftChild().setCode(result, false);
			}
			if (result.getRightChild() != null) {
				q.offer(result.getRightChild());
				result.getRightChild().setCode(result, true);
			}
			index++;
		}
	}

	private void print(HuffmanNode node, int index) {
		if (node instanceof InnerNode) {
			System.out.println("InnerNode" + index + ":" + node.getFrequency()
					+ "(" + node.getCode() + ")");
		} else {
			logger.info("Prefix" + index + ":" + node.getFrequency() + "("
					+ node.getCode() + ")" + ((Prefix) node).originalCode);
			// System.out
			// .println("Prefix" + index + ":" + node.getFrequency() + "("
			// + node.getCode() + ")"
			// + ((Prefix) node).originalCode);
		}

	}

	private void buildTree() {
		// TODO Auto-generated method stub
		// Collections.sort(nodesList, comparator);
		logger.info("Huffman Code has been sorted!");
		while (nodesList.size() > 1) {
			//logger.info("num:" + nodesList.size());
			HuffmanNode left = nodesList.remove();
			HuffmanNode right = nodesList.remove();
			HuffmanNode parent = new InnerNode(left, right);
			nodesList.add(parent);
			// insert(nodesList, parent);
		}
		root = nodesList.remove();
	}

	private void insert(LinkedList<HuffmanNode> nodesList, HuffmanNode parent) {
		int frequency = parent.getFrequency();
		Iterator<HuffmanNode> iter = nodesList.iterator();
		int count = 0;
		while (iter.hasNext()) {
			HuffmanNode temp = iter.next();
			if (frequency <= temp.getFrequency()) {
				break;
			}
			count++;
		}
		nodesList.add(count, parent);
	}

	private void preFrequency(int[] frequency) {
		for (int i = 0; i < frequency.length; i++) {
			if (frequency[i] > 0) {
				nodesList.add(new Prefix(frequency[i], i));
			}
		}
	}

	private void imitateData() {
		// TODO
		int count = 10;
		Random r = new Random(System.nanoTime());
		for (int i = 1; i < count; i++) {
			int temp = r.nextInt(100);
			System.out.println("Random Int:" + temp);
			nodesList.add(new Prefix(temp));
		}
	}

	private void prepareData() {
		// TODO Auto-generated method stub
		imitateData();
	}

}
