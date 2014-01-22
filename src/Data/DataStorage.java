package Data;

public abstract class DataStorage implements Comparable<DataStorage>{

	protected int serial;
	
	public final int getSerial(){
		return serial;
	}
	
	public abstract String getTitle();
	
	public final String toString(){
		return getTitle();
	}
	public final int compareTo(DataStorage d){
        return this.serial - d.serial;
    }
}
