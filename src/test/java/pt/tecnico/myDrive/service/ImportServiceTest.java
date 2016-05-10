package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;
import pt.tecnico.myDrive.domain.Manager;
import org.jdom2.Document;

import java.io.IOException;
import java.io.StringReader;

public class ImportServiceTest extends AbstractServiceTest{

	private final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
	+"<myDrive>"
	+"<user username=\"jtb\">"
	+"  <password>fermento</password>"
	+"  <name>Joaquim Teófilo Braga</name>"
	+"  <home>/home/jtb</home>"
	+"  <mask>rwxdr-x-</mask>"
	+"</user>"
	+"<plain id=\"3\">"
	+"  <path>/home/jtb</path>"
	+"  <name>profile</name>"
	+"  <owner>jtb</owner>"
	+"  <perm>rw-dr---</perm>"
	+"  <contents>Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República.</contents>"
	+"</plain>"
	+"<dir id=\"4\">"
	+"  <path>/home/jtb</path>"
	+"  <name>documents</name>"
	+"  <owner>jtb</owner>"
	+"  <perm>rwxdr-x-</perm>"
	+"</dir>"
	+"<link id=\"5\">"
	+"  <path>/home/jtb</path>"
	+"  <name>doc</name>"
	+"  <owner>jtb</owner>"
	+"  <perm>r-xdr-x-</perm>"
	+"  <value>/home/jtb/documents</value>"
	+"</link>"
	+"<dir id=\"7\">"
	+"  <path>/home/jtb</path>"
	+"  <owner>jtb</owner>"
	+"  <name>bin</name>"
	+"  <perm>rwxd--x-</perm>"
	+"</dir>"
	+"</myDrive>";


	@Override
	protected void populate() {
		Manager manager = Manager.getInstance();
	}

	@Test
	public void success() throws JDOMException, IOException {
		Document doc = new SAXBuilder().build(new StringReader(xml));
		ImportService service = new ImportService(doc);
		service.execute();

		Manager manager = Manager.getInstance();

		assertTrue(manager.hasUser("jtb"));
		assertEquals(manager.fetchUser("jtb", "fermento").getHome().getFileByName("profile").read(manager.fetchUser("jtb", "fermento")), "Primeiro chefe de Estado do regime republicano (acumulando com a chefia do governo), numa capacidade provisória até à eleição do primeiro presidente da República.");
		assertEquals(manager.fetchUser("jtb", "fermento").getHome().getFileByName("documents").getPermissions(), "rwxdr-x-");
		assertEquals(manager.fetchUser("jtb", "fermento").getHome().getFileByName("doc").read(manager.fetchUser("jtb", "fermento")), "/home/jtb/documents");
		assertEquals(manager.fetchUser("jtb", "fermento").getHome().getFileByName("bin").getPermissions(), "rwxd--x-");
	}



}
