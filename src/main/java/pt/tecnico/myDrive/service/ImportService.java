package pt.tecnico.myDrive.service;


import org.jdom2.Document;

public class ImportService extends MyDriveService {
	private final Document doc;

	public ImportService(Document doc) {
		this.doc = doc;
	}

	@Override
	protected void dispatch() {
		getManager().xmlImport(doc.getRootElement());
	}
}
