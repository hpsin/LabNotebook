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

	private Node root;
	private File filename;
	private SecretKeySpec key;
	
	public PDFWrapper(Node item, Document doc, Notebook parent, File file, SecretKeySpec key) throws IOException {
		this.root=item;
		this.key = key;
		this.filename = file;
		InputStream is = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		is.read(buf);
		String data = Util.encrypt(Base64.encodeBase64String(buf), key);		
		Element body = doc.createElement("Body");
		body.appendChild(doc.createTextNode(data));
		Element titleEl = doc.createElement("Title");
		titleEl.appendChild(doc.createTextNode(Util.encrypt(filename.getName(), key)));
		root.appendChild(titleEl);
		root.appendChild(body);
		parent.save();
		
		is.close();
	}

	public PDFWrapper(Node item, SecretKeySpec key) {
		root=item;
		this.key = key;
		this.filename = new File(Util.decrypt(getTextValue(null, (Element)item, "Title"), key));
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
		String data = Util.decrypt(getTextValue(null,  (Element)root,  "Body"), key);
		return new ByteArrayInputStream(Base64.decodeBase64(data));
	}

}
