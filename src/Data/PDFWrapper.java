package Data;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.icepdf.core.exceptions.PDFException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Hirsch Singhal
 *	Stores binary data for a PDF.  
 */
public class PDFWrapper extends DataStorage{

	private File filename;
	private SecretKeySpec key;
	
	/**
	 * Used to create a new {@link PDFWrapper}.
	 * @param item Node in the DOM containing this object
	 * @param doc Document used to create new Nodes, Children, and Elements.
	 * @param parent Parent Notebook.  Used to force a save once loaded.
	 * @param file Location of the .pdf file.
	 * @param key Used to encrypt the PDF data.  Can be null.
	 * @param serial Serial number for this {@link DataStorage} object.
	 * @throws IOException Occurs if IO access is blocks or an incorrect file is used. 
	 */
	public PDFWrapper(Node item, Document doc, Notebook parent, File file, SecretKeySpec key, int maxSerial) throws IOException {
		this.root=(Element) item;
		this.key = key;
		this.filename = file;
		this.serial = maxSerial;
		
		InputStream is = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		is.read(buf);
		String data = Util.encryptBytes(buf, key);		
		Element body = doc.createElement("Body");
		body.appendChild(doc.createTextNode(data));
		Element titleEl = doc.createElement("Title");
		titleEl.appendChild(doc.createTextNode(Util.encrypt(filename.getName(), key)));
		((Element) root).setAttribute("Serial", String.valueOf(serial));
		root.appendChild(titleEl);
		root.appendChild(body);
		parent.save();
		
		is.close();
	}

	/**
	 * Used to load a {@link PDFWrapper} from a DOM.  Decrypts the metadata if needed.
	 * @param item DOM Node containing the PDF data.
	 * @param key May be null.  Key used to decrypt the PDF data and metadata.
	 */
	public PDFWrapper(Node item, SecretKeySpec key) {
		root=(Element) item;
		this.key = key;
		this.filename = new File(Util.decrypt(getTextValue(null, (Element)item, "Title"), key));
		this.serial = Integer.parseInt(item.getAttributes().getNamedItem("Serial").getTextContent());
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
	
	/**
	 * Creates a decrypted stream suitable for use with a PDF rendering engine.
	 * @return PDF file as a stream.
	 * @throws IOException Occurs if the data cannot be accessed. 
	 */
	public InputStream getPDFStream() throws IOException{		
		byte[] data = Util.decryptBytes(getTextValue(null,  (Element)root,  "Body").getBytes(), key);
		return new ByteArrayInputStream(data);
	}	
}
