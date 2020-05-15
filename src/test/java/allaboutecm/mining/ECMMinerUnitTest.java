package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO: perform unit testing on the ECMMiner class, by making use of mocking.
 */
class ECMMinerUnitTest {
    private DAO dao;
    private ECMMiner ecmMiner;

    @BeforeEach
    public void setUp() {

        dao = mock(Neo4jDAO.class);
        ecmMiner = new ECMMiner(dao);
    }

    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMiner.mostProlificMusicians(5, -1, -1);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldReturnTheMusicianHimOrHerselfAsTheMostSocialMusicianWhenThereIsOnlyOneMusician() {
        //Album album = new Album(2010, "ECM 2165", "JASMINE");
        //Musician musician1 = new Musician("Charlie Haden");
        /*List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician);
        albumMusicians.add(musician1);
        album.setFeaturedMusicians(albumMusicians);
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(albumMusicians));*/
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");

        musician.setAlbums(Sets.newHashSet(album));
        List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician);
        album.setFeaturedMusicians(albumMusicians);
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMiner.mostSocialMusicians(3);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldNotReturnReturnTheMostSocialMusicianWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician = new Musician("Keith Jarrett");
        musician.setAlbums(Sets.newHashSet(album));
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(musician));

        List<Musician> musicians = ecmMiner.mostSocialMusicians(0);

        assertEquals(0, musicians.size());
    }

    @Test
    public void shouldReturnTheMostSocialMusician() {
        Album album1 = new Album(2018, "ECM 2590", "After the Fall");
        Album album2 = new Album(1973, "ECM 1021", "Ruta and Daitya");
        Musician musician = new Musician("Keith Jarrett");
        Musician musician1 = new Musician("Gary Peacock");
        Musician musician2 = new Musician("Jack Dejohnette");

        List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician);
        albumMusicians.add(musician1);
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

        Set<Musician> albumMusicians2 = Sets.newHashSet();
        albumMusicians2.add(musician);
        albumMusicians2.add(musician1);
        albumMusicians2.add(musician2);

        when(dao.loadAll(Musician.class)).thenReturn(albumMusicians2);
        List<Musician> musicians = ecmMiner.mostSocialMusicians(3);
        assertEquals(3, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldReturnTheBusiestYearWhenThereIsOnlyOneAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Integer> busiestYears = ecmMiner.busiestYear(4);
        assertEquals(1,busiestYears.size());
        assertTrue(busiestYears.contains(1975));
    }

    @Test
    public void shouldNotReturnTheBusiestYearWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        when(dao.loadAll(Album.class)).thenReturn(albums);
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

        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        albums.add(album4);
        albums.add(album5);
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Integer> busiestYears = ecmMiner.busiestYear(2);
        assertEquals(2,busiestYears.size());
    }
}