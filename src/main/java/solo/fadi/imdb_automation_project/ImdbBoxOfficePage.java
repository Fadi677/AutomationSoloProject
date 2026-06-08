package solo.fadi.imdb_automation_project;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class ImdbBoxOfficePage {

    private WebDriver driver;
    private WebDriverWait wait;
    private final By chartRows = By.xpath("//div[contains(@class, 'sc-fc35a1ef')]");
    private final By topMovieTitleLink = By.cssSelector("ul.ipc-metadata-list li:first-child h4.ipc-title__text");
    private final By topMovieWeekendGross = By.xpath("(//ul[contains(@class, 'ipc-metadata-list')]/li)[1]//span[contains(text(), '$')]");
    public ImdbBoxOfficePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }


    public void navigateToBoxOfficePage() {
        driver.get("https://www.imdb.com/chart/boxoffice/");
        wait.until(ExpectedConditions.presenceOfElementLocated(chartRows));
    }

    public int getBoxOfficeMoviesCount() {
        return driver.findElements(By.cssSelector("ul.ipc-metadata-list li")).size();
    }

    public String getTopMovieTitleName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(topMovieTitleLink));
        return driver.findElement(topMovieTitleLink).getText();
    }

    public String getTopMovieWeekendEarnings() {
        try {
            return driver.findElement(topMovieWeekendGross).getText();
        } catch (Exception e) {
            return "Revenue Undefined";
        }
    }
}