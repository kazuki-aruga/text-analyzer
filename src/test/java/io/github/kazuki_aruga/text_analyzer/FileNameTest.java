package io.github.kazuki_aruga.text_analyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class FileNameTest {

	@Test
	public void test() throws IOException {

		final Pattern pattern = Pattern.compile(
				"^([0-9]{4})_([^_]+)_有価証券報告書第([0-9]{1,2})期（平成([0-9]{1,2})年([0-9]{1,2})月([0-9]{1,2})日-平成([0-9]{1,2})年([0-9]{1,2})月([0-9]{1,2})日）[.]txt$");

		final File dirfile = new File("C:\\Temp\\dir.txt");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dirfile), "MS932"))) {

			for (String line = null; (line = reader.readLine()) != null;) {

				final Matcher matcher = pattern.matcher(line);

				if (matcher.matches()) {

					System.out.println("OK: " + line);

				} else {

					System.out.println("NG: " + line);
				}
			}
		}
	}

}
