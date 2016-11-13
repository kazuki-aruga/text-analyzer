/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kazuki
 */
public class DataAccess {

	private Connection conn;

	public DataAccess(Connection conn) {

		this.conn = conn;
	}

	public Connection getConnection() {

		return conn;
	}

	public boolean existsCompany(Company company) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement("select count(0) from comp where comp_code = ?")) {

			stmt.setString(1, company.getCompCode());

			try (ResultSet rs = stmt.executeQuery()) {

				if (!rs.next()) {

					return false;
				}

				if (rs.getInt(1) < 1) {

					return false;
				}
			}
		}

		return true;
	}

	public boolean existsVocab(Vocab vocab) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement(
				"select count(0) from vocab where proto = ? and pos = ? and pos1 = ? and pos2 = ? and pos3 = ?")) {

			stmt.setString(1, vocab.getProto());
			stmt.setString(2, vocab.getPos());
			stmt.setString(3, vocab.getPos1());
			stmt.setString(4, vocab.getPos2());
			stmt.setString(5, vocab.getPos3());

			try (ResultSet rs = stmt.executeQuery()) {

				if (!rs.next()) {

					return false;
				}

				if (rs.getInt(1) < 1) {

					return false;
				}
			}
		}

		return true;
	}

	public boolean existsReport(Report report) throws SQLException {

		try (PreparedStatement stmt = conn
				.prepareStatement("select count(0) from report where comp_code = ? and year = ?")) {

			stmt.setString(1, report.getCompany().getCompCode());
			stmt.setInt(2, report.getYear());

			try (ResultSet rs = stmt.executeQuery()) {

				if (!rs.next()) {

					return false;
				}

				if (rs.getInt(1) < 1) {

					return false;
				}
			}
		}

		return true;
	}

	public void saveReport(Report report) {

	}

}
