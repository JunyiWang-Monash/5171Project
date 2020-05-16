package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.Musician;
import allaboutecm.model.Review;
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
    // Extra Credit
    @DisplayName("Should return the best-selling album when there is only one")
    @Test
    public void shouldReturnTheBestSellingAlbumsWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setSales(50);
        dao.createOrUpdate(album);
        List<Album> albums = ecmMiner.bestSellingAlbums(3);
        assertEquals(50,albums.iterator().next().getSales());
    }
    // Extra Credit
    @DisplayName("Should not return the best-selling album when k is 0")
    @Test
    public void shouldNotReturnTheBestSellingAlbumsWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setSales(50);
        dao.createOrUpdate(album);
        List<Album> albums1 = ecmMiner.bestSellingAlbums(0);
        assertEquals(0,albums1.size());
    }

    // Extra Credit
    @DisplayName("Should return the best-selling albums")
    @Test
    public void shouldReturnTheBestSellingAlbums() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1974, "ECM 1080", "The Köln Concert 12");
        Album album2 = new Album(1973, "ECM 1090", "The Köln Concert 13");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        album.setSales(500);
        album1.setSales(1000);
        album2.setSales(2000);
        List<Album> albums1 = ecmMiner.bestSellingAlbums(2);
        assertEquals(2,albums1.size());
        assertEquals(2000,albums1.get(0).getSales());
        assertEquals(1000,albums1.get(1).getSales());
    }

    // Extra Credit
    @DisplayName("Should return the highest rated albums")
    @Test
    public void shouldReturnTheHighestRatedAlbums() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(2020, "ECM 2679", "Swallow tales");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        Set<Review> reviews = Sets.newHashSet();
        Review review = new Review("Very nice",9);
        Review review1 = new Review("Very good",8);
        reviews.add(review);
        reviews.add(review1);
        album.setReviews(reviews);
        Set<Review> reviews1 = Sets.newHashSet();
        Review review2 = new Review("Very nice and good",9);
        Review review3 = new Review("Very good and inter",9);
        reviews1.add(review2);
        reviews1.add(review3);
        album1.setReviews(reviews1);
        List<Album> albums1 = ecmMiner.highestRatedAlbums(1);
        assertEquals(1,albums1.size());
        assertEquals("Swallow tales",albums1.get(0).getAlbumName());
    }

    // Extra Credit
    @DisplayName("Should not return the highest rated albums when k is 0")
    @Test
    public void shouldNotReturnTheHighestRatedAlbumsWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        Set<Review> reviews = Sets.newHashSet();
        Review review = new Review("Very nice", 9);
        Review review1 = new Review("Very good", 8);
        reviews.add(review);
        reviews.add(review1);
        album.setReviews(reviews);
        List<Album> albums1 = ecmMiner.highestRatedAlbums(0);
        assertEquals(0, albums1.size());
    }

    // Extra Credit
    @DisplayName("Should return the highest rated albums when only one album")
    @Test
    public void shouldNotReturnTheHighestRatedAlbumsWhenOnlyOneAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        Set<Review> reviews = Sets.newHashSet();
        Review review = new Review("Very nice",9);
        Review review1 = new Review("Very good",8);
        reviews.add(review);
        reviews.add(review1);
        album.setReviews(reviews);
        List<Album> albums1 = ecmMiner.highestRatedAlbums(3);
        assertEquals(1,albums1.size());
        assertEquals("The Köln Concert",albums1.get(0).getAlbumName());
    }

    //SimilarAlbum
    @DisplayName("Should return the most similar album")
    @Test
    public void shouldReturnTheMostSimilarAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        Musician musician= new Musician("Keith Jarrett");
        Musician musician1 = new Musician("John Scofield");
        Musician musician2 = new Musician("Steve Swallow");
        List<Musician> musicians = Lists.newArrayList();
        musicians.add(musician);
        musicians.add(musician1);
        musicians.add(musician2);
        album.setFeaturedMusicians(musicians);
        Album album1 = new Album(1974, "ECM 1080", "Swallow Tales");
        dao.createOrUpdate(album1);
        Musician musician3= new Musician("Keith Jarrett");
        Musician musician4 = new Musician("John Scofield");
        Musician musician5 = new Musician("Steve Thomas");
        List<Musician> musicians1 = Lists.newArrayList();
        musicians1.add(musician3);
        musicians1.add(musician4);
        musicians1.add(musician5);
        album1.setFeaturedMusicians(musicians1);
        Album album2 = new Album(1977, "ECM 1090", "Swat Kats");
        dao.createOrUpdate(album2);
        album2.setFeaturedMusicians(musicians);
        Album album3 = new Album(1979, "ECM 1091", "Noddy");
        dao.createOrUpdate(album3);
        album3.setFeaturedMusicians(musicians);
        List<Album> similarAlbums = ecmMiner.mostSimilarAlbums(2,album2);
        assertEquals(2,similarAlbums.size());
        assertEquals("The Köln Concert",similarAlbums.get(0).getAlbumName());
        assertEquals("Noddy",similarAlbums.get(1).getAlbumName());
    }

}