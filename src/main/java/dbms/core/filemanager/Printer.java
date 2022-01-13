package dbms.core.filemanager;

import java.util.ArrayList;
import java.util.List;

import dbms.core.databasemanager.RelationDefinition;

public class Printer {
	
	private Printer() {
		
	}
	
	private static class Column {
		private String name;
		private List<String> rows;
		private int length; // the biggest string in term of length
		
		private Column(String name, List<String> rows, int length) {
			this.name = name;
			this.rows = rows;
			this.length = length;
		}
		
		
	}
	
	public static String print(RelationDefinition relation, List<Record> records) {
		String str = "\nTable : "+relation.getName();
		
		if(!records.isEmpty()) {
			List<Column> columns = new ArrayList<Printer.Column>();
			// column name is the column type
			List<String> types = relation.getColumnTypes();
			
			for(int i=0; i<types.size(); i++) {
				columns.add( getColumn(types.get(i), records, i) );
			}
			
			str += buildHeader(columns)+"\n";
			str += buildRows(columns, records.size());	
			for(Column column : columns) {
				str += buildLine(column.length);
			}
			str += "+";
		}
		str += "\nNumber of records : "+records.size()+"\n";
		
		return str;
	}
	
	private static Column getColumn(String name, List<Record> records, int index) {
		int maxLength = name.length();
		List<String> rows = new ArrayList<String>();
		for(Record record: records) {
			String row = record.getValues().get(index);
			if(row.length() > maxLength) {
				maxLength = row.length();
			}
			rows.add( record.getValues().get(index) );
		}
		
		return new Printer.Column(name, rows, maxLength);
	}
	
	private static String buildHeader(List<Column> columns) {
		String header = "";
		String line = "";
		String name = "";
		
		for(Column column : columns) {
			line+=buildLine(column.length);
		}
		line+="+";
		
		for(Column column : columns) {
			name+=buildRow(column.name, column.length)+" ";
		}		
		name+="|";
		return header+"\n"+line+"\n"+name+"\n"+line;
	}
	
	private static String buildRows(List<Column> columns, int numberOfRows) {
		String rows = "";
		
		for(int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
			for(Column column : columns) {
				String slotValue = column.rows.get(rowIndex);
				rows+=buildRow(slotValue, column.length)+" ";
			}
			rows+="|\n";
		}
		
		return rows;
	}
	
	private static String buildLine(int length) {
		String line="+";
		for(int i=0; i<length+2; i++) {
			line+="-";
		}
		return line;
	}
	
	private static String buildRow(String value, int length) {
		String row = "| ";
		row+=value;
		for(int i=0; i<length-value.length(); i++) {
			row+=" ";
		}
		return row;
	}
}
