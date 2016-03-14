package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.ImportDocumentException;
import pt.tecnico.myDrive.exception.IsNotJavaFullyQualifiedNameException;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

public class App extends App_Base {

    public App(String name, String permission, Manager manager, User owner, Directory parent, String content) {
    	this.initFile(name, permission, manager, owner, parent);
    	this.setContent(content);
    }

    public App(Manager manager, Element appNode) throws UnsupportedEncodingException {
        try {
            String method = new String(appNode.getChildText("method").getBytes("UTF-8"));
            setContent(method);
            this.xmlImport(manager, appNode);
        } catch (UnsupportedEncodingException e) {
            throw new ImportDocumentException("UnsupportedEncodingException");
        }
    }

    @Override
    public String getFileType() {
        return "app";
    }
    
    @Override
	public Element xmlExport() {
		Element element = new Element("app");
		element.setAttribute("id", getId().toString());
		
		Element pathElement = new Element("path");
		pathElement.setText(getAbsolutePath());
		element.addContent(pathElement);

		Element nameElement = new Element("name");
		nameElement.setText(getName());
		element.addContent(nameElement);

		Element ownerElement = new Element("owner");
		ownerElement.setText(getOwner().getName());
		element.addContent(ownerElement);

		Element permissionElement = new Element("perm");
		permissionElement.setText(getPermissions());
		element.addContent(permissionElement);
		
		Element methodElement = new Element("method");
		methodElement.setText(getContent());
		element.addContent(methodElement);
		
		return element;
	}

	@Override
	public void setContent(String content) {
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

		if (!isJavaFullyQualifiedName || containsReservedWords) {
			throw new IsNotJavaFullyQualifiedNameException(content);
		}

		super.setContent(content);
	}
}
