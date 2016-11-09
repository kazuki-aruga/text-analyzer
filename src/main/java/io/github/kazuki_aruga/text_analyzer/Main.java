package io.github.kazuki_aruga.text_analyzer;

import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.Tagger;
import net.moraleboost.mecab.impl.StandardTagger;

/**
 * 
 * Hello world!
 */
public class Main {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		// Taggerを構築。
		// 引数には、MeCabのcreateTagger()関数に与える引数を与える。
		final Tagger tagger = new StandardTagger("");

		try {

			// バージョン文字列を取得
			System.out.println("MeCab version " + tagger.version());

			// Lattice（形態素解析に必要な実行時情報が格納されるオブジェクト）を構築
			final Lattice lattice = tagger.createLattice();

			final String[] sentences = {
					"当社を取り巻く事業環境は、同業他社による競争は依然激しいものの、コンピュータシステム(Internet of Things)の信頼性確保に関するニーズの高まり、企業業績の回復に伴う設備投資を背景に、堅調に推移しております。",
					"このような状況下で企業が必要とする人材の雇用不足は深刻な問題となっており、当社が取り組むべき重要な課題も、優秀な人材の確保であると認識しております。",
					"セグメント別における具体的な課題とその対処に向けた方針は以下のとおりであります。", "本日は晴天なり、本日は晴天なり。" };

			try {

				for (String sentence : sentences) {

					// 解析対象文字列をセット
					lattice.setSentence(sentence);

					// tagger.parse()を呼び出して、文字列を形態素解析する。
					tagger.parse(lattice);

					// 一つずつ形態素をたどりながら、表層形と素性を出力
					Node node = lattice.bosNode();

					while (node != null) {

						String surface = node.surface();
						String feature = node.feature();
						System.out.println(surface + "\t" + feature);
						node = node.next();
					}
				}

			} finally {

				// latticeを破壊
				lattice.destroy();
			}

		} finally {

			// taggerを破壊
			tagger.destroy();
		}
	}

}
