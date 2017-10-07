package equationsolver;

class QueueData {
	public QueueData(int varIndex, double value) {
		super();
		this.varIndex = varIndex;
		this.value = value;
	}
	public int getVarIndex() {
		return varIndex;
	}
	public void setVarIndex(int varIndex) {
		this.varIndex = varIndex;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	private int varIndex;
	private double value;
}
