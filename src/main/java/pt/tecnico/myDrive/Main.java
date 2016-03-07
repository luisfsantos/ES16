package pt.tecnico.myDrive;

import java.io.File;
import java.io.PrintStream;

import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Manager;

//Imports Miguel
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom2.Document;

public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to MyDrive!");
		
		try {
			if (args.length == 0) {
				Manager m = Manager.getInstance();
			
			}
			else {
				// import
				for (String s: args){
	    			xmlScan(new File(s));
	    		}
			}
			
		} finally { FenixFramework.shutdown(); }
		
	}

	@Atomic
    public static void xmlScan(File file) {
        log.trace("xmlScan: " + FenixFramework.getDomainRoot());    //falta logger
	Manager manager = Manager.getInstance();
	SAXBuilder builder = new SAXBuilder();
	try {
	    Document document = (Document)builder.build(file);
	    manager.xmlImport(document.getRootElement());
	} catch (JDOMException | IOException e) {
	    e.printStackTrace();
	}
    }

}