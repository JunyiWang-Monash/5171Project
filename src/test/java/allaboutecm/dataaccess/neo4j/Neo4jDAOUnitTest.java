package allaboutecm.dataaccess.neo4j;

import allaboutecm.dataaccess.DAO;
import allaboutecm.mining.ECMMiner;
import allaboutecm.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

    @Test
    public void daoIsNotEmpty() {
        assertNotNull(dao);
    }

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
        assertEquals(0, dao.loadAll(MusicianInstrument.class).size());
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        dao.createOrUpdate(musicalInstrument);
        Collection<MusicalInstrument> musicalInstruments = dao.loadAll(MusicalInstrument.class);
        assertEquals(1, musicalInstruments.size());
    }

}