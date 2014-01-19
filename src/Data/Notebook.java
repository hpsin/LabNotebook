package Data;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import org.xml.sax.SAXException;


public class Notebook {
	private ArrayList<Entry> entryList = new ArrayList<>();
	private ArrayList<PDFWrapper> pdfList = new ArrayList<>();
	private boolean isEncrypted;
	private String author;
	private String title;
	private Document dom;
	private File filename;
	
	public Notebook(File f, String t, String author, boolean encrypted){
		try {
			this.filename=f;
			this.author = author;
			this.title = t;
			this.isEncrypted = encrypted;
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			dom = docBuilder.newDocument();
			Element rootElement = dom.createElement("Notebook");
			dom.appendChild(rootElement);

			rootElement.appendChild(makeTextNode("Title", title));
			rootElement.appendChild(makeTextNode("Author", author));
			rootElement.appendChild(makeTextNode("Encrypted", isEncrypted ? "1" : "0"));
			rootElement.appendChild(dom.createElement("Edits"));		
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(dom);
			StreamResult result = new StreamResult(f);
	 
			// Output to console for testing
//			 StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
	
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}


	public Notebook(File f){
		this.filename=f;
		if(!f.exists()){
			System.out.println("Used wrong constructor or something went wrong.");
		}
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
				entryList.add(new Entry(entries.item(i), dom, this));				
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
	
	public Entry newEntry(String title){
		Element el = dom.createElement("Entry");
		dom.getFirstChild().appendChild(el);
		Entry en = new Entry(el, dom, title, this);
		entryList.add(en);
		return en;		
	}
	
	public boolean save(){
		 try {
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(dom);
				StreamResult result = new StreamResult(filename);
				// Output to console for testing
				// StreamResult result = new StreamResult(System.out);
		 
				transformer.transform(source, result);
		 
		 }catch(TransformerException p){
			 p.printStackTrace();
			 return false;
		 }
		 return true;
	}
	
	private Element makeTextNode(String name, String value){
		Element e = dom.createElement(name);
		e.appendChild(dom.createTextNode(value));
		return e;
	}


	public ArrayList<Entry> getEntries() {
		return entryList;
	}
	
}
