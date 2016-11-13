/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 有価証券報告書クラス。
 * 
 * @author kazuki
 */
public class Report implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 企業。
	 */
	private Company company;

	/**
	 * 年度。
	 */
	private int year;

	/**
	 * 対処すべき課題。
	 */
	private List<Word> issues;

	/**
	 * 研究開発活動。
	 */
	private List<Word> rd;

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<Word> getIssues() {
		return issues;
	}

	public void setIssues(List<Word> issues) {
		this.issues = issues;
	}

	public List<Word> getRd() {
		return rd;
	}

	public void setRd(List<Word> rd) {
		this.rd = rd;
	}

}
