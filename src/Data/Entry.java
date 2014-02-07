package Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;

import javax.crypto.spec.SecretKeySpec;

import net.atlanticbb.tantlinger.shef.HTMLEditorPane;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Data representation of stored text and images.  Automatically updates the 
 * DOM using the provided {@link Node} upon saving.  
 * @author Hirsch Singhal
 */
public class Entry extends DataStorage{
	private String html_body;
	private String title;
	private Date timestamp;
	private int editCount;
	private Document doc;
	private ArrayList<Date> editList = new ArrayList<>();
	private Notebook parent;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private SecretKeySpec key;

	/**
	 * Used to create a new Entry from a Notebook. 
	 * @param item Node used to save this Entry's spot in the DOM.
	 * @param doc Top level of the DOM.  Used to generate new nodes.
	 * @param title Title of the Entry.
	 * @param note Parent Notebook that this Entry belongs to.
	 * @param key Key used to encrypt/decrypt body and title.  May be <code>null</code>. 
	 * @param maxSerial Serial number for this Entry.  Used to order {@link DataStorage} objects. 
	 */
	public Entry(Node item, Document doc, String title, Notebook note, SecretKeySpec key, int maxSerial){
		this.doc = doc;
		parent = note;
		Date d = new Date();
		timestamp = d;
		this.title=title;
		this.root=(Element) item;
		this.key = key;
		this.serial = maxSerial;
		
		item.appendChild(doc.createElement("Title"));
		item.appendChild(doc.createElement("Timestamp"));
		item.appendChild(doc.createElement("Edits"));
		root.setAttribute("Serial", String.valueOf(maxSerial));
		
		Node html = doc.createElement("Body");
		Node html_cdata = doc.createCDATASection("");
		item.appendChild(html);
		html.appendChild(html_cdata);

		getNode("Title", (Element) item).appendChild(doc.createTextNode(Util.encrypt(title, key)));
		getNode("Timestamp", root).appendChild(doc.createTextNode(sdf.format(d)));
		editCount=0;
	}

	/**
	 * Used to load an unencrypted Entry from a Notebook.
	 * @param item Node used to save this Entry's spot in the DOM.
	 * @param doc Top level of the DOM.  Used to generate new nodes.
	 * @param note Parent Notebook that this Entry belongs to.
	 */
	public Entry(Node item, Document doc, Notebook note) {
		this(item,doc,note, null);
	}

	/**
	 * Used to load an unencrypted Entry from a Notebook.
	 * @param item Node used to save this Entry's spot in the DOM.
	 * @param doc Top level of the DOM.  Used to generate new nodes.
	 * @param note Parent Notebook that this Entry belongs to.
	 * @param key Key used to encrypt/decrypt body and title.  May be <code>null</code>. 
	 */
	public Entry(Node item, Document doc, Notebook note, SecretKeySpec key) {
		this.doc=doc;
		this.parent = note;
		this.key=key;
		this.serial = Integer.parseInt(item.getAttributes().getNamedItem("Serial").getTextContent());
		root = (Element) item;
		title = Util.decrypt(getTextValue(title, root, "Title"), key);
		html_body = Util.decrypt(getTextValue(html_body, root, "Body"), key);
		try {
			timestamp = sdf.parse(getTextValue(null, root, "Timestamp"));
		} catch (ParseException e2) {
			e2.printStackTrace();
		}
		NodeList edits = root.getElementsByTagName("Edits").item(0).getChildNodes();
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

	/**
	 * Getter for HTML data stored in this Entry
	 * @return HTML as String.
	 */
	public String getHTML() {
		return html_body;
	}
	/**
	 * Sets HTML data.
	 * @param h HTML to be recorded by this Entry.
	 */
	public void setHTML(String h){
		html_body=h;
	}

	/* (non-Javadoc)
	 * @see Data.DataStorage#getTitle()
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return Number of times this Entry has been edited.  Equal to the number of edit timestamps found in the DOM.
	 */
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

	/**
	 * Updates the DOM using the {@link Node} provided in the constructor.  If 
	 * data should be encrypted, this method does so.  Forces the parent 
	 * {@link Notebook} to save to the harddrive.
	 * @return Success of the save. 
	 */
	public boolean saveXML(){
		((CharacterData)getNode("Body",root).getFirstChild()).setData(Util.encrypt(html_body, key));
		Date d = new Date();
		getNode("Edits", root).appendChild(makeTextNode("Edit", sdf.format(d), doc)); 
		editCount++;
		return parent.save();
	}

	private Element makeTextNode(String name, String value, Document doc){
		Element e = doc.createElement(name);
		e.appendChild(doc.createTextNode(value));
		return e;
	}

	/**
	 * Returns creation timestamp.
	 * @return Time of creation. 
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Helper method for getTimestamp().  Formats it as a String, using the following format:
	 * "yyyy-MM-dd'T'HH:mm:ss"
	 * @return String-formatted time of creation. 
	 */
	public String getTimestampString() {
		return sdf.format(timestamp);
	}

	/**
	 * @return ArrayList of edit timestamps.
	 */
	public ArrayList<Date> getEdits() {
		return editList;
	}

	/**
	 * Sets the title of this Entry.  Updates the DOM.
	 * @param t new title. 
	 */
	public void setTitle(String t){
		this.title=t;
		root.getElementsByTagName("Title").item(0).setTextContent(Util.encrypt(title, key));
	}

	/**
	 * Sets a new creation time for this Entry.  Updates the DOM.
	 * @param parse new creation timestamp. 
	 */
	public void setTimestamp(Date parse) {
		this.timestamp = parse;
		root.getElementsByTagName("Timestamp").item(0).setTextContent(sdf.format(parse));
		
	}
	
	
	
}
