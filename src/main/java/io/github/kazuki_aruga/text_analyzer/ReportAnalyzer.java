/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer;

import java.io.File;
import java.io.Reader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Pattern;

import io.github.kazuki_aruga.text_analyzer.entity.Company;
import io.github.kazuki_aruga.text_analyzer.entity.Report;

/**
 * 有価証券報告書解析クラス。
 * 
 * @author k-aruga
 */
public class ReportAnalyzer {

	private static final Pattern pattern = Pattern.compile(
			"^有価証券報告書第[0-9]{1,2}期（(平成[0-9]{1,2}年[0-9]{1,2}月[0-9]{1,2}日)-(平成[0-9]{1,2}年[0-9]{1,2}月[0-9]{1,2}日)）[.]txt$");

	private static final DateFormat japaseseFormat = new SimpleDateFormat("GGGGy年M月d日", new Locale("ja", "JP", "JP"));

	public static Report analyze(Company company, int year, Reader reader) {

		final Company comp = new Company();

		return null;
	}

}
