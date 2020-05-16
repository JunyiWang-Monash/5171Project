package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.dataaccess.neo4j.Neo4jDAO;
import allaboutecm.model.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    public void shouldReturnTheCooperatedMusicianWhenThereIsOnlyOneCooperatedMusician() {
        Album album = new Album(2010, "ECM 2165", "JASMINE");
        Musician musician = new Musician("Keith Jarrett");
        Musician musician1 = new Musician("Charlie Haden");
        List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician);
        albumMusicians.add(musician1);
        album.setFeaturedMusicians(albumMusicians);
        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(albumMusicians));

        List<Musician> musicians = ecmMiner.mostSocialMusicians(3);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));
    }

    @Test
    public void shouldReturnTheMostSocialMusician() {
        Album album1 = new Album(2018, "ECM 2590", "After the Fall");
        Album album2 = new Album(1973, "ECM 1021", "Ruta and Daitya");
        Musician musician = new Musician("Keith Jarrett");
        Musician musician1 = new Musician("Gary Peacock");
        Musician musician2 = new Musician("Jack Dejohnette");

        Set<Album> albums = Sets.newHashSet();
        albums.add(album1);
        albums.add(album2);
        musician.setAlbums(albums);
        musician1.setAlbums(Sets.newHashSet(album1));
        musician2.setAlbums(albums);

        List<Musician> albumMusicians = Lists.newArrayList();
        albumMusicians.add(musician);
        albumMusicians.add(musician1);
        albumMusicians.add(musician2);
        album1.setFeaturedMusicians(albumMusicians);
        List<Musician> albumMusicians1 = albumMusicians.subList(1, albumMusicians.size());
        albumMusicians1.add(musician);
        albumMusicians1.add(musician2);
        album2.setFeaturedMusicians(albumMusicians1);

        when(dao.loadAll(Musician.class)).thenReturn(Sets.newHashSet(albumMusicians));
        List<Musician> musicians = ecmMiner.mostSocialMusicians(2);
        assertEquals(2, musicians.size());
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

    @Test
    public void shouldReturnTheMusicianWhenThereIsOnlyOne1(){
        MusicalInstrument musicialInstrument= new MusicalInstrument("PIANO");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        musicalInstruments.add(musicialInstrument);
        Musician musician= new Musician("Keith Jarrett");
        MusicianInstrument musicianInstrument=new MusicianInstrument(musician, musicalInstruments);
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(Sets.newHashSet(musicianInstrument));

        List<Musician> musicians = ecmMiner.mostTalentedMusicians(5);

        assertEquals(1, musicians.size());
        assertTrue(musicians.contains(musician));

    }
    // Extra Credit
    @DisplayName("Should return the best-selling albums when there is only one")
    @Test
    public void shouldReturnTheBestSellingAlbumsWhenThereIsOnlyOne() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setSales(50);
        when(dao.loadAll(Album.class)).thenReturn(Sets.newHashSet(album));;
        List<Album> albums = ecmMiner.bestSellingAlbums(3);
        assertEquals(50,albums.iterator().next().getSales());
    }
    // Extra Credit
    @DisplayName("Should not return the best-selling albums when k is 0")
    @Test
    public void shouldNotReturnTheBestSellingAlbumsWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        album.setSales(50);
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        when(dao.loadAll(Album.class)).thenReturn(albums);
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
        album.setSales(500);
        album1.setSales(1000);
        album2.setSales(2000);
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        albums.add(album1);
        albums.add(album2);
        when(dao.loadAll(Album.class)).thenReturn(albums);
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
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        albums.add(album1);
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> albums1 = ecmMiner.highestRatedAlbums(1);
        assertEquals(1,albums1.size());
        assertEquals("Swallow tales",albums1.get(0).getAlbumName());
    }

    // Extra Credit
    @DisplayName("Should not return the highest rated albums when k is 0")
    @Test
    public void shouldNotReturnTheHighestRatedAlbumsWhenKIsZero() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Set<Review> reviews = Sets.newHashSet();
        Review review = new Review("Very nice",9);
        Review review1 = new Review("Very good",8);
        reviews.add(review);
        reviews.add(review1);
        album.setReviews(reviews);
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> albums1 = ecmMiner.highestRatedAlbums(0);
        assertEquals(0,albums1.size());
    }

    // Extra Credit
    @DisplayName("Should return the highest rated albums when only one album")
    @Test
    public void shouldNotReturnTheHighestRatedAlbumsWhenOnlyOneAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Set<Review> reviews = Sets.newHashSet();
        Review review = new Review("Very nice",9);
        Review review1 = new Review("Very good",8);
        reviews.add(review);
        reviews.add(review1);
        album.setReviews(reviews);
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> albums1 = ecmMiner.highestRatedAlbums(3);
        assertEquals(1,albums1.size());
        assertEquals("The Köln Concert",albums1.get(0).getAlbumName());
    }

    @DisplayName("Should return the most similar album")
    @Test
    public void shouldReturnTheMostSimilarAlbum() {
        Album album = new Album(1975, "ECM 1064/65", "The Köln Concert");
        Musician musician= new Musician("Keith Jarrett");
        Musician musician1 = new Musician("John Snowfield");
        Musician musician2 = new Musician("Steve Swallow");
        List<Musician> musicians = Lists.newArrayList();
        musicians.add(musician);
        musicians.add(musician1);
        musicians.add(musician2);
        album.setFeaturedMusicians(musicians);
        Album album1 = new Album(1974, "ECM 1080", "Swallow Tales");
        Musician musician3= new Musician("Keith Jarrett");
        Musician musician4 = new Musician("John Scofield");
        Musician musician5 = new Musician("Steve Thomas");
        List<Musician> musicians1 = Lists.newArrayList();
        musicians1.add(musician3);
        musicians1.add(musician4);
        musicians1.add(musician5);
        album1.setFeaturedMusicians(musicians1);
        Album album2 = new Album(1977, "ECM 1090", "Swat Kats");
        album2.setFeaturedMusicians(musicians);
        Album album3 = new Album(1979, "ECM 1091", "Noddy");
        album3.setFeaturedMusicians(musicians);
        Set<Album> albums = Sets.newHashSet();
        albums.add(album);
        albums.add(album1);
        albums.add(album2);
        albums.add(album3);
        when(dao.loadAll(Album.class)).thenReturn(albums);
        List<Album> similarAlbums = ecmMiner.mostSimilarAlbums(2,album2);
        assertEquals(2,similarAlbums.size());
        assertEquals("The Köln Concert",similarAlbums.get(0).getAlbumName());
        assertEquals("Noddy",similarAlbums.get(1).getAlbumName());
    }

    @DisplayName("Should return the most talented musician")
    @Test
    public void shouldReturnTheMostTalentedMusicianBackup(){
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
        musicianInstruments.add(musicianInstrument);
        musicianInstruments.add(musicianInstrument1);
        musicianInstruments.add(musicianInstrument2);
        when(dao.loadAll(MusicianInstrument.class)).thenReturn(musicianInstruments);
        List<Musician> musicians = ecmMiner.mostTalentedMusician1(2);
        assertEquals(2,musicians.size());
        assertEquals("John Snowfield",musicians.iterator().next().getName());
    }
}