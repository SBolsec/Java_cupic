package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A node representing an entire document.
 * 
 * @author sbolsec
 *
 */
public class DocumentNode extends Node {
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof DocumentNode)) return false;
		DocumentNode other = (DocumentNode) obj;
		if (this.children.size() != other.children.size())
			return false;
		for (int i = 0, n = children.size(); i < n; i++) {
			if (!this.children.get(i).equals(other.children.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.children == null) return sb.append("").toString();
		for (int i = 0, n = children.size(); i < n; i++) {
			sb.append(this.children.get(i).toString());
		}
		return sb.toString();
	}
}
