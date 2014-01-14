package Data;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Entry {
	private String html_body;
	private String title;
	private Timestamp timestamp;
	private Notebook parent;
	private int editCount;
	private ArrayList<Timestamp> editList;
	
	public Entry(String body, String title, Timestamp time, Notebook parent){
		html_body=body;
		this.title=title;
		timestamp=time;
		this.parent = parent;
		editCount=0;
		editList = new ArrayList<>();
	}
	
	public Entry(Node item, Notebook notebook) {
		parent = notebook;
		Element e = (Element) item;
		title = getTextValue(title, e, "Title");
		editCount = getNumValue(e, "EditCount");
		html_body = getTextValue(html_body, e, "html");
		NodeList edits = e.getElementsByTagName("Edit");
		for(int i=0; i<edits.getLength(); i++){
		//	editList.add(new Timestamp(Long.parseLong(getTextValue(null, edits.item(i), ")))			
		}
	}

	private int getNumValue(Element e, String tag) {
		return Integer.parseInt(getTextValue(null, e, tag));
	}

	public String getHTML() {
		return html_body;
	}

	public String getTitle() {
		return title;
	}

	public int getEditCount(){
		return editCount;
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
