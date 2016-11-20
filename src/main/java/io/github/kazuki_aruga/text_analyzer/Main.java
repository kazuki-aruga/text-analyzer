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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.kazuki_aruga.text_analyzer.entity.DataAccess;

/**
 * 有価証券報告書の「事業の状況」を形態素解析し、データベースに登録する。
 */
public class Main {

	/**
	 * 
	 * @param args
	 * @throws SQLException
	 */
	public static void main(String[] args) throws SQLException {

		try (MorphologicalAnalyzer ma = new MorphologicalAnalyzer();
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/text-analyzer",
						"text-analyzer", "text-analyzer")) {

			final ReportAnalyzer analyzer = new ReportAnalyzer(ma);
			final DataAccess da = new DataAccess(conn);

			final File baseDir = new File(args[0]);
		}
	}

}
