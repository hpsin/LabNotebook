package Data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Notebook {
	private ArrayList<Entry> entryList;
	private ArrayList<PDFWrapper> pdfList;
	private boolean isEncrypted;
	
	public Notebook(){
		
	}
	
	public Notebook(File f){
		String title = null, author=null;
		Document dom;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(f);
			Element el = dom.getDocumentElement();
			
			title = getTextValue(title, el, "Title");
			author = getTextValue(author, el, "Author");
			
			NodeList entries = el.getElementsByTagName("Entry");
			for(int i=0; i< entries.getLength(); i++){
				//TODO Add encryption check
				entryList.add(new Entry(entries.item(i), this));				
			}
			
			entries = el.getElementsByTagName("PDF");
			for(int i=0; i< entries.getLength(); i++){
				pdfList.add(new PDFWrapper(entries.item(i)));				
			}
			
		} catch (ParserConfigurationException pce) {
            System.out.println(pce.getMessage());
        } catch (SAXException se) {
            System.out.println(se.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }

	}

	private String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    NodeList nl;
	    nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 && nl.item(0).hasChildNodes()) {
	        value = nl.item(0).getFirstChild().getNodeValue();
	    }
	    return value;
	}
	
}
