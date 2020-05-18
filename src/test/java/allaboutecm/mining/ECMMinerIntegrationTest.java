package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.*;
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

    @DisplayName("Should not return any musician if k is zero or negative")
    @Test
    public void shouldNotReturnAnyMusicianWhenKIsZeroOrNegative() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        dao.createOrUpdate(musician);
        Album album1 = new Album(1975, "ECM 1080", "Dark Knight");
        Musician musician1 = new Musician("Robert Stall");
        dao.createOrUpdate(musician1);
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        albums.add(album1);
        musician.setAlbums(albums);
        musician1.setAlbums(Sets.newHashSet(album));
        List<Musician> musicians1 = ecmMiner.mostProlificMusicians(0, 1974, 1976);
        assertEquals(0, musicians1.size());
    }

    @DisplayName("Should return the musician when there is only one")
    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        dao.createOrUpdate(musician);

        List<Musician> musicians = ecmMiner.mostProlificMusicians(5, -1, -1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @DisplayName("Should return the most prolific musician")
    @Test
    public void shouldReturnTheMostProlificMusician() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        Album album1 = new Album(1975, "ECM 1080", "Dark Knight");
        Musician musician1 = new Musician("Robert Stall");
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        albums.add(album1);
        musician.setAlbums(albums);
        musician1.setAlbums(Sets.newHashSet(album));
        dao.createOrUpdate(musician);
        dao.createOrUpdate(musician1);
        List<Musician> musicians1 = ecmMiner.mostProlificMusicians(1, 1974, 1976);
        assertEquals(1, musicians1.size());
        assertTrue(musicians1.contains(musician));
    }

    @DisplayName("Should return the cooperated musician when there is only one cooperated musician")
    @Test
    public void shouldReturnTheMusicianHimOrHerselfAsTheMostSocialMusicianWhenThereIsOnlyOneMusician() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");

        musician.setAlbums(Sets.newHashSet(album));
        List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician);
        album.setFeaturedMusicians(albumMusicians);
        dao.createOrUpdate(musician);
        dao.createOrUpdate(album);
        List<Musician> musicians = ecmMiner.mostSocialMusicians(3);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @DisplayName("Should not return the most social musician when k is zero or negative")
    @Test
    public void shouldNotReturnReturnTheMostSocialMusicianWhenKIsZeroOrNegative() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);
        List<Musician> musicians = ecmMiner.mostSocialMusicians(0);
        assertEquals(0, musicians.size());
    }

    @DisplayName("Should return the most social musician")
    @Test
    public void shouldReturnTheMostSocialMusician() {
        Album album1 = new Album(2018, "ECM 2590", "After the Fall");
        Album album2 = new Album(1973, "ECM 1021", "Ruta and Daitya");
        dao.createOrUpdate(album1);
        dao.createOrUpdate(album2);
        Musician musician = new Musician("Keith Jarrett");
        Musician musician1 = new Musician("Gary Peacock");
        Musician musician2 = new Musician("Jack Dejohnette");
        List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician1);
        albumMusicians.add(musician);
        albumMusicians.add(musician2);
        album1.setFeaturedMusicians(albumMusicians);
        List<Musician> albumMusicians1 = albumMusicians.subList(1, albumMusicians.size());
        album2.setFeaturedMusicians(albumMusicians1);

        Set<Album> albums = Sets.newHashSet();
        albums.add(album1);
        albums.add(album2);
        musician.setAlbums(albums);
        musician1.setAlbums(Sets.newHashSet(album1));
        musician2.setAlbums(albums);
        dao.createOrUpdate(musician);
        dao.createOrUpdate(musician1);
        dao.createOrUpdate(musician2);

        List<Musician> musicians = ecmMiner.mostSocialMusicians(3);

        assertEquals(3, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @DisplayName("Should return the only talented musician when there is only one")
    @Test
    public void shouldReturnTheOnlyTalentedMusicianWhenThereIsOnlyOne(){
        Musician musician= new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,Sets.newHashSet(musicalInstrument));
        dao.createOrUpdate(musicianInstrument);
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(2);
        assertEquals(1,musicians.size());
        assertEquals("Keith Jarrett",musicians.iterator().next().getName());
    }

    @DisplayName("Should not return any talented musician when k is zero or negative")
    @Test
    public void shouldNotReturnAnyTalentedMusicianWhenKIsZeroOrNegative(){
        Musician musician= new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,Sets.newHashSet(musicalInstrument));
        dao.createOrUpdate(musicianInstrument);
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(0);
        assertEquals(0,musicians.size());
    }

    @DisplayName("Should return the most talented musician")
    @Test
    public void shouldReturnTheMostTalentedMusician(){
        Musician musician= new Musician("Keith Jarrett");
        Musician musician1 = new Musician("John Snowfield");
        Musician musician2 = new Musician("Steve Swallow");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Guitar");
        MusicalInstrument musicalInstrument2 = new MusicalInstrument("Piano");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        musicalInstruments.add(musicalInstrument);
        musicalInstruments.add(musicalInstrument1);
        musicalInstruments.add(musicalInstrument2);
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,musicalInstruments);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,musicalInstruments);
        Set<MusicalInstrument> musicalInstruments1 = Sets.newHashSet();
        musicalInstruments1.add(musicalInstrument);
        musicalInstruments1.add(musicalInstrument1);
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician2,musicalInstruments1);
        Set<MusicianInstrument> musicianInstruments = Sets.newHashSet();
        dao.createOrUpdate(musicianInstrument);
        dao.createOrUpdate(musicianInstrument1);
        dao.createOrUpdate(musicianInstrument2);
        List<Musician> musicians = ecmMiner.mostTalentedMusicians(2);
        assertEquals(2,musicians.size());
        assertTrue(musicians.contains(musician));
        assertTrue(musicians.contains(musician1));
    }

    @DisplayName("Should not return any similar album if the value of k is zero or negative")
    @Test
    public void shouldNotReturnAnySimilarAlbumIfTheValueOfKIsZeroOrNegative() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1976, "ECM 1080", "The Köln Concert12");
        Musician musician= new Musician("Keith Jarrett");
        album.setFeaturedMusicians(Lists.newArrayList(musician));
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        dao.createOrUpdate(album);
        List<Album> similarAlbums = ecmMiner.mostSimilarAlbums(0,album1);
        assertEquals(0,similarAlbums.size());
    }

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
        Musician musician4 = new Musician("John Snowfield");
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
        assertTrue(similarAlbums.contains(album));
        assertTrue(similarAlbums.contains(album3));
    }

    @DisplayName("Should return the busiest year when there is only one album")
    @Test
    public void shouldReturnTheBusiestYearWhenThereIsOnlyOneAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        List<Integer> busiestYears = ecmMiner.busiestYear(3);
        assertEquals(1,busiestYears.size());
    }

    @DisplayName("Should not return the busiest year when k is zero or negative")
    @Test
    public void shouldNotReturnTheBusiestYearWhenKIsZeroOrNegative() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        List<Integer> busiestYears = ecmMiner.busiestYear(0);
        assertEquals(0,busiestYears.size());    
    }

    @DisplayName("Should return the busiest year")
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
    @DisplayName("Should not return the best-selling album when k is 0 or Negative")
    @Test
    public void shouldNotReturnTheBestSellingAlbumsWhenKIsZeroOrNegative() {
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
    public void shouldNotReturnTheHighestRatedAlbumsWhenKIsZeroOrNegative() {
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

}