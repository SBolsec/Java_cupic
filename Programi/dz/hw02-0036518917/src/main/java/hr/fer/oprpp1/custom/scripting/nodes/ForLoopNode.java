package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct
 * 
 * @author sbolsec
 *
 */
public class ForLoopNode extends Node {
	
	/** Variable of the for-loop **/
	private ElementVariable variable;
	/** Start expression of the for-loop **/
	private Element startExpression;
	/** End expression of the for-loop **/
	private Element endExpression;
	/** Stop expression of the for-loop, can be null **/
	private Element stepExpressiom;
	
	/**
	 * Constructor which initializes all the properties
	 * @param variable variable of the for-loop
	 * @param startExpression start expression of the for-loop
	 * @param endExpression end expression of the for-loop
	 * @param stepExpression stop expression of the for-loop
	 */
	public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
		this.variable = variable;
		this.startExpression = startExpression;
		this.endExpression = endExpression;
		this.stepExpressiom = stepExpression;
	}
	
	/**
	 * Returns the variable
	 * @return variable
	 */
	public ElementVariable getVariable() {
		return variable;
	}
	
	/**
	 * Returns the start expression
	 * @return start expression
	 */
	public Element getStartExpression() {
		return startExpression;
	}
	
	/**
	 * Returns the end expression
	 * @return end expression
	 */
	public Element getEndExpression() {
		return endExpression;
	}
	
	/**
	 * Returns the step expression
	 * @return step expression
	 */
	public Element getStepExpression() {
		return stepExpressiom;
	}
	
//	@Override
//	public boolean equals(Object obj) {
//		
//	}
//	
//	@Override
//	public String toString() {
//		
//	}
}
