class IPAddress {
	public boolean[] bitAddress = new boolean[8];
	int size = 0;
	boolean flag = false;
	int frequency = 0;

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getFrequency() {
		return frequency;
	}

	public int size() {
		return size;
	}

	public IPAddress() {
		flag = false;
		size = 0;
	}

	public IPAddress(boolean[] address, int originSize, int bitsNum, int index) {
		// this.bitAddress = address.clone();
		size = originSize;
		// this.size = size;
		boolean[] temp = new boolean[bitsNum];
		int num = 0;
		do {
			int m = index % 2;
			if (m == 1) {
				temp[num] = true;
			} else {
				temp[num] = false;
			}
			num++;
			index = index >>> 1;
		} while (index > 0);
		size = size + bitsNum;
		while (this.bitAddress.length < size) {
			this.bitAddress = new boolean[size];
		}
		for (int i = 0; i < originSize; i++) {
			bitAddress[i] = address[i];
		}
		for (int i = 0; i < bitsNum; i++) {
			this.bitAddress[size - 1 - i] = temp[bitsNum - i - 1];
		}
		// this.address.addAll(address);
		// this.address.addAll(temp);
		flag = false;
	}
}
