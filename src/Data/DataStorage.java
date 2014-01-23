package Data;

import org.w3c.dom.Element;

public abstract class DataStorage implements Comparable<DataStorage>{

	protected int serial;
	protected Element root;
	
	public final int getSerial(){
		return serial;
	}
	
	public final Element getNode(){
		return root;
	}
	
	public abstract String getTitle();
	
	public final String toString(){
		return getTitle();
	}
	public final int compareTo(DataStorage d){
        return this.serial - d.serial;
    }
}
