/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import io.github.kazuki_aruga.text_analyzer.entity.Company;
import io.github.kazuki_aruga.text_analyzer.entity.Report;

/**
 * 有価証券報告書解析クラス。
 * 
 * @author k-aruga
 */
public class ReportAnalyzer {

	private static final Pattern pattern = Pattern.compile(
			"^[^_]+_有価証券報告書第[0-9]{1,2}期（(平成[0-9]{1,2}年[0-9]{1,2}月[0-9]{1,2}日)-(平成[0-9]{1,2}年[0-9]{1,2}月[0-9]{1,2}日)）[.]xls$");

	private static final DateFormat japaneseFormat = new SimpleDateFormat("GGGGy年M月d日", new Locale("ja", "JP", "JP"));

	/**
	 * 
	 * @param compCode
	 *            企業コード。
	 * @param pathname
	 *            有価証券報告書のパス。
	 * @return 有価証券報告書オブジェクト。
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	public static Report analyze(String compCode, String pathname)
			throws EncryptedDocumentException, InvalidFormatException, IOException {

		final File file = new File(pathname);
		if (!file.exists() || !file.isFile()) {

			return null;
		}

		final Matcher matcher = pattern.matcher(file.getName());
		if (!matcher.find()) {

			return null;
		}

		final int year = getYear(matcher.group(2));
		if (year == 0) {

			return null;
		}

		try (Workbook book = WorkbookFactory.create(file)) {

			final BusinessSituation bs = FormParser.parseBusinessSituation(book);

			final Company company = new Company();

			company.setCompName(FormParser.getCompanyName(book));
			company.setCompCode(compCode);

			final Report report = new Report();

			report.setCompany(company);
			report.setYear(year);
			report.setIssues(MorphologicalAnalyzer.analyze(bs.getIssues()));
			report.setRd(MorphologicalAnalyzer.analyze(bs.getRd()));

			return report;
		}
	}

	/**
	 * 
	 * @param gdate
	 * @return
	 */
	private static int getYear(String gdate) {

		try {

			final Date date = japaneseFormat.parse(gdate);
			return date.getYear() + 1900;

		} catch (ParseException e) {

			return 0;
		}
	}

}
