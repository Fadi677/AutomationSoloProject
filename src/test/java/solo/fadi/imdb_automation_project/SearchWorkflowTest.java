package solo.fadi.imdb_automation_project;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SearchWorkflowTest extends BaseTest {
	
	private ImdbPage imdb;
	
	@BeforeMethod
    public void prepareTestEnvironment() {
        imdb = new ImdbPage(driver, wait);
        imdb.navigateToHome();
    }
	
	private String[] getCsvRow(int rowIndex) {
	    String csvPath = "search_data.csv";
	    int currentIdx = 0;
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
	        String line;
	        while ((line = br.readLine()) != null) {
	            if (currentIdx == rowIndex) {
	                return line.split(",");
	            }
	            currentIdx++;
	        }
	    } catch (IOException e) {
	        Assert.fail("Failed to read test data file: " + e.getMessage());
	    }
	    throw new IllegalArgumentException("Row index " + rowIndex + " not found in CSV.");
	}
	
	// 1. All Positive
    @Test(priority = 1)
    public void testHappyPathSearch() {
        String[] data = getCsvRow(1);
        String query = data[1];
        String expected = data[2];

        imdb.searchForMovie(query);
        imdb.clickFirstSearchResult();
        
        String actualTitle = imdb.getMovieTitleText();
        Assert.assertTrue(actualTitle.contains(expected), "Title validation failed!");
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
    	List<WebElement> results = new java.util.ArrayList<>();
        results.clear();
        imdb.searchForMovie("xyzqwe123456789");
        results = imdb.getSearchResultElements();
        System.out.println(results);
        Assert.assertTrue(results.isEmpty(), "Expected 0 search results for a non-existent movie, but the list was not empty!");
    }
}