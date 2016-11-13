/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer.entity;

import java.io.Serializable;

/**
 * 単語クラス。文章中に出現した語彙の表層形を表すクラス。
 * 
 * @author kazuki
 */
public class Word implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 表層形。
	 */
	private String surface;

	/**
	 * 語彙。
	 */
	private Vocab vocab;

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public Vocab getVocab() {
		return vocab;
	}

	public void setVocab(Vocab vocab) {
		this.vocab = vocab;
	}

}
