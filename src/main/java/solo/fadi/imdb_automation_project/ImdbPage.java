package solo.fadi.imdb_automation_project;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;

public class ImdbPage {
	
    private WebDriver driver = null;
    private WebDriverWait wait = null;
    
    public ImdbPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }
    
    
    //Workflow 1 Searching
    private By searchBoxBy 		   = By.id("suggestion-search");
    private By firstSearchResultBy = By.xpath("(//a[contains(@class, 'ipc-title-link-wrapper')])[1]");
    private By movieTitleHeaderBy  = By.xpath("//h1");
    private By allTitleResultsBy   = By.xpath("(//a[contains(@class, 'ipc-title-link-wrapper')])");
    private By noResultsMessageBy  = By.xpath("//section[@data-testid='error-section'] | //div[contains(@class, 'search-no-results')]");

    //Workflow 2 Filtering and Sorting
    private By firstChartMovieTitleBy      = By.xpath("(//h3[contains(@class, 'ipc-title__text')])[1]");
    private By topThreeChartMoviesBy       = By.xpath("(//h3[contains(@class, 'ipc-title__text')])[position() <= 3]");
    private By sortDropdownButtonBy        = By.id("sort-by-selector");
    private By sortByReleaseDateOptionBy   = By.xpath("//option[@value='RELEASE_DATE']");
    private By sortByAlphabeticalOptionBy  = By.xpath("//option[@value='ALPHA']");
    private By totalChartRowsBy 		   = By.xpath("//ul[contains(@class, 'ipc-metadata-list')]//li[contains(@class, 'ipc-metadata-list-summary-item')]");
    private By sortDirectionToggleButtonBy = By.id("swap-sort-order-button");
    private By fifthChartMovieLinkBy 	   = By.xpath("(//a[contains(@class, 'ipc-title-link-wrapper')])[5]");

    
    //Workflow 1 Searching
    public void navigateToHome() {
        driver.get("https://www.imdb.com/");
    }

    public void searchForMovie(String movieName) {
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxBy));
        searchInput.clear();
        searchInput.sendKeys(movieName);
        searchInput.sendKeys(Keys.ENTER); 
    }

    public void clickFirstSearchResult() {
        WebElement firstResult = wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultBy));
        firstResult.click();
    }

    public String getMovieTitleText() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(movieTitleHeaderBy));
        return title.getText();
    }

    
    public List<WebElement> getSearchResultElements() {
        // Force Selenium to wait until the browser engine reports the page load lifecycle is complete
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
        
        // Once the DOM is ready, collect and return all matching elements
        return driver.findElements(allTitleResultsBy);
    }

    public boolean isNoResultsMessageDisplayed() {
        try {
            WebElement errorBox = wait.until(ExpectedConditions.visibilityOfElementLocated(noResultsMessageBy));
            return errorBox.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    //Actions for Workflow 2 Filtering and Sorting
    public void navigateToTop250Chart() {
        driver.get("https://www.imdb.com/chart/top/");
    }

    public String getTopRatedMovieName() {
        WebElement topMovie = wait.until(ExpectedConditions.visibilityOfElementLocated(firstChartMovieTitleBy));
        return topMovie.getText();
    }

    public int getChartRowsCount() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstChartMovieTitleBy));
        return driver.findElements(totalChartRowsBy).size();
    }

    public void sortChartByReleaseDate() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdownButtonBy));
        dropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(sortByReleaseDateOptionBy)).click();
        waitForSortToLoad();
    }

    public void sortChartAlphabetically() {
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(sortDropdownButtonBy));
        dropdown.click();
        wait.until(ExpectedConditions.elementToBeClickable(sortByAlphabeticalOptionBy)).click();
        waitForSortToLoad();
    }

    public List<WebElement> getTopThreeMovieElements() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(topThreeChartMoviesBy));
        return driver.findElements(topThreeChartMoviesBy);
    }

    public void toggleSortDirection() {
        WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(sortDirectionToggleButtonBy));
        toggleBtn.click();
        waitForSortToLoad();
    }

    public String clickFifthMovieAndGetTitle() {
        WebElement fifthMovie = wait.until(ExpectedConditions.elementToBeClickable(fifthChartMovieLinkBy));
        String expectedTitle = fifthMovie.getText();
        
        if(expectedTitle.contains(". ")) {
            expectedTitle = expectedTitle.substring(expectedTitle.indexOf(". ") + 2);
        }
        
        fifthMovie.click();
        return expectedTitle;
    }

    private void waitForSortToLoad() {
        try { Thread.sleep(2500); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}