package ru.lauk.siteparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.lauk.siteparser.service.ParseService;

@SpringBootApplication
public class SiteParserApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(SiteParserApplication.class, args);
		System.setProperty(
				"webdriver.chrome.driver",
				"backend/chromedriver_win32/chromedriver.exe");
	}
}
