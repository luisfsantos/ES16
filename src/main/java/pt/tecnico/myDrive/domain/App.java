package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.InvalidContentException;
import pt.tecnico.myDrive.exception.InvalidPermissionException;
import pt.tecnico.myDrive.exception.IsNotJavaFullyQualifiedNameException;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class App extends App_Base {

    public App(String name, User owner, Directory parent, String content) {
    	this.initFile(name, owner.getUmask(), owner, parent);
    	this.setContent(content);
    }
    
    
    public App(String name, Directory parent, String content) {
    	this.initFile(name, Manager.getInstance().getSuperUser().getUmask(), Manager.getInstance().getSuperUser(), parent);
    	this.setContent(content);
    }

    public App(Manager manager, Element appNode) throws UnsupportedEncodingException {
		String method = new String(appNode.getChildText("method").getBytes("UTF-8"));
		setContent(method);
		this.xmlImport(manager, appNode);
    }
    
    @Override
	public Element xmlExport() {
    	Element appElement = super.xmlExport();
		appElement.setName("app");
		
		appElement.getChild("contents").setName("method");
		
		return appElement;
	}

	@Override
	public void setContent(String content) {
		System.out.println("YOOOOOOOOOOOOOOOOOOO:"+ content + "\n");

		String[] reservedWords = {"import","true", "null"};

		boolean isJavaFullyQualifiedName =
				Pattern.matches("([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*", content);

		boolean containsReservedWords = false;
		for(String word : reservedWords) {
			if (content.contains(word + ".")
					|| content.contains("." + word + ".")
					|| content.contains("." + word)) {
				containsReservedWords = true;
				break;
			}
		}

		if (content.equals("")){
			System.out.println("HEYYYYYYYY\n");
			isJavaFullyQualifiedName = false;
		}

		if (!isJavaFullyQualifiedName || containsReservedWords) {
			throw new InvalidContentException(this.getName(),content);  //IsNotJavaFullyQualifiedNameException(content);
		}

		super.setContent(content);
	}

	@Override
	public void write(User u, String content){
		System.out.println("writing:" + content+"\n");
		if (u.hasPermission(this, Mask.WRITE)) {
			super.setContent(content);
		}
		else {
			throw new InvalidPermissionException("Write in App"); //not sure about argument
		}
	}
}
