/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer;

import java.io.Serializable;
import java.util.List;

/**
 * 有価証券報告書の「事業の状況」をあらわすクラス。
 * 
 * @author k-aruga
 */
public class BusinessSituation implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 対処すべき課題。
	 */
	private List<String> issues;

	/**
	 * 研究開発活動。
	 */
	private List<String> rd;

	/**
	 * @return the issues
	 */
	public List<String> getIssues() {
		return issues;
	}

	/**
	 * @param issues
	 *            the issues to set
	 */
	public void setIssues(List<String> issues) {
		this.issues = issues;
	}

	/**
	 * @return the rd
	 */
	public List<String> getRd() {
		return rd;
	}

	/**
	 * @param rd
	 *            the rd to set
	 */
	public void setRd(List<String> rd) {
		this.rd = rd;
	}

}
