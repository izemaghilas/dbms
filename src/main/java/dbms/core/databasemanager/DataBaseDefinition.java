package dbms.core.databasemanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import dbms.core.Constants;

public enum DataBaseDefinition {
	INSTANCE;
	
	private List<RelationDefinition> relations;
	private final static String CATALOG_FILE_NAME = "Catalog.def";
	
	public void init() throws FileNotFoundException, IOException, ClassNotFoundException {
		this.relations = new ArrayList<RelationDefinition>();
		
		// deserialize
		if( Files.exists(Paths.get(Constants.DB_DIRECTORY+"/"+CATALOG_FILE_NAME)) ) {
			ObjectInputStream deserializer = new ObjectInputStream( new FileInputStream(new File(Constants.DB_DIRECTORY+"/"+CATALOG_FILE_NAME)));
			deserializer.readObject();
			this.read(deserializer);
			
			deserializer.close();
		}
	}
	
	private void read(ObjectInputStream deserializer) throws IOException, ClassNotFoundException {
		int numberOfRelations = deserializer.readInt();
		for(int i=0; i < numberOfRelations; i++) {
			RelationDefinition relation = (RelationDefinition) deserializer.readObject();
			this.relations.add(relation);
		}
	}
	
	public List<RelationDefinition> getRelations() {
		return relations;
	}

	public void setRelations(List<RelationDefinition> relations) {
		this.relations = relations;
	}
	
	public void addRelation(RelationDefinition relation) {
		this.relations.add(relation);
	}
	
	public int getNumberOfRelation() {
		return relations.size();
	}

	void finish() throws IOException {
		
		if(Files.notExists(Paths.get(Constants.DB_DIRECTORY+"/"+CATALOG_FILE_NAME))) {
			Files.createFile(Paths.get(Constants.DB_DIRECTORY+"/"+CATALOG_FILE_NAME));
		}
		
		ObjectOutputStream serializer = new ObjectOutputStream(new FileOutputStream(new File(Constants.DB_DIRECTORY+"/"+CATALOG_FILE_NAME)));
		serializer.writeObject(INSTANCE);
		this.write(serializer);
		
		serializer.close();
	}
	
	private void write(ObjectOutputStream serializer) throws IOException {
		serializer.writeInt(this.relations.size());
		for(RelationDefinition relation : this.relations) {
			serializer.writeObject(relation);
		}
	}
}
