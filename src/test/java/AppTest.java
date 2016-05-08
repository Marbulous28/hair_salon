import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;
import org.sql2o.*;
import org.junit.*;

public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @ClassRule
  public static ServerRule server = new ServerRule();


  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteClientsQuery = "DELETE FROM clients *;";
      String deleteStylistsQuery = "DELETE FROM stylists *;";
      con.createQuery(deleteClientsQuery).executeUpdate();
      con.createQuery(deleteStylistsQuery).executeUpdate();
    }
  }

  @Test
  public void rootTest() {
    goTo("http://localhost:4567/");
    assertThat(pageSource()).contains("Hair Salon!");
    assertThat(pageSource()).contains("View Stylist List");
    assertThat(pageSource()).contains("Add a New Stylist");
  }

  @Test
  public void stylistIsCreatedTest() {
    goTo("http://localhost:4567/");
    click("a", withText("Add a New Stylist"));
    fill("#name").with("Debbie");
    submit(".btn");
    assertThat(pageSource()).contains("Your stylist has been added.");
  }

  @Test
  public void stylistIsDisplayedTest() {
    Stylist myStylist = new Stylist("Debbie");
    myStylist.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    assertThat(pageSource()).contains("Debbie");
  }

  @Test
  public void stylistShowPageDisplaysName() {
    goTo("http://localhost:4567/stylists/new");
    fill("#name").with("Debbie");
    submit(".btn");
    click("a", withText("View stylists"));
    click("a", withText("Debbie"));
    assertThat(pageSource()).contains("Debbie");
  }

  @Test
  public void stylistClientFormIsDisplayed() {
    goTo("http://localhost:4567/stylists/new");
    fill("#name").with("Debbie");
    submit(".btn");
    click("a", withText("View stylists"));
    click("a", withText("Debbie"));
    click("a", withText("Add a new client"));
    assertThat(pageSource()).contains("Add a Client for Debbie");
  }

  @Test
  public void clientAreAddedAndDisplayed() {
    goTo("http://localhost:4567/stylists/new");
    fill("#name").with("Debbie");
    submit(".btn");
    click("a", withText("View stylists"));
    click("a", withText("Debbie"));
    click("a", withText("Add a new client"));
    fill("#name").with("James");
    submit(".btn");
    click("a", withText("View stylists"));
    click("a", withText("Debbie"));
    assertThat(pageSource()).contains("James");
  }

  @Test
  public void clientShowPage() {
    Stylist myStylist = new Stylist("Debbie");
    myStylist.save();
    Client myClient = new Client("Dax", myStylist.getId());
    myClient.save();
    String stylistPath = String.format("http://localhost:4567/stylists/%d", myStylist.getId());
    goTo(stylistPath);
    click("a", withText("Dax"));
    assertThat(pageSource()).contains("Dax");
    assertThat(pageSource()).contains("Go Back");
  }

}
