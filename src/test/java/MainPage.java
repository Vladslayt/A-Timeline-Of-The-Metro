import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Главная страница сайта
 */
@Slf4j
public class MainPage {
    private final Page page;
    private final Locator inputStationsFrom;
    private final Locator inputStationsTo;
    private final Locator listStationsFrom;
    private final Locator listStationsTo;
    private final Locator buttonClearFrom;
    private final Locator buttonClearTo;
    private final Locator time;
    private final Locator miss;


    public MainPage(Page page) {
        BasicConfigurator.configure();
        log.info("Start");
        this.page = page;
        page.navigate("https://metro-way.ru/moscow/");

        listStationsFrom = page.locator("#lstStationsFrom").locator("//div").nth(1);
        listStationsTo = page.locator("#lstStationsTo").locator("//div").nth(1);

        inputStationsFrom = page.locator("#lstStationsFrom").locator("//input");
        inputStationsTo = page.locator("#lstStationsTo").locator("//input");

        buttonClearFrom = page.locator("#lstStationsFrom").getByRole(AriaRole.BUTTON);
        buttonClearTo = page.locator("#lstStationsTo").getByRole(AriaRole.BUTTON);

        time = page.locator(".selected").locator(".text-duration");

        miss = page.locator(".copyright");
    }

    public MainPage fillStations() {
        String stanTo, stanFrom = "";
        inputStationsFrom.click();
        page.waitForCondition(() -> listStationsFrom.locator("//a").count() > 0);
        int count = listStationsFrom.locator("//a").count();
        log.info("start parsing");
        for (int i = 73; i < count; i++){
            log.info("clear first");
            buttonClearFrom.click();

            log.info("miss first");
            miss.click();

            log.info("click first");
            inputStationsFrom.click();

            page.waitForTimeout(500);

            log.info("select first");
            if(stanFrom.equals(listStationsFrom.locator("//a").nth(i).textContent())){
                log.info("SKIP DUPLICATE");
                continue;
            }
            stanFrom = listStationsFrom.locator("//a").nth(i).textContent();
            listStationsFrom.locator("//a").nth(i).click();

            for (int j = i + 1; j < count; j++){
                log.info("clear second");
                buttonClearTo.click();

                log.info("miss second");
                miss.click();

                log.info("click second");
                inputStationsTo.click();

                page.waitForTimeout(500);

                log.info("select second");
                stanTo = listStationsTo.locator("//a").nth(j).textContent();
                if(stanTo.equals(stanFrom)){
                    log.info("SKIP DUPLICATE");
                    continue;
                }
                listStationsTo.locator("//a").nth(j).click();

                log.info("add to file");
                appendToFile(stanFrom, stanTo, time.textContent());

                page.waitForTimeout(5000);
            }
        }

        return this;
    }

    public static void appendToFile(String staFrom, String stanTo, String time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("metro.txt", true))){
            String s = "\"" + staFrom + "\"  \"" + stanTo + "\"  \"" + time + "\"";
            writer.append(s);
            writer.append("\n");
        } catch(IOException e) {
            System.err.println(e);
        }
    }

    public MainPage takeAllStations() {
        String stanFrom = "";
        inputStationsFrom.click();
        page.waitForCondition(() -> listStationsFrom.locator("//a").count() > 0);
        int count = listStationsFrom.locator("//a").count();
        log.info("start parsing");


            log.info("clear first");
            buttonClearFrom.click();

            log.info("miss first");
            miss.click();

            log.info("click first");
            inputStationsFrom.click();

            page.waitForTimeout(500);

            log.info("select first");
            for (int i = 0; i < count; i++) {
                stanFrom = listStationsFrom.locator("//a").nth(i).textContent();

                appendToFile(stanFrom);
            }
            page.waitForTimeout(2000);

        return this;
    }

    public static void appendToFile(String staFrom) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("stations.txt", true))){
            writer.append(staFrom);
            writer.append("\n");
        } catch(IOException e) {
            System.err.println(e);
        }
    }
}
