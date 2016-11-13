/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer.entity;

import java.io.Serializable;

/**
 * 語彙クラス。 MeCabの辞書に登録されている語彙を表すクラス。
 * 
 * @author kazuki
 */
public class Vocab implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 語彙ID。
	 */
	private Integer vocabId;

	/**
	 * 原形。
	 */
	private String proto;

	/**
	 * 品詞。
	 */
	private String pos;

	/**
	 * 品詞細分類1。
	 */
	private String pos1;

	/**
	 * 品詞細分類2。
	 */
	private String pos2;

	/**
	 * 品詞細分類3。
	 */
	private String pos3;

	/**
	 * 活用形。
	 */
	private String conj;

	/**
	 * 活用型。
	 */
	private String type;

	/**
	 * 読み。
	 */
	private String furi;

	/**
	 * 発音。
	 */
	private String pronun;

	public Integer getVocabId() {
		return vocabId;
	}

	public void setVocabId(Integer vocabId) {
		this.vocabId = vocabId;
	}

	public String getProto() {
		return proto;
	}

	public void setProto(String proto) {
		this.proto = proto;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPos1() {
		return pos1;
	}

	public void setPos1(String pos1) {
		this.pos1 = pos1;
	}

	public String getPos2() {
		return pos2;
	}

	public void setPos2(String pos2) {
		this.pos2 = pos2;
	}

	public String getPos3() {
		return pos3;
	}

	public void setPos3(String pos3) {
		this.pos3 = pos3;
	}

	public String getConj() {
		return conj;
	}

	public void setConj(String conj) {
		this.conj = conj;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFuri() {
		return furi;
	}

	public void setFuri(String furi) {
		this.furi = furi;
	}

	public String getPronun() {
		return pronun;
	}

	public void setPronun(String pronun) {
		this.pronun = pronun;
	}

}
