import java.util.Comparator;

public class CodeComparator implements Comparator<HuffmanNode> {

	@Override
	public int compare(HuffmanNode o1, HuffmanNode o2) {
		// TODO Auto-generated method stub
		if (o1.getFrequency() < o2.getFrequency()) {
			return -1;
		} else if (o1.getFrequency() > o2.getFrequency()) {
			return 1;
		} else {
			return 0;
		}
	}

}
