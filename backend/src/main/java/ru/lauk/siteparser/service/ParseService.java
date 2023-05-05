package ru.lauk.siteparser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EnableScheduling
@Service
public class ParseService {

    public String simpleParse(String parsedUrl) throws IOException {
        URL site = new URL(parsedUrl);
        URLConnection connection = site.openConnection();
        StringBuilder siteCode = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream()))) {
            while (reader.ready()) {
                siteCode.append(reader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return siteCode.toString();
    }

    public void leranParse() throws Exception {
        String url = "https://www.leran.pro/";
        System.setProperty("webdriver.chrome.driver", "chromedriver_win32/chromedriver.exe");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get(url);//TODO первая страница
        Document doc = Jsoup.parse(webDriver.getPageSource());
        Element body = doc.body();
        Elements catElements = body.getElementsByAttributeValueContaining("href", "cat");//TODO каталоги
        class Catalogue {
            private final String link;
            private final String tagText;

            Catalogue(String link, String tagText) {
                this.link = link;
                this.tagText = tagText;
            }
        }
        Map<String, Catalogue> elementMap = new HashMap<>();//TODO отсеивание лишних каталогов
        Pattern pattern = Pattern.compile("^/{1}(.[^/]+)/{1}(.[^/]+)/{1}$|^/{1}(.[^/]+)+[/?]{1}(.[^/]+)$");
        Matcher matcher = null;
        for (Element element : catElements) {
            String catalogueUrl = element.attr("href");
            matcher = pattern.matcher(catalogueUrl);
            if (!matcher.find()) {
                String[] splitUrl = catalogueUrl.split("/");
                if (splitUrl.length > 4) {
                    elementMap.put(splitUrl[3] + "/" + splitUrl[4], new Catalogue(catalogueUrl, element.toString()));
                } else {
                    elementMap.put(splitUrl[3], new Catalogue(catalogueUrl, element.toString()));
                }
            }
        }
        System.out.println(elementMap.size());
        for (Map.Entry<String, Catalogue> entry : elementMap.entrySet()) {
            System.out.println(entry.getKey() + " | " + entry.getValue());
        }

        webDriver.get(url + elementMap.keySet().stream().findFirst().get());
        doc = Jsoup.parse(webDriver.getPageSource());
        body = doc.body();
        System.out.println(body);

        webDriver.close();
        webDriver.quit();
        System.exit(0);
    }

}
