package io.github.kazuki_aruga.text_analyzer.entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataAccessTest {

	private static Connection conn;

	private DataAccess target;

	@BeforeClass
	public static void beforeClass() throws SQLException {

		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/text-analyzer?autoReconnect=true&useSSL=false",
				"text-analyzer", "text-analyzer");
	}

	@Before
	public void setUp() throws SQLException {

		target = new DataAccess(conn);
		target.beginTransaction();
	}

	@After
	public void tearDown() throws SQLException {

		target.rollback();
		target = null;
	}

	@AfterClass
	public static void afterClass() throws SQLException {

		conn.close();
	}

	@Test
	public void testsExistCompany() throws SQLException {

		final Company company = new Company();

		company.setCompCode("9999");

		Assert.assertFalse(target.existsCompany(company));
	}

	@Test
	public void testExistsVocab() throws SQLException {

		final Vocab vocab = new Vocab();

		vocab.setProto("テスト");
		vocab.setPos("テスト");
		vocab.setPos1("テスト1");
		vocab.setPos2("テスト2");
		vocab.setPos3("テスト3");

		Assert.assertFalse(target.existsVocab(vocab));
	}

	@Test
	public void testSaveVocab() throws SQLException {

		final Vocab vocab = new Vocab();

		vocab.setProto("テスト");
		vocab.setPos("テスト");
		vocab.setPos1("テスト1");
		vocab.setPos2("テスト2");
		vocab.setPos3("テスト3");
		vocab.setConj("テスト");
		vocab.setType("テスト");
		vocab.setFuri("テスト");
		vocab.setPronun("テスト");

		target.saveVocab(vocab);

		Assert.assertNotNull(vocab.getVocabId());
	}

	@Test
	public void testExistsReport() throws SQLException {

		final Company company = new Company();

		company.setCompCode("9999");

		final Report report = new Report();

		report.setCompany(company);
		report.setYear(2000);

		Assert.assertFalse(target.existsReport(report));
	}

	@Test
	public void testMergeReport() throws SQLException {

		final Company company = new Company();
		company.setCompCode("9999");
		company.setCompName("テスト");

		final Report report = new Report();
		report.setCompany(company);
		report.setYear(2000);

		final List<Word> issues = new ArrayList<>();

		final Word word = new Word();
		word.setSurface("テスト");
		final Vocab vocab = new Vocab();
		vocab.setProto("テスト");
		vocab.setPos("テスト");
		vocab.setPos1("テスト");
		vocab.setPos2("テスト");
		vocab.setPos3("テスト");
		vocab.setConj("テスト");
		vocab.setType("テスト");
		vocab.setFuri("テスト");
		vocab.setPronun("テスト");
		word.setVocab(vocab);

		issues.add(word);

		report.setIssues(issues);
		report.setRd(issues);

		target.mergeReport(report);
	}

}
