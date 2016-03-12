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


public class Main {
	static final Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args){
		System.out.println("*** Welcome to MyDrive! ***");
		
		try {
			if (args.length == 0) {
				setup();
			}
			else {
				for (String s: args) {
	    			xmlScan(new java.io.File(s));
	    		}
			}
			
		} finally { FenixFramework.shutdown(); }
		
	}
	
    @Atomic
    public static void setup() {
    	Manager.getInstance().createNewUser("user2");
    	log.trace("[Main:setup] Root directory = " + Manager.getInstance().getRootDirectory().getName());
    	log.trace("[Main:setup] Home = " + Manager.getInstance().getHomeDirectory().getName());
    	log.trace("[Main:setup] Total n of users = " + Manager.getInstance().getUserSet().size());
    	log.trace("[Main:setup] Total n of files = " + Manager.getInstance().getFileSet().size());
    	xmlPrint();
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