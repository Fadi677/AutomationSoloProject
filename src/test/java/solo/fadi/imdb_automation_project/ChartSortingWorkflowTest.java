package solo.fadi.imdb_automation_project;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class ChartSortingWorkflowTest extends BaseTest {
	
	//Change sorting from top ranked to released date
    @Test(priority = 1)
    public void testReleaseDateSorting() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        
        String originalTopMovie = imdb.getTopRatedMovieName();
        System.out.println(originalTopMovie);
        imdb.sortChartByReleaseDate();
        String sortedTopMovie = imdb.getTopRatedMovieName();
        System.out.println(sortedTopMovie);
        Assert.assertNotEquals(originalTopMovie, sortedTopMovie, "Sorting by Release Date failed to alter list state.");
    }
    
    //verify the top 250 list shows exactly 250 movies
    @Test(priority = 2)
    public void testGridRowCompleteness() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        int totalRows = imdb.getChartRowsCount();
        Assert.assertEquals(totalRows, 250, "Top Rated grid structure does not contain exactly 250 entries!");
    }

    //verify the list is sorted alphabetically
    @Test(priority = 3)
    public void testAlphabeticalSorting() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        imdb.sortChartAlphabetically();
        
        List<WebElement> topTen = imdb.getTopTenMovieElements(); 
        Assert.assertTrue(topTen.size() >= 10, "Failed to scrape top 10 records.");
        
        System.out.println("--- Top 10 Alphabetical Movies ---");
        for (WebElement movie : topTen) {
            System.out.println(movie.getText());
        }
    }

    //verify clicking the sort direction button flips the list order
    @Test(priority = 4)
    public void testSortDirectionToggle() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        String normalFirstMovie = imdb.getTopRatedMovieName();
        imdb.toggleSortDirection();
        String invertedLastMovie = imdb.getLastMovieName();
        System.out.println("Original First: " + normalFirstMovie);
        System.out.println("Inverted Last: " + invertedLastMovie);
        Assert.assertEquals(normalFirstMovie, invertedLastMovie, "The top-rated movie did not move to the last position after toggling sort direction.");
    }

    //verify clicking on a movie from the top 250 list navigates to it's page
    @Test(priority = 5)
    public void testDeepLinkNavigation() {
        ImdbPage imdb = new ImdbPage(driver, wait);
        imdb.navigateToTop250Chart();
        int randomIndex = new java.util.Random().nextInt(250); 
        String expectedTitle = imdb.clickMovieByIndexAndGetTitle(randomIndex);
        String actualLandingTitle = imdb.getMovieTitleText();
        System.out.println("Testing Movie Index: " + randomIndex);
        System.out.println("Expected (Chart): " + expectedTitle);
        System.out.println("Actual (Landing): " + actualLandingTitle);
        Assert.assertTrue(actualLandingTitle.contains(expectedTitle), 
            "The landing page title '" + actualLandingTitle + "' did not match the clicked chart movie title '" + expectedTitle + "'!");
    }
}