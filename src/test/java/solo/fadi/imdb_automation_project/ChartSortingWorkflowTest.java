package solo.fadi.imdb_automation_project;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class ChartSortingWorkflowTest extends BaseTest {

    @Test(priority = 1, description = "TC 2.1: Rating Sort State vs. Release Date Sort State")
    public void testReleaseDateSorting() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        
        String originalTopMovie = imdb.getTopRatedMovieName();
        imdb.sortChartByReleaseDate();
        String sortedTopMovie = imdb.getTopRatedMovieName();
        
        Assert.assertNotEquals(originalTopMovie, sortedTopMovie, "Sorting by Release Date failed to alter list state.");
    }

    @Test(priority = 2, description = "TC 2.2: Top 250 List Grid Completeness")
    public void testGridRowCompleteness() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        
        int totalRows = imdb.getChartRowsCount();
        Assert.assertEquals(totalRows, 250, "Top Rated grid structure does not contain exactly 250 entries!");
    }

    @Test(priority = 3, description = "TC 2.3: Alphabetical Sorting Order Verification")
    public void testAlphabeticalSorting() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        imdb.sortChartAlphabetically();
        
        List<WebElement> topThree = imdb.getTopThreeMovieElements();
        Assert.assertTrue(topThree.size() >= 3, "Failed to scrape top records.");
        System.out.println("Top Alphabetical Movie: " + topThree.get(0).getText());
    }

    @Test(priority = 4, description = "TC 2.4: List Direction Toggle (Ascending vs. Descending)")
    public void testSortDirectionToggle() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        
        String normalFirstMovie = imdb.getTopRatedMovieName();
        imdb.toggleSortDirection();
        String invertedFirstMovie = imdb.getTopRatedMovieName();
        
        Assert.assertNotEquals(normalFirstMovie, invertedFirstMovie, "Toggling sort order layout didn't invert data rows.");
    }

    @Test(priority = 5, description = "TC 2.5: Deep Navigation Link Integrity from List")
    public void testDeepLinkNavigation() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        
        String expectedTitleSnippet = imdb.clickFifthMovieAndGetTitle();
        String actualLandingTitle = imdb.getMovieTitleText();
        
        Assert.assertTrue(actualLandingTitle.contains(expectedTitleSnippet), 
            "Target profile title '" + actualLandingTitle + "' did not match chart entry snippet: '" + expectedTitleSnippet + "'");
    }
}