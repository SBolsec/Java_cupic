package hr.fer.oprpp1.hw04.db;

import java.util.List;

public class QueryParser {
	
	public boolean isDirectQuery() {
		
	}
	
	public String getQueriedJMBAG() {
		if (!isDirectQuery()) throw new IllegalStateException("The query was not a direct one!");
	}

	public List<ConditionalExpression> getQuery() {
		
	}
}
