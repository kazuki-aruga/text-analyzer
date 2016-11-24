package io.github.kazuki_aruga.text_analyzer;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Assert;
import org.junit.Test;

public class FormParserTest {

	@Test
	public void testParseBusinessSituation() throws EncryptedDocumentException, InvalidFormatException, IOException {

		try (Workbook workbook = WorkbookFactory
				.create(new File("C:\\Temp\\ＩＤ_有価証券報告書第34期（平成13年4月1日-平成14年3月31日）.xls"))) {

			final BusinessSituation bs = FormParser.parseBusinessSituation(workbook);

			Assert.assertFalse(bs.getIssues().isEmpty());
			Assert.assertFalse(bs.getRd().isEmpty());
		}

		try (Workbook workbook = WorkbookFactory
				.create(new File("C:\\Temp\\Ｊ－ウチダエスコ_有価証券報告書第43期（平成26年7月21日-平成27年7月20日）.xls"))) {

			final BusinessSituation bs = FormParser.parseBusinessSituation(workbook);

			Assert.assertFalse(bs.getIssues().isEmpty());
			Assert.assertFalse(bs.getRd().isEmpty());
		}
	}

	@Test
	public void testParseCompanyName() throws EncryptedDocumentException, InvalidFormatException, IOException {

		try (Workbook workbook = WorkbookFactory
				.create(new File("C:\\Temp\\ＩＤ_有価証券報告書第34期（平成13年4月1日-平成14年3月31日）.xls"))) {

			Assert.assertNotNull(FormParser.getCompanyName(workbook));
		}

		try (Workbook workbook = WorkbookFactory
				.create(new File("C:\\Temp\\Ｊ－ウチダエスコ_有価証券報告書第43期（平成26年7月21日-平成27年7月20日）.xls"))) {

			Assert.assertNotNull(FormParser.getCompanyName(workbook));
		}
	}

	@Test
	public void testMatcher() {

		Pattern pattern = Pattern.compile("生産.*受注(及び|および)販売の状況");
		Assert.assertTrue(pattern.matcher("生産、受注および販売の状況").matches());

		pattern = Pattern.compile("^.*【財政状態.*の分析】$");
		Assert.assertTrue(pattern.matcher("2. 【財政状態の状況の分析】").matches());
	}

}
