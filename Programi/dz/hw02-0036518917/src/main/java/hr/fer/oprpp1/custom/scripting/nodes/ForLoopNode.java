package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * A node representing a single for-loop construct
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
	private Element stepExpression;
	
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
		this.stepExpression = stepExpression;
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
		return stepExpression;
	}

	/**
	 * Two for loops are equal if they have the same signature, ie. variable name, 
	 * start expression, end expression and stop expression, and have the exact
	 * same children nodes.
	 * @param obj object to be tested
	 * @return true if the for loop nodes are equal, false otherwise
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ForLoopNode)) return false;
		ForLoopNode other = (ForLoopNode) obj;
		if (!this.variable.equals(other.variable) ||
				!this.startExpression.equals(other.startExpression) ||
				!this.endExpression.equals(other.endExpression))
			return false;
		if (this.stepExpression != null && other.stepExpression != null) {
			if (!this.stepExpression.equals(other.stepExpression)) {
				return false;
			}
		} else if ((this.stepExpression == null && other.stepExpression != null)
				|| (this.stepExpression != null && other.stepExpression == null)) {
			return false;
		}
		if ((this.children == null && other.children != null) ||
				(this.children != null && other.children == null)) {
			return false;
		}
		if (this.children == null && other.children == null) {
			return true;
		}
		if (this.children.size() != other.children.size()) {
			return false;
		}
		for (int i = 0, n = children.size(); i < n; i++) {
			if (!this.children.get(0).equals(other.children.get(0))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Returns string representation of for loop node and all of its children nodes.
	 * @return string representation of for loop node
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{$ FOR ").append(variable).append(' ');
		
		// If there is a string, insert the escaping characters
		if (startExpression instanceof ElementString) {
			sb.append(generateStringInsideTag(startExpression.toString())).append(' ');
		} else {
			sb.append(startExpression).append(' ');
		}
		if (endExpression instanceof ElementString) {
			sb.append(generateStringInsideTag(endExpression.toString())).append(' ');
		} else {
			sb.append(endExpression).append(' ');
		}
		if (stepExpression != null) {
			if (stepExpression instanceof ElementString) {
				sb.append(generateStringInsideTag(stepExpression.toString())).append(' ');
			} else {
				sb.append(stepExpression).append(' ');
			}
		}
		sb.append("$}");
		if (!children.isEmpty()) {
			for (int i = 0, n = children.size(); i < n; i++) {
				sb.append(children.get(i).toString());
			}
		}
		sb.append("{$ END $}");
		return sb.toString();
	}
}
