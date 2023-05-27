package ru.lauk.siteparser.service;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.touch.TouchActions;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Parsing process control class
 */
@Service
@Log4j2
public class ParseService {

    /**
     * Simple parse only one page without any handling
     * @param uri - page full address for parsing
     * @return text format page representation
     */
    public String simpleParse(String uri) {
        WebDriver webDriver = getWebDriverByUri(uri);
        String result = webDriver.getPageSource();
        webDriver.quit();
        return result;
    }

    /**
     *
     * @param uri
     * @return
     */
    private WebDriver getWebDriverByUri(String uri) {
        ChromeOptions options = new ChromeOptions();
        WebDriver webDriver;

        options.addArguments("--remote-allow-origins=*");
        webDriver = new ChromeDriver(options);

        log.info("Beginning parsing process for uri - {}", uri);
        webDriver.get(uri);
        log.info("Ended parsing process for uri - {}", uri);

        return webDriver;
    }

    /**
     * Здесь пока я разрабаотываю план как лучше сделатьнастраиваемую систему
     */
    public String parseExperiment(String uri) throws Exception {
        ChromeDriver driver;
        String pageSource;
        Element body;

        try {
            driver = (ChromeDriver) getWebDriverByUri(uri);
        } catch (Exception e) {
            String errorMassage = String.format("Getting resource main page " +
                    "\"%s\" ending with exception", uri);
            log.error(errorMassage, e);
            throw new Exception(errorMassage);
        }
        pageSource = driver.getPageSource();
        body = Jsoup.parse(pageSource);

        List<WebElement> elements = driver.findElements(By.tagName("a"));
        WebElement regionChangeButtonElement = elements.stream()
                .filter(e -> e.getAttribute("class").equals("link " +
                        "link_size_14 link_underline_disabled " +
                        "link_valign_middle ajax-html_on-click " +
                        "indicator_type_header-location-change " +
                        "header__location-change-link ajax-html indicator"))
                .findFirst().orElse(null);
        regionChangeButtonElement.click();

        List<Element> jsoupElements;
        int count = 0;

        while(true) {
            jsoupElements = Jsoup.parse(driver.getPageSource())
                    .body()
                    .getElementsByAttributeValue("class", "text  region-select__city");
            count++;
            if (count == 20) {
                throw new Exception("time out");
            } else if (jsoupElements.size() == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                    log.error(ie);
                }
            } else {
                break;
            }
        }

        Map<String, String> regionMap = new HashMap<>();

        for (Element spanTag : jsoupElements) {
            String regionId = spanTag.attributes().get("data-region_id");
            String regionName = spanTag.text();

            if (regionId.trim().isEmpty() ||
                    regionName.trim().isEmpty() ||
                    regionMap.containsKey(regionId)) {
                continue;
            }

            regionMap.put(regionId, regionName);
        }

        driver.quit();

        return regionMap.toString();
    }

    public void leranParse() throws Exception {
        String url = "https://www.leran.pro/";
        System.setProperty("webdriver.chrome.driver", "backend/chromedriver_win32/chromedriver.exe");
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
