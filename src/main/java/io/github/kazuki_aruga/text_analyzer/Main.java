/**
 * Copyright 2016 Kazuki Aruga <mailto:kazuki.aruga@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.github.kazuki_aruga.text_analyzer;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import io.github.kazuki_aruga.text_analyzer.entity.DataAccess;
import io.github.kazuki_aruga.text_analyzer.entity.Report;

/**
 * 有価証券報告書の「事業の状況」を形態素解析し、データベースに登録する。
 */
public class Main {

	private static final Log log = LogFactory.getLog(Main.class);

	private final MorphologicalAnalyzer ma;

	private final Connection conn;

	private Main(MorphologicalAnalyzer ma, Connection conn) {

		this.ma = ma;
		this.conn = conn;
	}

	private void execute(boolean jisa, File jisaDir)
			throws EncryptedDocumentException, InvalidFormatException, IOException, SQLException {

		final ReportAnalyzer analyzer = new ReportAnalyzer(ma);
		final DataAccess da = new DataAccess(conn);

		final File[] compDirList = jisaDir.listFiles(new FileFilter() {

			private Pattern pattern = Pattern.compile("[0-9]{4}");

			@Override
			public boolean accept(File pathname) {

				return pattern.matcher(pathname.getName()).matches() && pathname.isDirectory();
			}
		});

		for (File compDir : compDirList) {

			for (File reportFile : compDir.listFiles()) {

				da.beginTransaction();

				try {

					final Report report = analyzer.analyze(jisa, compDir.getName(), reportFile.getAbsolutePath());
					if (report == null) {

						da.rollback();
						continue;
					}

					da.mergeReport(report);

					da.commit();

				} catch (EncryptedDocumentException | InvalidFormatException | IOException | SQLException e) {

					da.rollback();
					throw e;
				}
			}
		}
	}

	/**
	 * 
	 * @param args
	 * @throws SQLException
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws EncryptedDocumentException
	 */
	public static void main(String[] args)
			throws SQLException, EncryptedDocumentException, InvalidFormatException, IOException {

		if (args.length < 1) {

			System.err.println("基底ディレクトリを指定してください。");
			return;
		}

		final File baseDir = new File(args[0]);

		if (!baseDir.exists() || !baseDir.isDirectory()) {

			System.err.println("基底ディレクトリの指定が間違っています。");
			return;
		}

		final File jisaDir = new File(baseDir, "JISA加入");
		if (!jisaDir.exists() || !jisaDir.isDirectory()) {

			System.err.println("JISA加入ディレクトリが存在しません。");
			return;
		}

		final File otherDir = new File(baseDir, "JISA非加入");
		if (!otherDir.exists() || !otherDir.isDirectory()) {

			System.err.println("JISA非加入ディレクトリが存在しません。");
			return;
		}

		final String dburl = 1 < args.length ? args[1]
				: "jdbc:mysql://localhost:3306/text-analyzer?autoReconnect=true&useSSL=false";
		final String dbuser = 2 < args.length ? args[2] : "text-analyzer";
		final String dbpass = 3 < args.length ? args[3] : "text-analyzer";

		try (MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
				Connection conn = DriverManager.getConnection(dburl, dbuser, dbpass)) {

			final Main main = new Main(ma, conn);
			main.execute(true, jisaDir);
			main.execute(false, otherDir);
		}
	}

}
