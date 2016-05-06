import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ClientTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/hair_salon_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM clients *;";
      con.createQuery(sql).executeUpdate();
    }
  }

  @Test
  public void Client_instantiatesCorrectly_true() {
    Client myClient = new Client("James");
    assertEquals(true, myClient instanceof Client);
  }

  @Test
  public void getName_clientInstantiatesWithName_String() {
    Client myClient = new Client("Tiffany");
    assertEquals("Tiffany", myClient.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Client.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Client firstClient = new Client("James");
    Client secondClient = new Client("James");
    assertTrue(firstClient.equals(secondClient));
  }

}
