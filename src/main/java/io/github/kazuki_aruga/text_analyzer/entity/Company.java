/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer.entity;

import java.io.Serializable;

/**
 * @author kazuki
 */
public class Company implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 企業コード。
	 */
	private String compCode;

	/**
	 * 企業名。
	 */
	private String compName;

	/**
	 * JISAに加入しているかどうか。
	 */
	private boolean jisa;

	public String getCompCode() {
		return compCode;
	}

	public void setCompCode(String compCode) {
		this.compCode = compCode;
	}

	public String getCompName() {
		return compName;
	}

	public void setCompName(String compName) {
		this.compName = compName;
	}

	public boolean isJisa() {
		return jisa;
	}

	public void setJisa(boolean jisa) {
		this.jisa = jisa;
	}

}
