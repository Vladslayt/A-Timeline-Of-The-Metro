import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.BasicConfigurator;

import java.io.*;

/**
 * Главная страница сайта
 */
@Slf4j
public class MetroBookPage {
    private final Page page;
    private final Locator inputStationsFrom;
    private final Locator inputStationsTo;
    private final Locator time;

    public MetroBookPage(Page page) {
        BasicConfigurator.configure();
        log.info("Start");
        this.page = page;
        page.navigate("https://metrobook.ru/");

        inputStationsFrom = page.locator("#fromStation");
        inputStationsTo = page.locator("#toStation");

        time = page.locator("#totalTime");
    }

    public MetroBookPage fillStations() {
        String stationFrom, stationTo;
        for(int i = 73; i < 244; i++){
            stationFrom = takeStrFromFile(i);

            inputStationsFrom.fill(stationFrom);
            page.keyboard().press("ArrowDown");
            page.keyboard().press("Enter");

            for (int j = i + 1; j < 244; j++){
                stationTo = takeStrFromFile(j);

                inputStationsTo.fill(stationTo);
                page.keyboard().press("ArrowDown");
                page.keyboard().press("Enter");

                page.waitForTimeout(300);
                String strTime = time.textContent();

                appendToFile(stationFrom, stationTo, strTime);

                page.waitForTimeout(1500);
            }
        }

        return this;
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

    public static void appendToFile(String staFrom, String stanTo, String time) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("allTime.txt", true))){
            String s = "\"" + staFrom + "\"  \"" + stanTo + "\"  \"" + time + "\"";
            writer.append(s);
            writer.append("\n");
        } catch(IOException e) {
            System.err.println(e);
        }
    }
}
