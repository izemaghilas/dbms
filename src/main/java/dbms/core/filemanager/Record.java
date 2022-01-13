package dbms.core.filemanager;

import java.util.ArrayList;
import java.util.List;

public class Record {
	private List<String> values;
	
	public Record() {
		this.values = new ArrayList<String>();
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		String str = "";
		for(String value: this.values) {
			str+="| "+value+" ";
		}
		str+="|";
		return str;
	}
}
