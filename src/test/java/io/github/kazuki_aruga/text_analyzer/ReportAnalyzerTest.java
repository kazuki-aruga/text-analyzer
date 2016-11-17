package io.github.kazuki_aruga.text_analyzer;

import org.junit.Assert;
import org.junit.Test;

import io.github.kazuki_aruga.text_analyzer.entity.Report;

public class ReportAnalyzerTest {

	@Test
	public void testAnalyze1() throws Exception {

		final Report report = ReportAnalyzer.analyze("4699",
				"C:\\Temp\\Ｊ－ウチダエスコ_有価証券報告書第43期（平成26年7月21日-平成27年7月20日）.xls");

		Assert.assertNotNull(report);
		Assert.assertNotNull(report.getCompany());
		Assert.assertEquals("4699", report.getCompany().getCompCode());
		Assert.assertEquals("ウチダエスコ株式会社", report.getCompany().getCompName());
		Assert.assertEquals(2015, report.getYear());
		Assert.assertNotNull(report.getIssues());
		Assert.assertFalse(report.getIssues().isEmpty());
		Assert.assertNotNull(report.getRd());
		Assert.assertFalse(report.getRd().isEmpty());
	}

	@Test
	public void testAnalyze2() throws Exception {

		final Report report = ReportAnalyzer.analyze("4709", "C:\\Temp\\ＩＤ_有価証券報告書第34期（平成13年4月1日-平成14年3月31日）.xls");

		Assert.assertNotNull(report);
		Assert.assertNotNull(report.getCompany());
		Assert.assertEquals("4709", report.getCompany().getCompCode());
		Assert.assertEquals("株式会社インフォメーション・ディベロプメント", report.getCompany().getCompName());
		Assert.assertEquals(2002, report.getYear());
		Assert.assertNotNull(report.getIssues());
		Assert.assertFalse(report.getIssues().isEmpty());
		Assert.assertNotNull(report.getRd());
		Assert.assertFalse(report.getRd().isEmpty());
	}

}
