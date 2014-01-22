package Data;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;
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
	private SecretKeySpec key;
	private int maxSerial;

	public Notebook(File f, String t, String author, boolean encrypted, SecretKeySpec key){
		try {
			this.filename=f;
			this.author = author;
			this.title = t;
			this.isEncrypted = encrypted;
			this.key = key;
			this.maxSerial = 0;

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			dom = docBuilder.newDocument();
			Element rootElement = dom.createElement("Notebook");
			dom.appendChild(rootElement);

			rootElement.appendChild(makeTextNode("Title", title));
			rootElement.appendChild(makeTextNode("Author", Util.encrypt(author, key)));
			rootElement.appendChild(makeTextNode("Encrypted", isEncrypted ? "1" : "0"));
			rootElement.appendChild(dom.createElement("Edits"));
			if(isEncrypted)rootElement.appendChild(makeTextNode("PassCheck", Util.encrypt(f.getName(), key)));

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

	public Notebook(File f) throws InvalidKeyException{
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
			isEncrypted = getTextValue(null, el, "Encrypted").equals("1");
			if(isEncrypted){
				this.key = getKey(el,f.getName());
			}
			author = Util.decrypt(getTextValue(author, el, "Author"), key);
			
			
			Entry e;
			NodeList entries = el.getElementsByTagName("Entry");
			for(int i=0; i< entries.getLength(); i++){
				e=new Entry(entries.item(i), dom, this, this.key);
				entryList.add(e);
				if(e.getSerial() > this.maxSerial){
					maxSerial=e.getSerial();
				}
			}
			PDFWrapper p;
			entries = el.getElementsByTagName("PDF");
			for(int i=0; i< entries.getLength(); i++){
				p=new PDFWrapper(entries.item(i), key);
				pdfList.add(p);
				if(p.getSerial() > this.maxSerial){
					maxSerial=p.getSerial();
				}
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
		maxSerial++;
		Entry en = new Entry(el, dom, title, this, key, maxSerial);
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


	public void addPDF(File file) {
		Element el = dom.createElement("PDF");
		dom.getFirstChild().appendChild(el);
		try {
			maxSerial++;
			pdfList.add(new PDFWrapper(el,dom, this, file, key, maxSerial));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public ArrayList<PDFWrapper> getPDFs() {
		return pdfList;
	}

	private SecretKeySpec getKey(Element el, String secret) throws InvalidKeyException {
		String pass = (String) JOptionPane.showInputDialog(new Frame(), 
				"Please enter password:", 
				"Password Required",
				JOptionPane.QUESTION_MESSAGE,
				null,
				null,
				"");
		SecretKeySpec key2 = Util.makeKey(pass, title);
		if(secret.equals(Util.decrypt(getTextValue(null, el, "PassCheck"), key2))){
			return key2;
		}
		throw new InvalidKeyException("Password was incorrect");
	}



	
}
