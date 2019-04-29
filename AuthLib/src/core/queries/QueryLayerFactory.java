package core.queries;

public class QueryLayerFactory {
	
	

	public static QueryLayer getInstance() {
		return MockDB.getInstance();
	}

	
}
