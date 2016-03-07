package pt.tecnico.myDrive;


import java.io.ByteArrayInputStream;
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
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.File;


public class Main {
	static final Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args) {
		System.out.println("Welcome to MyDrive!");
		
		try {
			if (args.length == 0) {
				setup();
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

    @Atomic
    public static void setup() {
        Manager m = Manager.getInstance();
        File file = new File();
        User user = new User();

        file.setManager(m);
        file.setUser(user);
        user.setManager(m);
    }

    @Atomic
    public static void xmlPrint() {
        log.trace("xmlPrint: " + FenixFramework.getDomainRoot());
		Document doc = Manager.getInstance().xmlExport();
		XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		try { xmlOutput.output(doc, new PrintStream(System.out));
		} catch (IOException e) { System.out.println(e); }
    }
}