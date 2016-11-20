/**
 * 
 */
package io.github.kazuki_aruga.text_analyzer.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * データアクセスクラス。
 * 
 * @author kazuki
 */
public class DataAccess {

	/**
	 * DB接続。
	 */
	private final Connection conn;

	/**
	 * DB接続をもとに、データアクセスクラスのインスタンスを生成する。
	 * 
	 * @param conn
	 *            DB接続。
	 */
	public DataAccess(Connection conn) {

		this.conn = conn;
	}

	/**
	 * トランザクションを開始する。
	 * 
	 * @throws SQLException
	 */
	public void beginTransaction() throws SQLException {

		conn.setAutoCommit(false);
	}

	/**
	 * トランザクションをコミットする。
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {

		conn.commit();
	}

	/**
	 * トランザクションをロールバックする。
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {

		conn.rollback();
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

	public Company findCompany(String compCode) throws SQLException {

		try (PreparedStatement stmt = conn
				.prepareStatement("select comp_code, comp_name, jisa from comp where comp_code = ?")) {

			stmt.setString(1, compCode);

			try (ResultSet rs = stmt.executeQuery()) {

				if (!rs.next()) {

					return null;
				}

				final Company comp = new Company();

				comp.setCompCode(rs.getString(1));
				comp.setCompName(rs.getString(2));
				comp.setJisa(rs.getBoolean(3));

				return comp;
			}
		}
	}

	public void saveCompany(Company company) throws SQLException {

		try (PreparedStatement stmt = conn
				.prepareStatement("insert into comp (comp_code, comp_name, jisa) values (?, ?, ?)")) {

			stmt.setString(1, company.getCompCode());
			stmt.setString(2, company.getCompName());
			stmt.setBoolean(3, company.isJisa());

			stmt.executeUpdate();
		}
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

	public void mergeVocab(Vocab vocab) throws SQLException {

		findVocab(vocab);

		if (vocab.getVocabId() == null) {

			saveVocab(vocab);
		}
	}

	private void findVocab(Vocab vocab) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement(
				"select vocab_id, proto, pos, pos1, pos2, pos3, conj, type, furi, pronun from vocab where proto = ? and pos = ? and pos1 = ? and pos2 = ? and pos3 = ? and conj = ?")) {

			stmt.setString(1, vocab.getProto());
			stmt.setString(2, vocab.getPos());
			stmt.setString(3, vocab.getPos1());
			stmt.setString(4, vocab.getPos2());
			stmt.setString(5, vocab.getPos3());
			stmt.setString(6, vocab.getConj());

			try (ResultSet rs = stmt.executeQuery()) {

				if (rs.next()) {

					vocab.setVocabId(rs.getInt("vocab_id"));
				}
			}
		}
	}

	public void saveVocab(Vocab vocab) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement(
				"insert into vocab (proto, pos, pos1, pos2, pos3, conj, type, furi, pronun) values (?, ?, ?, ?, ?, ?, ?, ?, ?)",
				Statement.RETURN_GENERATED_KEYS)) {

			stmt.setString(1, vocab.getProto());
			stmt.setString(2, vocab.getPos());
			stmt.setString(3, vocab.getPos1());
			stmt.setString(4, vocab.getPos2());
			stmt.setString(5, vocab.getPos3());
			stmt.setString(6, vocab.getConj());
			stmt.setString(7, vocab.getType());
			stmt.setString(8, vocab.getFuri());
			stmt.setString(9, vocab.getPronun());

			stmt.executeUpdate();

			try (ResultSet rs = stmt.getGeneratedKeys()) {

				while (rs.next()) {

					vocab.setVocabId(rs.getInt(1));
				}
			}
		}
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

	public void mergeReport(Report report) throws SQLException {

		if (!existsCompany(report.getCompany())) {

			saveCompany(report.getCompany());
			saveReport(report);

		} else {

			if (!existsReport(report)) {

				saveReport(report);
			}
		}

		mergeReportWord(report);
	}

	public void saveReport(Report report) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement("insert into report (comp_code, year) values (?, ?)")) {

			stmt.setString(1, report.getCompany().getCompCode());
			stmt.setInt(2, report.getYear());

			stmt.executeUpdate();
		}
	}

	public void mergeReportWord(Report report) throws SQLException {

		int seq = 0;
		for (Word word : report.getIssues()) {

			mergeVocab(word.getVocab());
			saveReportWord(report, 1, ++seq, word);
		}

		seq = 0;
		for (Word word : report.getRd()) {

			mergeVocab(word.getVocab());
			saveReportWord(report, 2, ++seq, word);
		}
	}

	public void saveReportWord(Report report, int section, int seq, Word word) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement(
				"insert into report_word (comp_code, year, section, seq, surface, vocab_id) values (?, ?, ?, ?, ?, ?)")) {

			stmt.setString(1, report.getCompany().getCompCode());
			stmt.setInt(2, report.getYear());
			stmt.setInt(3, section);
			stmt.setInt(4, seq);
			stmt.setString(5, word.getSurface());
			stmt.setInt(6, word.getVocab().getVocabId());

			stmt.executeUpdate();
		}
	}

}
