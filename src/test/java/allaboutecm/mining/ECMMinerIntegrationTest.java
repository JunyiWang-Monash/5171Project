package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.Musician;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * TODO: perform integration testing of both ECMMiner and the DAO classes together.
 */
class ECMMinerIntegrationTest {
    //private static final String TEST_DB = "target/test-data/test-db.neo4j";
    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;
    private static ECMMiner ecmMiner;
    @BeforeAll
    public static void setUp() {
        // See @https://neo4j.com/docs/ogm-manual/current/reference/ for more information.

        // To use an impermanent embedded data store which will be deleted on shutdown of the JVM,
        // you just omit the URI attribute.

        // Impermanent embedded store
        Configuration configuration = new Configuration.Builder().build();

        // Disk-based embedded store
        // Configuration configuration = new Configuration.Builder().uri(new File(TEST_DB).toURI().toString()).build();

        // HTTP data store, need to install the Neo4j desktop app and create & run a database first.
//        Configuration configuration = new Configuration.Builder().uri("http://neo4j:password@localhost:7474").build();

        sessionFactory = new SessionFactory(configuration, Musician.class.getPackage().getName());
        session = sessionFactory.openSession();

        dao = new Neo4jDAO(session);
        ecmMiner = new ECMMiner(dao);
    }


    @AfterEach
    public void tearDownEach() {
        session.purgeDatabase();
    }

    @AfterAll
    public static void tearDown() throws IOException {
        session.purgeDatabase();
        session.clear();
        sessionFactory.close();
        //File testDir = new File(TEST_DB);
        //if (testDir.exists()) {
            //FileUtils.deleteDirectory(testDir.toPath());
        //}
    }

    @Test
    public void shouldReturnTheBusiestYearWhenThereIsOnlyOneAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        List<Integer> busiestYears = ecmMiner.busiestYear(3);
        assertEquals(1,busiestYears.size());
    }

    @Test
    public void shouldNotReturnTheBusiestYearWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        List<Integer> busiestYears = ecmMiner.busiestYear(0);
        assertEquals(0,busiestYears.size());    
    }

    @Test
    public void shouldReturnTheBusiestYear() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1979, "ECM 1064/64", "The Köln Concert1");
        Album album2 = new Album(1975, "ECM 1064/63", "The Köln Concert12");
        Album album3 = new Album(1975, "ECM 1064/62", "The Köln Concert13");
        Album album4 = new Album(1979, "ECM 1064/61", "The Köln Concert14");
        Album album5 = new Album(1976, "ECM 1064/60", "The Köln Concert15");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        dao.createOrUpdate(album3);
        dao.createOrUpdate(album4);
        dao.createOrUpdate(album5);
        List<Integer> busiestYears = ecmMiner.busiestYear(2);
        assertEquals(2,busiestYears.size());
    }

}