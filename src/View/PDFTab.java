package View;

import java.io.IOException;

import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;

import Data.DataStorage;
import Data.PDFWrapper;


/**
 * Displays a PDF document one page at a time using icePDF.
 * @author Hirsch Singhal
 *	
 */
public class PDFTab extends javax.swing.JPanel implements DataRepresentation{

	private PDFWrapper pdfw;
	
	/**
	 * @param pw {@link PDFWrapper} to provide the inputStream containing the 
	 * raw PDF data.  
	 */
	public PDFTab(PDFWrapper pw){
		this.pdfw=pw;
		this.setName(pw.getTitle());
		SwingController controller = new SwingController();
		controller.setIsEmbeddedComponent(true);

		// set the viewController embeddable flag.
		DocumentViewController viewController =
				controller.getDocumentViewController();

		JPanel viewerComponentPanel = new JPanel();
		viewerComponentPanel.add(viewController.getViewContainer());

		// add copy keyboard command
		ComponentKeyBinding.install(controller, viewerComponentPanel);

		// add interactive mouse link annotation support via callback
		controller.getDocumentViewController().setAnnotationCallback(
				new org.icepdf.ri.common.MyAnnotationCallback(
						controller.getDocumentViewController()));

		this.add(viewerComponentPanel);

		// Now that the GUI is all in place, we can try opening a PDF
//		byte[] data =  pw.getPDFStream();
		try {
			controller.openDocument(pw.getPDFStream(), "", null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// hard set the page view to single page which effectively give a single
		// page view. This should be done after openDocument as it has code that
		// can change the view mode if specified by the file.
		controller.setPageViewMode(
				DocumentViewControllerImpl.ONE_PAGE_VIEW,
				false);
	}

	@Override
	public DataStorage getData() {
		return pdfw;
	}


}
