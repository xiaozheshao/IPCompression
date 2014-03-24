public interface HuffmanNode {

	int getFrequency();

	HuffmanNode getLeftChild();

	HuffmanNode getRightChild();

	HuffmanNode getParent();

	String getCode();

	int getCodeLength();

	boolean[] getBoolCode();

	void setCode(HuffmanNode node, boolean last);

	void setFrequency(int frequency);

	void setLeftChild(HuffmanNode left);

	void setRightChild(HuffmanNode right);

	void setParent(HuffmanNode parent);
}
