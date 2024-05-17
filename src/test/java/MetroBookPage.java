import com.microsoft.playwright.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.io.*;

@Slf4j
public class MetroBookPage {
    private final Browser browser;

    public MetroBookPage(Browser browser) {
        BasicConfigurator.configure();
        log.info("Start");
        this.browser = browser;
    }

    public void fillStations(int start, int end) {
        try (BrowserContext context = browser.newContext()) {
            Page page = context.newPage();
            page.navigate("https://metrobook.ru/");
            String filename = Thread.currentThread().getName() + ".txt";
            for (int i = start; i < end; i++) {
                String stationFrom = takeStrFromFile(i);
                Locator inputStationsFrom = page.locator("#fromStation");
                inputStationsFrom.fill(stationFrom);
                page.keyboard().press("ArrowDown");
                page.keyboard().press("Enter");
                for (int j = i + 1; j < end; j++) {
                    String stationTo = takeStrFromFile(j);

                    Locator inputStationsTo = page.locator("#toStation");
                    Locator time = page.locator("#totalTime");

                    inputStationsTo.fill(stationTo);
                    page.keyboard().press("ArrowDown");
                    page.keyboard().press("Enter");

                    page.waitForTimeout(300);
                    String strTime = time.textContent();

                    // Запись в файл
                    appendToFile(filename, stationFrom, stationTo, strTime);

                    // Запись в консоль для диагностики
                    System.out.println(stationFrom + "\"  \"" + stationTo + "\"  \"" + strTime + "\"");

                    page.waitForTimeout(1500);
                }
            }
        }
    }

    public static void appendToFile(String filename, String staFrom, String stanTo, String time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))){
            String s = "\"" + staFrom + "\"  \"" + stanTo + "\"  \"" + time + "\"";
            writer.append(s);
            writer.append("\n");
        } catch(IOException e) {
            System.err.println(e);
        }
    }

    public static String takeStrFromFile(int numberLine) {
        try (BufferedReader reader = new BufferedReader(new FileReader("stations.txt"))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                count++;
                if (count == numberLine) {
                    return line;
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return null;
    }
}
