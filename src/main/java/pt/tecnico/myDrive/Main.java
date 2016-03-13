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
import pt.tecnico.myDrive.domain.File;


public class Main {
	static final Logger log = LogManager.getRootLogger();
	
	public static void main(String[] args){
		System.out.println("*** Welcome to MyDrive! ***");
		
		try {
			if (args.length == 0) {
				setup();
			}
			else {
				/*for (String s: args) {
	    			xmlScan(new java.io.File(s));
	    		}
				print();*/
			}
			
		} finally { FenixFramework.shutdown(); }
	}
	
    @Atomic
    public static void setup() {

    	//DAVID
    	//Manager.getInstance().createNewUser("root");
    	//System.out.println(Manager.getInstance().getHomeDirectory().getParent().getName());
    	//System.out.println(Manager.getInstance().getHomeDirectory().getName());
    	//Manager.getInstance().getHomeDirectory().lsDir();
    	//Manager.getInstance().getHomeDirectory().getFileByName("README10").remove();
    	//Manager.getInstance().getHomeDirectory().getFileByName("README3").showContent();
    	//Manager.getInstance().getHomeDirectory().createPlainFile("README3", Manager.getInstance(), Manager.getInstance().getUserByUsername("root"),"banana3");
    	//Manager.getInstance().getHomeDirectory().createDirectory("DA\0", Manager.getInstance(), Manager.getInstance().getUserByUsername("root"));
    	//DAVID
    	
    	
    	log.trace("[Main:setup] Home = " + Manager.getInstance().getHomeDirectory().getName());
    	log.trace("[Main:setup] Total n of users = " + Manager.getInstance().getUserSet().size());
    	log.trace("[Main:setup] Total n of files = " + Manager.getInstance().getFileSet().size());   
    	
    }
}
    /* C
=======
    	Manager.getInstance().createNewUser("user2");
    	log.trace("[Main:setup] Root directory = " + Manager.getInstance().getRootDirectory().getName());
    	log.trace("[Main:setup] Home = " + Manager.getInstance().getHomeDirectory().getName());
    	log.trace("[Main:setup] Total n of users = " + Manager.getInstance().getUserSet().size());
    	log.trace("[Main:setup] Total n of files = " + Manager.getInstance().getFileSet().size());

    	xmlPrint();
    }




	@Atomic
	public static void print() {
		log.trace("Print: " + FenixFramework.getDomainRoot());
		Manager manager = Manager.getInstance();

		for (User u: manager.getUserSet()) {
			System.out.println("User:" + u.getName()+ " has " + u.getFileSet().size() + " files:");
			for (File f: u.getFileSet())
				System.out.println("\t" + f.getName() + " -> " + f.getAbsolutePath());
		}
	}

>>>>>>> 33ce48f36d91905f5933429da453bf3106af889e
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
    
}*/