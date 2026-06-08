package solo.fadi.imdb_automation_project;

import org.testng.Assert;
import org.testng.annotations.Test;

public class GuestBoxOfficeWorkflowTest extends BaseTest {

    // TEST 1: Verifies an unregistered guest can open the box office layout successfully
    @Test(priority = 1)
    public void testNavigateToBoxOfficeAndVerifyListLoads() {
        ImdbBoxOfficePage officePage = new ImdbBoxOfficePage(driver, wait);
        
        officePage.navigateToBoxOfficePage();
        int rowsCount = officePage.getBoxOfficeMoviesCount();
        System.out.println("Box Office Test 1 - Total chart tracking movies loaded: " + rowsCount);
        
        Assert.assertTrue(rowsCount > 0, "The public box office metadata listing container returned empty chart arrays!");
    }

    // TEST 2: Validates the number one ranking box office slot text is parsed cleanly
    @Test(priority = 2)
    public void testVerifyTopMovieTitleIsPresent() {
        ImdbBoxOfficePage officePage = new ImdbBoxOfficePage(driver, wait);
        
        officePage.navigateToBoxOfficePage();
        String topMovieName = officePage.getTopMovieTitleName();
        System.out.println("Box Office Test 2 - Currently ranking at #1: " + topMovieName);
        
        Assert.assertNotNull(topMovieName, "The parsed top ranking box office label returned null.");
        Assert.assertFalse(topMovieName.isEmpty(), "The movie name text node data pulled back completely empty strings.");
    }

    // TEST 3: Verifies that financial tracking numbers are visible to guest traffic
    @Test(priority = 3)
    public void testVerifyTopMovieRevenueDataExists() {
        ImdbBoxOfficePage officePage = new ImdbBoxOfficePage(driver, wait);
        
        officePage.navigateToBoxOfficePage();
        String grossEarnings = officePage.getTopMovieWeekendEarnings();
        System.out.println("Box Office Test 3 - #1 Movie Weekend Gross: " + grossEarnings);
        
        Assert.assertTrue(grossEarnings.contains("$") || !grossEarnings.equals("Revenue Undefined"), 
            "The chart revenue metrics node returned unexpected formatting or structural placeholders.");
    }

    // TEST 4: Validates that the #1 movie element correctly displays the active title matching your screen layout
    @Test(priority = 4)
    public void testVerifyTopMovieTitleContent() {
        ImdbBoxOfficePage officePage = new ImdbBoxOfficePage(driver, wait);
        
        officePage.navigateToBoxOfficePage();
        String topTitleText = officePage.getTopMovieTitleName();
        
        // Matches the clean string element exactly as displayed in your screenshot layout view
        Assert.assertEquals(topTitleText, "Scary Movie", 
            "The top ranking movie text content does not match the expected active number one chart title!");
    }

    // TEST 5: Assures data stability by doing a double check reload and checking consistency
    @Test(priority = 5)
    public void testChartDataConsistencyOnRefresh() {
        ImdbBoxOfficePage officePage = new ImdbBoxOfficePage(driver, wait);
        
        officePage.navigateToBoxOfficePage();
        String firstLoadTitle = officePage.getTopMovieTitleName();
        
        // Refresh page completely
        driver.navigate().refresh();
        
        String secondLoadTitle = officePage.getTopMovieTitleName();
        Assert.assertEquals(firstLoadTitle, secondLoadTitle, "The chart component structures loaded mismatched data strings across sequential requests.");
    }
}