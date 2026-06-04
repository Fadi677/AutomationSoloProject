package solo.fadi.imdb_automation_project;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ImdbPage {
	
    private WebDriver driver = null;
    private WebDriverWait wait = null;
    
    public ImdbPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    
    //Workflow 1 Searching
    private By searchBoxBy 		   = By.id("suggestion-search");
    private By firstSearchResultBy = By.xpath("(//a[contains(@class, 'ipc-title-link-wrapper')])[1]");
    private By movieTitleHeaderBy  = By.xpath("//span[@data-testid='hero__primary-text']");
    private By allTitleResultsBy   = By.xpath("(//a[contains(@class, 'ipc-title-link-wrapper')])");
    private By noResultsMessageBy  = By.xpath("//div[contains(text(), 'No results found for')]");

    //Workflow 2 Filtering and Sorting
    private By firstChartMovieTitleBy      = By.xpath("(//h3[contains(@class, 'ipc-title__text')])[1]");
    private By topTenChartMoviesBy         = By.xpath("(//h3[contains(@class, 'ipc-title__text')])[position() <= 10]");
    private By sortDropdownButtonBy 	   = By.xpath("//span[@class='ipc-simple-select ipc-simple-select--base ipc-simple-select--on-accent2 sc-652f853-2 HqCIN']");
    private By sortByReleaseDateOptionBy   = By.xpath("//option[@value='RELEASE_DATE']");
    private By sortByAlphabeticalOptionBy  = By.xpath("//option[@value='TITLE_REGIONAL']");
    private By totalChartRowsBy 		   = By.xpath("//ul[contains(@class, 'ipc-metadata-list')]//li[contains(@class, 'ipc-metadata-list-summary-item')]");
    private By sortDirectionToggleButtonBy = By.id("swap-sort-order-button");
    private By movieTitleBy 			   = By.xpath("//div[contains(@class, 'cli-title')]//h3[@class='ipc-title__text']");
    private By movieLinksLocator 	       = By.cssSelector("div.cli-title h3.ipc-title__text");
    
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
        WebElement firstResult = wait.until(ExpectedConditions.presenceOfElementLocated(firstSearchResultBy));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(firstResult)).click();
        } catch (org.openqa.selenium.StaleElementReferenceException e) {
            wait.until(ExpectedConditions.elementToBeClickable(firstSearchResultBy)).click();
        }
    }

    public String getMovieTitleText() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(movieTitleHeaderBy));
        return title.getText();
    }
    
    
    public List<WebElement> getSearchResultElements() {
        // 1. Wait for the page structure to finish processing basic network rendering
        wait.until(webDriver -> ((org.openqa.selenium.JavascriptExecutor) webDriver)
            .executeScript("return document.readyState").equals("complete"));
        // 2. Wait until either actual titles show up OR the "No results found" box renders
        wait.until(ExpectedConditions.or(
            ExpectedConditions.presenceOfElementLocated(allTitleResultsBy),
            ExpectedConditions.presenceOfElementLocated(noResultsMessageBy)
        ));
        // 3. Check if the "No results" message is currently visible on the screen
        List<WebElement> noResultsElements = driver.findElements(noResultsMessageBy);
        if (!noResultsElements.isEmpty() && noResultsElements.get(0).isDisplayed()) {
            System.out.println("IMDb UI confirmed: No results found. Returning an empty list safely.");
            return new java.util.ArrayList<>(); // Hand back an empty list directly to your test assertion
        }
        // 4. If the "No results" block isn't there, collect and return the matching movie rows
        return driver.findElements(allTitleResultsBy);
    }


    //Workflow 2 Filtering and Sorting
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
    

    public List<WebElement> getTopTenMovieElements() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(topTenChartMoviesBy));
        return driver.findElements(topTenChartMoviesBy);
    }

    public void toggleSortDirection() {
        WebElement toggleBtn = wait.until(ExpectedConditions.elementToBeClickable(sortDirectionToggleButtonBy));
        toggleBtn.click();
        waitForSortToLoad();
    }
    
    public String getLastMovieName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(movieTitleBy));
        List<WebElement> allMovies = driver.findElements(movieTitleBy);
        WebElement lastMovie = allMovies.get(allMovies.size() - 1);
        return lastMovie.getText();
    }

    public String clickMovieByIndexAndGetTitle(int index) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        
        // 1. Force-scroll down to kick off lazy-loading scripts
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        
        // 2. Explicitly wait for all 250 rows to fully load into the DOM
        wait.until(ExpectedConditions.numberOfElementsToBe(movieLinksLocator, 250));
        
        // 3. Extract the complete list safely
        List<WebElement> movieLinks = driver.findElements(movieLinksLocator);        
        WebElement targetMovie = movieLinks.get(index);        
        
        // 4. Center the chosen element in the viewport view
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", targetMovie);        
        
        // 5. Harvest and strip numbering (e.g., "142. Interstellar" -> "Interstellar")
        String expectedTitle = targetMovie.getText().replaceFirst("^\\d+\\.\\s+", "").trim();        
        
        // 6. Execute JavaScript click to bypass overlay intercept issues
        js.executeScript("arguments[0].click();", targetMovie);
        
        return expectedTitle;
    }

    private void waitForSortToLoad() {
        try { Thread.sleep(2500); } catch (InterruptedException e) { e.printStackTrace(); }
    }
}