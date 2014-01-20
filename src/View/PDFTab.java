package View;

import java.io.IOException;

import javax.swing.JPanel;

import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.views.DocumentViewController;
import org.icepdf.ri.common.views.DocumentViewControllerImpl;

import Data.PDFWrapper;


public class PDFTab extends javax.swing.JPanel {

	public PDFTab(PDFWrapper pw){
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
			controller.openDocument(pw.getPDFStream(), "description", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}

		// hard set the page view to single page which effectively give a single
		// page view. This should be done after openDocument as it has code that
		// can change the view mode if specified by the file.
		controller.setPageViewMode(
				DocumentViewControllerImpl.ONE_PAGE_VIEW,
				false);
	}


}
