package hr.fer.oprpp1.hw04.db;

/**
 * Allows conditional expressions to be created.
 * @author sbolsec
 *
 */
public class ConditionalExpression {

	/** Value getter **/
	private IFieldValueGetter fieldGetter;
	/** String literal **/
	private String stringLiteral;
	/** Comparison **/
	private IComparisonOperator comparisonOperator;
	
	/**
	 * Constructor which initializes all the properties.
	 * @param valueGetter value getter implementation
	 * @param literal string literal
	 * @param comparison comparison implementation
	 */
	public ConditionalExpression(IFieldValueGetter fieldGetter, String stringLiteral, IComparisonOperator comparisonOperator) {
		super();
		this.fieldGetter = fieldGetter;
		this.stringLiteral = stringLiteral;
		this.comparisonOperator = comparisonOperator;
	}

	/**
	 * Returns value getter.
	 * @return value getter
	 */
	public IFieldValueGetter getFieldGetter() {
		return fieldGetter;
	}

	/**
	 * Returns string literal.
	 * @return string literal
	 */
	public String getStringLiteral() {
		return stringLiteral;
	}

	/**
	 * Returns comparison.
	 * @return comparison
	 */
	public IComparisonOperator getComparisonOperator() {
		return comparisonOperator;
	}
}
