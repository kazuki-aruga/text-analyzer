package io.github.kazuki_aruga.text_analyzer.entity;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DataAccessTest {

	private DataAccess target;

	/**
	 * 
	 * @throws SQLException
	 */
	@Before
	public void setUp() throws SQLException {

		target = new DataAccess(DriverManager.getConnection("jdbc:mysql://localhost:3306/text-analyzer",
				"text-analyzer", "text-analyzer"));
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
		vocab.setPos1("テスト");
		vocab.setPos2("テスト");
		vocab.setPos3("テスト");

		Assert.assertFalse(target.existsVocab(vocab));
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

	@After
	public void tearDown() throws SQLException {

		target.getConnection().close();
		target = null;
	}

}
