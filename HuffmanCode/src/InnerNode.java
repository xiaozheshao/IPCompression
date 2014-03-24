public class InnerNode implements HuffmanNode {

	private HuffmanNode leftChild = null;

	private HuffmanNode rightChild = null;

	private HuffmanNode parent = null;

	private int frequency = 0;

	private boolean[] code = new boolean[0];

	private int codeLength = 0;

	public InnerNode(HuffmanNode left, HuffmanNode right) {
		leftChild = left;
		rightChild = right;
		left.setParent(this);
		right.setParent(this);
		frequency = left.getFrequency() + right.getFrequency();
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
	public void setFrequency(int frequency) {
		// TODO Auto-generated method stub
		this.frequency = frequency;
	}

	@Override
	public void setLeftChild(HuffmanNode left) {
		// TODO Auto-generated method stub
		this.leftChild = left;
	}

	@Override
	public void setRightChild(HuffmanNode right) {
		// TODO Auto-generated method stub
		this.rightChild = right;
	}

	@Override
	public void setParent(HuffmanNode parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
	}

	@Override
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

	public int getCodeLength() {
		return codeLength;
	}

	@Override
	public boolean[] getBoolCode() {
		// TODO Auto-generated method stub
		return code;
	}
}
