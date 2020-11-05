package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Tests a student record against a list of query filters.
 * @author sbolsec
 *
 */
public class QueryFilter implements IFilter {

	/** List of query filters **/
	private List<ConditionalExpression> list;
	
	/**
	 * Constructor which initializes the list of query filters.
	 * @param list
	 */
	public QueryFilter(List<ConditionalExpression> list) {
		this.list = list;
	}
	
	/**
	 * Tests the record against all the filters. If they all return true,
	 * the output is true, false otherwise.
	 * @param record student record to be tested
	 * @return true if all the filter returned true for the record, false otherwise
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		for (ConditionalExpression expr : list) {
			boolean res = expr.getComparisonOperator().satisfied(
					expr.getFieldGetter().get(record),
					expr.getStringLiteral()
			);
			if (!res)
				return false;
		}
		return true;
	}
}
