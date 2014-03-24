public class Prefix implements HuffmanNode {

	int frequency = 0;
	HuffmanNode leftChild = null;
	HuffmanNode rightChild = null;
	HuffmanNode parent = null;
	boolean[] code = new boolean[0];
	int codeLength = 0;
	public int originalCode = 0;

	/**
	 * maybe not needed
	 * 
	 * @param left
	 * @param right
	 */
	public Prefix(HuffmanNode left, HuffmanNode right) {
		leftChild = left;
		rightChild = right;
	}

	public Prefix(int freq) {
		frequency = freq;
	}

	public Prefix(int freq, int orgin) {
		this(freq);
		originalCode = orgin;
	}

	public int getCodeLength() {
		return codeLength;
	}

	public void setCode(HuffmanNode node, boolean last) {
		codeLength = node.getCodeLength();
//		code = new boolean[codeLength + 1];
//		boolean [] temp = node.getBoolCode();
//		for (int i = 0; i < codeLength; i ++){
//			code[i] = temp[i];			
//		}
//		code[codeLength] = last;
		codeLength++;
	}

	public boolean[] getBoolCode() {
		return code;
	}

	public String getCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < codeLength; i++) {
			if (code[i] == false) {
				sb.append("0");
			} else {
				sb.append("1");
			}
		}
		return sb.toString();
	}

	@Override
	public int getFrequency() {
		// TODO Auto-generated method stub
		return frequency;
	}

	@Override
	public HuffmanNode getLeftChild() {
		// TODO Auto-generated method stub
		return leftChild;
	}

	@Override
	public HuffmanNode getRightChild() {
		// TODO Auto-generated method stub
		return rightChild;
	}

	@Override
	public HuffmanNode getParent() {
		// TODO Auto-generated method stub
		return parent;
	}

	@Override
	public void setLeftChild(HuffmanNode left) {
		// TODO Auto-generated method stub
		leftChild = left;
	}

	@Override
	public void setRightChild(HuffmanNode right) {
		// TODO Auto-generated method stub
		rightChild = right;
	}

	@Override
	public void setParent(HuffmanNode parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
	}

	@Override
	public void setFrequency(int frequency) {
		// TODO Auto-generated method stub
		this.frequency = frequency;
	}

}
