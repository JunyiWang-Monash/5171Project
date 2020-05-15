package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.mining.ECMMiner;
import allaboutecm.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.*;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.neo4j.ogm.support.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: add test cases to adequately test the Neo4jDAO class.
 */
class Neo4jDAOUnitTest {
    private static final String TEST_DB = "target/test-data/test-db.neo4j";

    private static DAO dao;
    private static Session session;
    private static SessionFactory sessionFactory;

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
        File testDir = new File(TEST_DB);
        if (testDir.exists()) {
//            FileUtils.deleteDirectory(testDir.toPath());
        }
    }

    @DisplayName("Dao is not empty")
    @Test
    public void daoIsNotEmpty() {
        assertNotNull(dao);
    }

    @DisplayName("Successful creation And loading of musician")
    @Test
    public void successfulCreationAndLoadingOfMusician() throws IOException {
        assertEquals(0, dao.loadAll(Musician.class).size());

        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));

        dao.createOrUpdate(musician);
        Musician loadedMusician = dao.load(Musician.class, musician.getId());

        assertNotNull(loadedMusician.getId());
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());

        assertEquals(1, dao.loadAll(Musician.class).size());

//        dao.delete(musician);
//        assertEquals(0, dao.loadAll(Musician.class).size());
    }

    @Test
    public void successfulDeleteOfAMusician() throws IOException {
        assertEquals(0, dao.loadAll(Musician.class).size());
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        Musician musician1 = new Musician("Edward Jenner");
        dao.createOrUpdate(musician);
        dao.createOrUpdate(musician1);
        assertEquals(2,dao.loadAll(Musician.class).size());
        dao.delete(musician);
        assertEquals(1, dao.loadAll(Musician.class).size());
        assertEquals("Edward Jenner",dao.load(Musician.class,musician1.getId()).getName());
    }

    @Test
    public void successfulUpdateOfMusician() throws IOException {
        assertEquals(0, dao.loadAll(Musician.class).size());
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        dao.createOrUpdate(musician);
        musician.setBiography("I am the greatest");
        assertEquals("I am the greatest", dao.load(Musician.class,musician.getId()).getBiography());
    }

    @Test
    public void successfulCreationOfMusicianAndAlbum() throws IOException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));


        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));


        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);

        Collection<Musician> musicians = dao.loadAll(Musician.class);
        assertEquals(1, musicians.size());
        Musician loadedMusician = musicians.iterator().next();
        assertEquals(musician, loadedMusician);
        assertEquals(musician.getMusicianUrl(), loadedMusician.getMusicianUrl());
        assertEquals(musician.getAlbums(), loadedMusician.getAlbums());
    }

    @Test
    public void successfulSearchOfMusicianByName() throws IOException {
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        Musician musician1 = new Musician("Edward Jenner");
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        musician.setAlbums(Sets.newHashSet(album));
        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);
        dao.createOrUpdate(musician1);
        Musician musician2 =dao.findMusicianByName("Edward Jenner");
        assertEquals(musician1,musician2);
    }

    @Test
    public void successfulCreationAndLoadingOfAlbums() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulDeleteOfAAlbum() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());
        dao.delete(album);
        assertEquals(0, dao.loadAll(Album.class).size());
    }

    @Test
    public void successfulUpdateOfAlbum() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        dao.createOrUpdate(album);
        album.setRecordNumber("ECM 1080");
        assertEquals("ECM 1080", dao.load(Album.class,album.getId()).getRecordNumber());
    }

    @Test
    public void successfulCreationOfAlbumWithMusician() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setFeaturedMusicians(Lists.newArrayList(musician));
        dao.createOrUpdate(album);
        dao.createOrUpdate(musician);
        assertEquals(1, dao.loadAll(Album.class).size());
        Album loadedAlbum = dao.loadAll(Album.class).iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getFeaturedMusicians(), loadedAlbum.getFeaturedMusicians());
    }

    @Test
    public void successfulCreationOfAlbumWithMusicianAndInstruments() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Musician musician = new Musician("Keith Jarrett");
        musician.setMusicianUrl(new URL("https://www.keithjarrett.org/"));
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,Sets.newHashSet(musicalInstrument));
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setInstruments(Sets.newHashSet(musicianInstrument));
        dao.createOrUpdate(musicianInstrument);
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());
        Album loadedAlbum = dao.loadAll(Album.class).iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getInstruments(), loadedAlbum.getInstruments());
    }

    @Test
    public void successfulCreationOfAlbumWithTracks() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Track track = new Track("SHE WAS YOUNG","09:33");
        album.setTracks(Sets.newHashSet(track));
        dao.createOrUpdate(track);
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());
        Album loadedAlbum = dao.loadAll(Album.class).iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getTracks(), loadedAlbum.getTracks());
    }

    @Test
    public void successfulCreationOfAlbumWithReviews() throws IOException {
        assertEquals(0, dao.loadAll(Album.class).size());
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Review review = new Review("It is a good album",10);
        album.setReviews(Sets.newHashSet(review));
        dao.createOrUpdate(review);
        dao.createOrUpdate(album);
        assertEquals(1, dao.loadAll(Album.class).size());
        Album loadedAlbum = dao.loadAll(Album.class).iterator().next();
        assertEquals(album, loadedAlbum);
        assertEquals(album.getReviews(), loadedAlbum.getReviews());
    }

    @Test
    public void successfulSearchOfAlbumByName() throws IOException {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Album album1 = new Album(1970, "ECM 1080", "The Köln");
        dao.createOrUpdate(album);
        dao.createOrUpdate(album1);
        Album album3 =dao.findAlbumByName("The Köln");
        assertEquals("The Köln",album3.getAlbumName());
    }

    @Test
    public void successfulCreationAndLoadingOfMusicalInstrument() throws IOException {
        assertEquals(0, dao.loadAll(MusicalInstrument.class).size());
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        dao.createOrUpdate(musicalInstrument);
        Collection<MusicalInstrument> musicalInstruments = dao.loadAll(MusicalInstrument.class);
        assertEquals(1, musicalInstruments.size());
    }

    @DisplayName("Successful update of musical instrument")
    @Test
    public void successfulUpdateOfMusicalInstrument() throws IOException {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        dao.createOrUpdate(musicalInstrument);
        assertEquals("Violin", dao.loadAll(MusicalInstrument.class).iterator().next().getName());
        musicalInstrument.setName("Guitar");
        Collection<MusicalInstrument> musicalInstruments = dao.loadAll(MusicalInstrument.class);
        assertEquals("Guitar", musicalInstruments.iterator().next().getName());
    }

    @DisplayName("Successful delete of musical instrument")
    @Test
    public void successfulDeleteOfMusicalInstrument() throws IOException {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicalInstrument musicalInstrument1 = new MusicalInstrument("Guitar");
        dao.createOrUpdate(musicalInstrument);
        dao.createOrUpdate(musicalInstrument1);
        Collection<MusicalInstrument> musicalInstruments1 = dao.loadAll(MusicalInstrument.class);
        assertEquals(2, musicalInstruments1.size());
        dao.delete(musicalInstrument);
        assertEquals(1,dao.loadAll(MusicalInstrument.class).size());
        Collection<MusicalInstrument> musicalInstruments = dao.loadAll(MusicalInstrument.class);
        assertEquals("Guitar", musicalInstruments.iterator().next().getName());
    }

    @DisplayName("Successful creation of musician instrument with musician and instruments")
    @Test
    public void successfulCreationOfMusicianInstrument() throws IOException {
        assertEquals(0,dao.loadAll(MusicianInstrument.class).size());
        Musician musician = new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,Sets.newHashSet(musicalInstrument));
        dao.createOrUpdate(musicianInstrument);
        assertEquals(1,dao.loadAll(MusicianInstrument.class).size());
        assertEquals(musician,dao.load(MusicianInstrument.class,musicianInstrument.getId()).getMusician());
    }

    @DisplayName("Successful update of musician instrument")
    @Test
    public void successfulUpdateOfMusicianInstrument() throws IOException {
        assertEquals(0,dao.loadAll(MusicianInstrument.class).size());
        Musician musician = new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,Sets.newHashSet(musicalInstrument));
        Musician musician1 = new Musician("Steve Swallow");
        dao.createOrUpdate(musicianInstrument);
        assertEquals(musician,dao.loadAll(MusicianInstrument.class).iterator().next().getMusician());
        musicianInstrument.setMusician(musician1);
        assertEquals(musician1,dao.loadAll(MusicianInstrument.class).iterator().next().getMusician());
    }

    @DisplayName("Successful delete of musician instrument")
    @Test
    public void successfulDeleteOfMusicianInstrument() throws IOException {
        Musician musician = new Musician("Keith Jarrett");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,Sets.newHashSet(musicalInstrument));
        dao.createOrUpdate(musicianInstrument);
        assertEquals(1,dao.loadAll(MusicianInstrument.class).size());
        dao.delete(musicianInstrument);
        assertEquals(0,dao.loadAll(MusicianInstrument.class).size());
    }

    @DisplayName("Successful creation of review")
    @Test
    public void successfulCreationOfReview() throws IOException {
        assertEquals(0,dao.loadAll(Review.class).size());
        Review review = new Review("I enjoyed it",9);
        dao.createOrUpdate(review);
        assertEquals(1,dao.loadAll(Review.class).size());
        assertEquals("I enjoyed it",dao.loadAll(Review.class).iterator().next().getAlbumReview());
    }

    @DisplayName("Successful update of review")
    @Test
    public void successfulUpdateOfReview() throws IOException {
        Review review = new Review("I enjoyed it",9);
        dao.createOrUpdate(review);
        assertEquals(9,dao.loadAll(Review.class).iterator().next().getRating());
        review.setRating(8);
        assertEquals(8,dao.loadAll(Review.class).iterator().next().getRating());
    }

    @DisplayName("Successful delete of review")
    @Test
    public void successfulDeleteOfReview() throws IOException {
        Review review = new Review("I enjoyed it",9);
        dao.createOrUpdate(review);
        assertEquals(1,dao.loadAll(Review.class).size());
        dao.delete(review);
        assertEquals(0,dao.loadAll(Review.class).size());
    }

    @DisplayName("Successful search of review by content")
    @Test
    public void successfulSearchOfReviewByContent() throws IOException {
        Review review = new Review("I enjoyed it",9);
        Review review1 = new Review("I loved it",8);
        dao.createOrUpdate(review);
        dao.createOrUpdate(review1);
        Review review3 = dao.findReviewByContent("I enjoyed it");
        assertEquals(9,review3.getRating());
    }

    @DisplayName("Successful creation of track")
    @Test
    public void successfulCreationOfTrack() {
        assertEquals(0,dao.loadAll(Track.class).size());
        Track track = new Track("SHE WAS YOUNG","09:33");
        dao.createOrUpdate(track);
        assertEquals(1,dao.loadAll(Track.class).size());
        assertEquals("SHE WAS YOUNG",dao.loadAll(Track.class).iterator().next().getTrackName());
    }

    @DisplayName("Successful update of track")
    @Test
    public void successfulUpdateOfTrack() {
        Track track = new Track("SHE WAS YOUNG","09:33");
        Track track1 = new Track("SHE WAS OLD","08:33");
        dao.createOrUpdate(track);
        dao.createOrUpdate(track1);
        track.setTrackName("INTO THE DREAMS");
        assertEquals("INTO THE DREAMS",dao.load(Track.class,track.getId()).getTrackName());
    }

    @DisplayName("Successful delete of track")
    @Test
    public void successfulDeleteOfTrack() {
        Track track = new Track("SHE WAS YOUNG","09:33");
        Track track1 = new Track("SHE WAS OLD","08:33");
        dao.createOrUpdate(track);
        dao.createOrUpdate(track1);
        assertEquals(2,dao.loadAll(Track.class).size());
        dao.delete(track1);
        assertEquals(1,dao.loadAll(Track.class).size());
    }

    @DisplayName("Successful search of track")
    @Test
    public void successfulSearchOfTrack() {
        Track track = new Track("SHE WAS YOUNG","09:33");
        Track track1 = new Track("SHE WAS OLD","08:33");
        dao.createOrUpdate(track);
        dao.createOrUpdate(track1);
        Track track3 = dao.findTrackByName("SHE WAS OLD");
        assertEquals("SHE WAS OLD",track3.getTrackName());
    }

}