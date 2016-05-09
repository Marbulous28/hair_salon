import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class StylistTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
    String deleteClinentsQuery = "DELETE FROM clients *;";
    String deleteStylistsQuery = "DELETE FROM stylists *;";
    con.createQuery(deleteClinentsQuery).executeUpdate();
    con.createQuery(deleteStylistsQuery).executeUpdate();
    }
  }

  @Test
  public void Stylist_instantiatesCorrectly_true() {
    Stylist myStylist = new Stylist("Megan");
    assertEquals(true, myStylist instanceof Stylist);
  }

  @Test
  public void getName_stylistInstantiatesWithName_String() {
    Stylist myStylist = new Stylist("Megan");
    assertEquals("Megan", myStylist.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Stylist.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Stylist firstStylist = new Stylist("Megan");
    Stylist secondStylist = new Stylist("Megan");
    assertTrue(firstStylist.equals(secondStylist));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Stylist myStylist = new Stylist("Megan");
    myStylist.save();
    assertTrue(Stylist.all().get(0).equals(myStylist));
  }

  @Test
  public void save_assignsIdToObject() {
    Stylist myStylist = new Stylist("Megan");
    myStylist.save();
    Stylist savedStylist = Stylist.all().get(0);
    assertEquals(myStylist.getId(), savedStylist.getId());
  }

  @Test
  public void find_findStylistInDatabase_true() {
    Stylist myStylist = new Stylist("Megan");
    myStylist.save();
    Stylist savedStylist = Stylist.find(myStylist.getId());
    assertTrue(myStylist.equals(savedStylist));
  }

  @Test
  public void getClients_retrievesALlClientsFromDatabase_clientsList() {
    Stylist myStylist = new Stylist("Megan");
    myStylist.save();
    Client firstClient = new Client("James", myStylist.getId());
    firstClient.save();
    Client secondClient = new Client("Jamie", myStylist.getId());
    secondClient.save();
    Client[] clients = new Client[] { firstClient, secondClient };
    assertTrue(myStylist.getClients().containsAll(Arrays.asList(clients)));
  }

  @Test
  public void update_updatesStylistName_true() {
    Stylist myStylist = new Stylist("Debbie");
    myStylist.save();
    myStylist.update("Simon");
    assertEquals("Simon", Stylist.find(myStylist.getId()).getName());
  }

  @Test
  public void delete_deletesStylist_true() {
    Stylist myStylist = new Stylist("Debbie");
    myStylist.save();
    int myStylistId = myStylist.getId();
    myStylist.delete();
    assertEquals(null, Stylist.find(myStylistId));
  }



}
