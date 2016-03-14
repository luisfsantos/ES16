package pt.tecnico.myDrive;

import java.io.IOException;
import java.io.PrintStream;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.tecnico.myDrive.domain.Manager;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Directory;
import pt.tecnico.myDrive.domain.File;


public class Main {
	static final Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args){
		
		try {
			if (args.length == 0) {
				setup();
			}
			else {
				for (String s: args) {
	    			xmlScan(new java.io.File(s));
	    		}
				print();
			}
			
		} finally { FenixFramework.shutdown(); }
	}
	
    @Atomic
    public static void setup() {	
    	String lista = "";
    	for (User u: Manager.getInstance().getUserSet()) {
    		lista += u.getUsername()+ "\n";
    	}
    	Manager.getInstance().getHomeDirectory().createPlainFile("README", lista);
    	Manager.getInstance().getHomeDirectory().getFileByName("README").showContent();
    	Manager.getInstance().getRootDirectory().createDirectory("usr").createDirectory("local").createDirectory("bin");
    	Manager.getInstance().getRootDirectory().lookup("/usr/local/bin").remove();
    	xmlPrint();
    	Manager.getInstance().getRootDirectory().lookup("/home/README").remove();
    	Manager.getInstance().getHomeDirectory().showContent();
    }




	@Atomic
	public static void print() {
		log.trace("Print: " + FenixFramework.getDomainRoot());
		Manager manager = Manager.getInstance();

		for (User u: manager.getUserSet()) {
			System.out.println("User:" + u.getName()+ " has " + u.getFileSet().size() + " files:");
			for (File f: u.getFileSet())
				System.out.println("\t" + f.getName() + " contains.... ");
		}
	}


	@Atomic
    public static void xmlScan(java.io.File file) {
        log.trace("xmlScan: " + FenixFramework.getDomainRoot());  
		Manager manager = Manager.getInstance();
		SAXBuilder builder = new SAXBuilder();
		try {
		    Document document = (Document)builder.build(file);
		    manager.xmlImport(document.getRootElement());
		} catch (JDOMException | IOException e) {
		    e.printStackTrace();
		}
	}

    @Atomic
    public static void xmlPrint() {
        log.trace("[Main:xmlPrint] " + FenixFramework.getDomainRoot());
		Document doc = Manager.getInstance().xmlExport();
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try { xmlOutput.output(doc, new PrintStream(System.out));
		} catch (IOException e) { System.out.println(e); }
    }
    
}