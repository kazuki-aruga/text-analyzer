package io.github.kazuki_aruga.text_analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class FileNameTest {

	@Test
	public void test() throws IOException, ParseException {

		final Pattern pattern = Pattern.compile(
				"^([0-9]{4})_[^_]+_有価証券報告書第[0-9]{1,2}期（(平成[0-9]{1,2}年[0-9]{1,2}月[0-9]{1,2}日)-(平成[0-9]{1,2}年[0-9]{1,2}月[0-9]{1,2}日)）[.]txt$");

		final DateFormat japaseseFormat = new SimpleDateFormat("GGGGy年M月d日", new Locale("ja", "JP", "JP"));
		final DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		// 和暦にフォーマットした現在日時を西暦にパースする
		// 日付の妥当性をチェックするためsetLenient()にfalseを渡す
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(ClassLoader.getSystemResourceAsStream("filename-list.txt"), "MS932"))) {

			for (String line = null; (line = reader.readLine()) != null;) {

				final Matcher matcher = pattern.matcher(line);

				if (matcher.matches()) {

					final String compCode = matcher.group(1);
					final String empDateFrom = matcher.group(2);
					final String empDateTo = matcher.group(3);

					System.out.print("OK: " + line);
					System.out.print("Date From: " + df.format(japaseseFormat.parse(empDateFrom)));
					System.out.println("Date To  : " + df.format(japaseseFormat.parse(empDateTo)));

				} else {

					System.out.println("NG: " + line);
				}
			}
		}
	}

}
