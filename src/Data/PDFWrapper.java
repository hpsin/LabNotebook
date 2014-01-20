package Data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PDFWrapper extends DataStorage{

	private Node root;
	private File filename;
	
	public PDFWrapper(Node item, Document doc, Notebook parent, File file) throws IOException {
		this.root=item;
		this.filename = file;
		InputStream is = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		is.read(buf);
		String data = Base64.encodeBase64String(buf);		
		Element body = doc.createElement("Body");
		body.appendChild(doc.createTextNode(data));
		Element titleEl = doc.createElement("Title");
		titleEl.appendChild(doc.createTextNode(filename.getName()));
		root.appendChild(titleEl);
		root.appendChild(body);
		parent.save();
		
		is.close();
	}

	public PDFWrapper(Node item) {
		root=item;
		this.filename = new File(getTextValue(null, (Element)item, "Title"));
	}
	
	public String getTitle(){
		return filename.getName();
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
	
	public InputStream getPDFStream() throws IOException{		
		String data = getTextValue(null,  (Element)root,  "Body");
        byte[] buf = Base64.decodeBase64(data);
		return new ByteArrayInputStream(Base64.decodeBase64(data));
	}

}
