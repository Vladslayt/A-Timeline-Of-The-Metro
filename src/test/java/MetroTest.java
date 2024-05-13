import org.junit.jupiter.api.Test;

class MetroTest extends BaseTest {
    @Test
    void takeAllTimeInMetro() {
//        new MainPage(page)
//                .takeAllStations();
                //.fillStations();

        new MetroBookPage(page)
                .fillStations();
    }
}
