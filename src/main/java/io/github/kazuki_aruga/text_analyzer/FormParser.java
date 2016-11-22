/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author k-aruga
 */
public class FormParser {

	private static final Log log = LogFactory.getLog(FormParser.class);

	/**
	 * 事業の状況を記載したシート名。
	 */
	private static Pattern[] bsSheetNames = { //
			Pattern.compile("事業の状況"), //
			Pattern.compile("業績等の概要"), //
			Pattern.compile("生産、受注及び販売の状況"), //
			Pattern.compile("対処すべき課題"), //
			Pattern.compile("事業等のリスク"), //
			Pattern.compile("経営上の重要な契約等"), //
			Pattern.compile("研究開発活動"), //
	};

	/**
	 * 事業の状況のセクション。
	 */
	private static Pattern[] bsSections = new Pattern[] { //
			Pattern.compile("^.*【業績等の概要】$"), //
			Pattern.compile("^.*【生産.*受注[及び|および]販売の状況】$"), //
			Pattern.compile("^.*【対処すべき課題】$"), //
			Pattern.compile("^.*【事業等のリスク】$"), //
			Pattern.compile("^.*【経営上の重要な契約等】$"), //
			Pattern.compile("^.*【研究開発活動】$"), //
			Pattern.compile("^.*【財政状態.*経営成績[及び|および]キャッシュ.*フローの状況の分析】$"), //
	};

	/**
	 * 会社名を取得する。
	 * 
	 * @param book
	 *            Excelワークブック。
	 * @return 会社名。見つからない場合は<code>null</code>。
	 */
	public static String getCompanyName(Workbook book) {

		final Sheet cover = findCover(book);
		if (cover == null) {

			return null;
		}

		for (Row row : cover) {

			boolean found = false;

			for (Cell cell : row) {

				if (found) {

					final String value = getValue(cell);
					if (value == null) {

						log.warn("会社名が見つかりませんでした。");
						return "";
					}

					return value;
				}

				if ("【会社名】".equals(getValue(cell))) {

					found = true;
				}
			}
		}

		log.warn("会社名が見つかりませんでした。");
		return "";
	}

	private static Sheet findCover(Workbook workbook) {

		for (Sheet sheet : workbook) {

			if (sheet.getSheetName().contains("表紙")) {

				return sheet;
			}
		}

		return null;
	}

	/**
	 * SPEEDAからダウンロードしたExcelの有価証券報告書・半期報告書・四半期報告書の「事業の状況」を解析する。
	 * 
	 * @param book
	 *            Excelファイル。
	 */
	public static BusinessSituation parseBusinessSituation(Workbook workbook) {

		// 「事業の状況」シートが存在するか確認する
		final List<Sheet> sheets = findSheets(workbook);
		if (sheets.isEmpty()) {

			log.warn("「事業の状況」シートが見つかりません。");
			return null;
		}

		// 「事業の状況」シートの内容を解析する
		int section = -1; // セクションのインデックス

		final List<String> issues = new ArrayList<>(); // 対処すべき課題
		final List<String> rd = new ArrayList<>(); // 研究開発活動

		for (Sheet sheet : sheets) {

			for (Row row : sheet) {

				for (Cell cell : row) {

					// セルの文字列を取得する
					final String value = getValue(cell);
					// 文字列が存在する場合
					if (value != null) {

						// セクションを調べる
						final int sectionIndex = getSectionIndex(value);
						if (-1 < sectionIndex) {

							section = sectionIndex;
							// セクションの行は含めない
							continue;
						}

						switch (section) {
						case 2: // 対処すべき課題

							issues.add(value);
							break;

						case 5: // 研究開発活動

							rd.add(value);
							break;

						default:
						}
					}
				}
			}
		}

		if (issues.isEmpty()) {

			log.error("３【対処すべき課題】が見つかりません。");
		}
		if (rd.isEmpty()) {

			log.error("６【研究開発活動】が見つかりません。");
		}

		// 結果を生成して返却する
		final BusinessSituation bs = new BusinessSituation();

		bs.setIssues(issues);
		bs.setRd(rd);

		return bs;

	}

	/**
	 * ワークブックから事業の状況が記載されたシートを検索する。
	 * 
	 * @param workbook
	 *            ワークブック。
	 * @return シートの一覧。
	 */
	private static List<Sheet> findSheets(Workbook workbook) {

		final List<Sheet> result = new ArrayList<>();

		for (Pattern sheetNamePattern : bsSheetNames) {

			for (Sheet sheet : workbook) {

				final String sheetName = sheet.getSheetName();

				if (sheetNamePattern.matcher(sheetName).matches()) {

					result.add(sheet);
					break;
				}
			}
		}

		return result;
	}

	/**
	 * 出力すべきセルかどうかを判定する。
	 * 
	 * @param cell
	 *            セル。
	 * @return 出力すべき場合は<code>true</code>。
	 */
	private static String getValue(Cell cell) {

		// 文字列ではないセルは出力しない
		if (cell.getCellType() != Cell.CELL_TYPE_STRING) {

			return null;
		}

		// 罫線が引かれているセルは出力しない
		// if (cell.getCellStyle().getBorderLeft() != CellStyle.BORDER_NONE) {
		//
		// return null;
		// }

		final String value = formatValue(cell.getStringCellValue());

		if (value.isEmpty()) {

			return null;
		}

		return value;
	}

	/**
	 * 出力する文字列を整形する。
	 * 
	 * @param stringCellValue
	 *            セルの文字列。
	 * @return 整形済み文字列。
	 */
	private static String formatValue(String stringCellValue) {

		// nbspをスペースに変換し、前後をトリムする
		return stringCellValue.replace('\u00A0', ' ').trim();
	}

	/**
	 * セクション区切りかどうか判定し、セクション区切りの場合、セクションインデックスを返す。
	 * 
	 * @param value
	 *            文字列。
	 * @return セクションインデックス。セクション区切りではない場合は<code>-1</code>。
	 */
	private static int getSectionIndex(String value) {

		for (int i = 0; i < bsSections.length; i++) {

			if (bsSections[i].matcher(value).find()) {

				return i;
			}
		}

		return -1;
	}

}
