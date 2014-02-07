package View;

import Data.DataStorage;
import Data.Entry;
import Data.PDFWrapper;

/**
 * @author Hirsch Singhal
 *  Interface to provide access to Data objects from within a View object.
 */
public interface DataRepresentation {

	/**
	 * Used to obtain the {@link Entry} or {@link PDFWrapper}
	 * @return {@link DataStorage} object corresponding to this View object. 
	 */
	public DataStorage getData();
	
}
