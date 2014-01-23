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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PDFWrapper extends DataStorage{

	private File filename;
	private SecretKeySpec key;
	
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
	
	public InputStream getPDFStream() throws IOException{		
		byte[] data = Util.decryptBytes(getTextValue(null,  (Element)root,  "Body").getBytes(), key);
		return new ByteArrayInputStream(data);
	}	
}
