package solo.fadi.imdb_automation_project;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.List;

public class SearchWorkflowTest extends BaseTest {
	
	private ImdbPage imdb;
	
	@BeforeMethod
    public void prepareTestEnvironment() {
        imdb = new ImdbPage(driver, wait);
        imdb.navigateToHome();
    }
	
	
	//All Positive
    @Test(priority = 1)
    public void testHappyPathSearch() {
        imdb.searchForMovie("Inception");
        imdb.clickFirstSearchResult();
        
        String actualTitle = imdb.getMovieTitleText();
        Assert.assertTrue(actualTitle.contains("Inception"), "Title validation failed!");
    }

    //Case Insensitivity CHeck
    @Test(priority = 2)
    public void testCaseInsensitiveSearch() {
        imdb.searchForMovie("iNcEpTiOn");
        imdb.clickFirstSearchResult();
        
        String actualTitle = imdb.getMovieTitleText();
        Assert.assertTrue(actualTitle.equalsIgnoreCase("Inception"), "Case-insensitive title matching failed!");
    }

    //Searching using part of the title
    @Test(priority = 3)
    public void testPartialKeywordSearch() {
        imdb.searchForMovie("Incept");
        
        List<WebElement> results = imdb.getSearchResultElements();
        Assert.assertTrue(results.size() > 0, "No results returned for partial keyword query!");
        
        String firstResultText = results.get(0).getText();
        Assert.assertTrue(firstResultText.contains("Incept"), "Top partial match result contextually incorrect!");
    }
    
    //Searching containing numbers or special characters
    @Test(priority = 4)
    public void testSpecialCharacterSearch() {
        imdb.searchForMovie("Se7en");
        imdb.clickFirstSearchResult();
        
        String actualTitle = imdb.getMovieTitleText();
        Assert.assertTrue(actualTitle.contains("Seven") || actualTitle.contains("Se7en"), "Alphanumeric search resolution failed!");
    }

    // Searching for non existent movie
    @Test(priority = 5)
    public void testInvalidSearchQuery() {
        imdb.searchForMovie("xyzqwe123456789");
        List<WebElement> results = imdb.getSearchResultElements();
        Assert.assertTrue(results.isEmpty(), "Expected 0 search results for a non-existent movie, but the list was not empty!");
        System.out.println(results);
    }
}