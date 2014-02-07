package Data;

import org.w3c.dom.Element;

/**
 * Used to store data in a {@link Notebook}.  Provides ordering and titling. 
 * @author Hirsch Singhal
 */
public abstract class DataStorage implements Comparable<DataStorage>{

	protected int serial;
	protected Element root;
	
	/**
	 * Serial numbers are not guaranteed to follow chronological order. 
	 * @return Serial number used for ordering.  
	 */
	public final int getSerial(){
		return serial;
	}
	
	/**
	 * Sets the serial number.  Updates the DOM representation.   
	 * @param value New serial number.  
	 */
	public final void setSerial(int value) {
		this.serial = value;
		root.setAttribute("Serial", String.valueOf(value));	
	} 
	
	/**
	 * @return DOM Element corresponding to this object.
	 */
	public final Element getNode(){
		return root;
	}
	
	/**
	 * Used to title tabs and list entries .
	 * @return Title of this DataStorage object (filename or {@link Entry} title). 
	 */
	public abstract String getTitle();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public final String toString(){
		return getTitle();
	}
	/**
	 * Orders by serial number.
	 * @param d DataStorage object to compare 
	 * @return Positive if this comes before d.  Negative if it comes after.  0 if they have the same serial number. 
	 */
	public final int compareTo(DataStorage d){
        return this.serial - d.serial;
    }
}
