package Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Entry {
	private String html_body;
	private String title;
	private Date timestamp;
	private Element nodeRep;
	private int editCount;
	private Document doc;
	private ArrayList<Date> editList = new ArrayList<>();
	private Notebook parent;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	
	public Entry(Node item, Document doc, String title, Notebook note){
		this.doc = doc;
		parent = note;
		Date d = new Date();
		timestamp = d;
		this.title=title;
		this.nodeRep=(Element) item;
		
		item.appendChild(doc.createElement("Title"));
		item.appendChild(doc.createElement("Timestamp"));
		item.appendChild(doc.createElement("Edits"));
		item.appendChild(makeTextNode("Body", "", doc));
		
		getNode("Title", (Element) item).appendChild(doc.createTextNode(title));
		getNode("Timestamp", nodeRep).appendChild(doc.createTextNode(sdf.format(d)));
		editCount=0;
	}
	
	public Entry(Node item, Document doc, Notebook note) {
		this.doc=doc;
		this.parent = note;
		nodeRep = (Element) item;
		title = getTextValue(title, nodeRep, "Title");
		html_body = getTextValue(html_body, nodeRep, "Body");
		//html_body = html_body.substring(9, html_body.length()-4); //Get rid of CDATA
		try {
			timestamp = sdf.parse(getTextValue(null, nodeRep, "Timestamp"));
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		NodeList edits = nodeRep.getElementsByTagName("Edits").item(0).getChildNodes();
		editCount = edits.getLength();
		
		for(int i=0; i<edits.getLength(); i++){
			Node n = edits.item(i);
			try {
				String text = n.getTextContent();
				Date editDate = sdf.parse(text);
				editList.add(editDate);
//				editList.add(sdf.parse(n.getTextContent()));
			} catch (DOMException | ParseException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String getHTML() {
		return html_body;
	}
	public void setHTML(String h){
		html_body=h;
	}

	public String getTitle() {
		return title;
	}
	public String toString(){
		return title;
	}

	public int getEditCount(){
		return editCount;
	}
	
	private String getTextValue(String def, Element doc, String tag) {
	    String value = def;
	    value = getNode(tag, doc).getTextContent();
	    return value;
	}

	private Element getNode(String tag, Element doc){
		NodeList nl = doc.getElementsByTagName(tag);
	    if (nl.getLength() > 0 ) {
	        return (Element) nl.item(0);
	    }
	    throw new NoSuchElementException();
	}
	
	public boolean saveXML(){
		getNode("Body",nodeRep).getFirstChild().setNodeValue( html_body);
		Date d = new Date();
		getNode("Edits", nodeRep).appendChild(makeTextNode("Edit", sdf.format(d), doc)); 
		editCount++;
		return parent.save();
	}
		
	private Element makeTextNode(String name, String value, Document doc){
		Element e = doc.createElement(name);
		e.appendChild(doc.createTextNode(value));
		return e;
	}
}
