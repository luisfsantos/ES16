package pt.tecnico.myDrive.domain;

import org.jdom2.Element;
import pt.tecnico.myDrive.exception.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
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
		super.xmlImport(manager, appNode);
    }
    
    @Override
	public Element xmlExport() {
    	Element appElement = super.xmlExport();
		appElement.setName("app");
		
		appElement.getChild("contents").setName("method");
		
		return appElement;
	}

	@Override
	public String getType() {
		return "App";
	}

	@Override
	public void setContent(String content) {
		if(content == null) return;

		String[] reservedWords = {"import","true", "null"};
		boolean isJavaFullyQualifiedName;

		if(content.indexOf('.') == -1) {
			isJavaFullyQualifiedName =
					Pattern.matches("[\\p{Lu}_$][\\p{L}\\p{N}_$]*", content);
		} else {
			isJavaFullyQualifiedName =
					Pattern.matches("([\\p{L}_$][\\p{L}\\p{N}_$]*\\.)*[\\p{L}_$][\\p{L}\\p{N}_$]*", content);
		}

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

	@Override
	public void write(User u, String content){
		if (u.hasPermission(this, Mask.WRITE)) {
			setContent(content);
		}
		else {
			throw new AccessDeniedException("write", super.getName()); //not sure about argument
		}
	}
	
	@Override
	public void execute(User user, String[] args) {
		if (user.hasPermission(this, Mask.EXEC)) {
			try {
				ReflectClass.run(this.viewContent(), args);
			} catch (ClassNotFoundException e) {
				throw new ExecClassNotFountException(e.getMessage());
			} catch (SecurityException e) {
				throw new ExecSecurityException(e.getMessage());
			} catch (NoSuchMethodException e) {
				throw new ExecMethodNotFountException(e.getMessage());
			} catch (IllegalArgumentException e) {
				throw new ExecIllegalArgumentException(e.getMessage());
			} catch (IllegalAccessException e) {
				throw new ExecIllegalAccessException(e.getMessage());
			} catch (InvocationTargetException e) {
				throw new ExecInvocationTargetException(e.getMessage());
			}
		}
		else {
			throw new AccessDeniedException("execute", this.getName()); 
		}
	}
	
}
