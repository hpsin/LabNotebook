package Data;

public abstract class DataStorage {

	public abstract String getTitle();
	
	public String toString(){
		return getTitle();
	}
	
}
