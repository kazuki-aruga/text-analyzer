/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.kazuki_aruga.text_analyzer.entity.Vocab;
import io.github.kazuki_aruga.text_analyzer.entity.Word;
import net.moraleboost.mecab.Lattice;
import net.moraleboost.mecab.Node;
import net.moraleboost.mecab.impl.StandardTagger;

/**
 * @author k-aruga
 *
 */
public final class MorphologicalAnalyzer {

	/**
	 * 
	 */
	private MorphologicalAnalyzer() {

	}

	/**
	 * 形態素解析を行う。
	 * 
	 * @param sentences
	 *            文のリスト。
	 * @return 単語のリスト。
	 */
	public static List<Word> analyze(List<String> sentences) {

		// Taggerを構築。
		// 引数には、MeCabのcreateTagger()関数に与える引数を与える。
		final StandardTagger engine = new StandardTagger("");

		try {

			// Lattice（形態素解析に必要な実行時情報が格納されるオブジェクト）を構築
			final Lattice lattice = engine.createLattice();

			try {

				final List<Word> words = new ArrayList<>();

				for (String sentence : sentences) {

					// 解析対象文字列をセット
					lattice.setSentence(sentence);

					// tagger.parse()を呼び出して、文字列を形態素解析する。
					engine.parse(lattice);

					// 一つずつ形態素をたどりながら、表層形と素性を出力
					Node node = lattice.bosNode().next();

					while (node != null && node.stat() != Node.TYPE_EOS_NODE) {

						final Word word = new Word();

						word.setSurface(node.surface());
						word.setVocab(parseFeature(node.feature()));

						words.add(word);

						node = node.next();
					}

				}

				return words;

			} finally {

				// latticeを破壊
				lattice.destroy();
			}

		} finally {

			// taggerを破壊
			engine.destroy();
		}

	}

	/**
	 * 語彙を返す。
	 * 
	 * @param feature
	 *            語彙。
	 * @return 語彙。
	 */
	private static Vocab parseFeature(String feature) {

		final Vocab vocab = new Vocab();

		final String[] features = new String[9];
		Arrays.fill(features, "*");
		final String[] values = feature.split(",");
		System.arraycopy(values, 0, features, 0, values.length);

		vocab.setPos(features[0]);
		vocab.setPos1(features[1]);
		vocab.setPos2(features[2]);
		vocab.setPos3(features[3]);
		vocab.setConj(features[4]);
		vocab.setType(features[5]);
		vocab.setProto(features[6]);
		vocab.setFuri(features[7]);
		vocab.setPronun(features[8]);

		return vocab;
	}

}
