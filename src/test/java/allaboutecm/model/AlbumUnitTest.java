package allaboutecm.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AlbumUnitTest {
    private Album album;

    @BeforeEach
    public void setUp() {
        album = new Album(1975, "ECM 1064/65", "The Köln Concert");
    }

    @Test
    @DisplayName("Should construct album object")
    public void shouldConstructAlbumObject()
    {
        assertNotNull(album, "Album object cannot be null");
    }

    @ParameterizedTest
    @ValueSource(ints = {1971,1970})
    @DisplayName("Album release year should be greater than or equal 1970")
    public void albumReleaseYearShouldBeGreaterThanOrEqualTo1970(int year)
    {
        Album album1 = new Album(year,"ECM 1090","ABCD");
        assertNotNull(album1);
    }

    @Test
    @DisplayName("Album release year cannot be less than 1970")
    public void albumReleaseYearCannotBeLessThan1970()
    {
        assertThrows(IllegalArgumentException.class,() -> new Album(1969,"ECM 1090","ABCD"));
    }

    @Test
    @DisplayName("Album release year cannot be greater than current year")
    public void albumReleaseYearCannotBeGreaterThanCurrentYear()
    {
        int year = Year.now().getValue() + 1;
        assertThrows(IllegalArgumentException.class,() -> new Album(year,"ECM 1090","ABCD"));
    }

    @Test
    @DisplayName("Album release year should be less than or equal to current year")
    public void albumReleaseYearShouldBeLessThanOrEqualToCurrentYear()
    {
        int year = Year.now().getValue();
        Album album1 = new Album(year,"ECM 1090","ABCD");
        assertNotNull(album1);
    }

    @Test
    @DisplayName("Album name cannot be null")
    public void albumNameCannotBeNull() {
        Exception e = assertThrows(NullPointerException.class, () -> album.setAlbumName(null));
        assertEquals("Album name cannot be null",e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Album name cannot be empty or blank")
    public void albumNameCannotBeEmptyOrBlank(String arg) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> album.setAlbumName(arg));
        assertEquals("Album name cannot be empty or blank",e.getMessage());
    }

    @Test
    public void sameNameAndNumberMeansSameAlbum() {
        Album album1 = new Album(1975, "ECM 1064/65", "The Köln Concert");
        assertEquals(album, album1);
    }


    @Test
    @DisplayName("Set of featured musicians cannot be null")
    public void setOfFeaturedMusiciansCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> album.setFeaturedMusicians(null));
        assertEquals("Set of featured musicians cannot be null",e.getMessage());
    }

    @Test
    public void testSetFeaturedMusicians()
    {
        List<Musician> test = settingUpTheMusicianList();
        album.setFeaturedMusicians(test); //
        assertEquals(test,album.getFeaturedMusicians());
    }

    @Test
    @DisplayName("Set of featured musicians cannot contain null element")
    public void setOfFeaturedMusiciansCannotContainNullElement()
    {
        //List<Musician> test = Sets.newHashSet();
        List<Musician> test = Lists.newArrayList();
        test.add(null);
        Exception e = assertThrows(NullPointerException.class,() -> album.setFeaturedMusicians(test));
        assertEquals("Set of featured musicians cannot contain null element",e.getMessage());
    }

    @Test
    public void musicianInstrumentsSetCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> album.setInstruments(null));
        assertEquals("Set of musician instruments cannot be null",e.getMessage());
    }

    @DisplayName("Setting up the musician instrument set with musician")
    public Set<MusicianInstrument> settingUpTheMusicianInstrumentSetWithMusicianInstrument()
    {
        Set<MusicianInstrument> musicianInstruments = Sets.newHashSet();
        Musician musician;
        musician = new Musician("John");
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        musicalInstruments.add(musicalInstrument);
        MusicianInstrument musicianInstrument = new MusicianInstrument(musician,musicalInstruments);
        musicianInstruments.add(musicianInstrument);
        return musicianInstruments;
    }

    @DisplayName("Setting up the Musician list ")
    public List<Musician> settingUpTheMusicianList()
    {
        List<Musician> musicians = Lists.newArrayList();
        Musician musician;
        musician = new Musician("John");
        musicians.add(musician);
        return musicians;
    }

    @Test
    public void testSetOfValidMusicianInstruments()
    {
        Set<MusicianInstrument> test = settingUpTheMusicianInstrumentSetWithMusicianInstrument();
        album.setInstruments(test);
        assertEquals(test,album.getInstruments());
    }

    @Test
    @DisplayName("Set of instruments cannot contain null element")
    public void setOfInstrumentsCannotContainNullElement()
    {
        Set<MusicianInstrument> musicianInstruments = Sets.newHashSet();
        musicianInstruments.add(null);
        Exception e = assertThrows(NullPointerException.class,() -> album.setInstruments(musicianInstruments));
        assertEquals("Set of musician instruments cannot contain null element",e.getMessage());
    }

    @Test
    public void albumURLCannotBeNull() {
        assertThrows(NullPointerException.class,() -> album.setAlbumURL(null));
    }

    @Test
    @DisplayName("Test set invalid album URL")
    public void testSetInvalidAlbumURL() throws IOException {
        URL url = new URL("https://www.jetbrains.com/idea/abc");
        assertThrows(IllegalArgumentException.class,() -> album.setAlbumURL(url));
    }

    @Test
    @DisplayName("Test set valid album URL")
    public void testSetValidAlbumURL() throws IOException {
        URL url = new URL("https://www.jetbrains.com/idea");
        album.setAlbumURL(url);
        assertEquals(url,album.getAlbumURL());
    }

    @Test
    @DisplayName("Test set valid album name")
    public void testSetValidAlbumName(){
        album.setAlbumName("Big Vicious");
        assertEquals("Big Vicious",album.getAlbumName());
    }

    @Test
    @DisplayName("Test set album name with leading and trailing spaces")
    public void testSetAlbumNameWithLeadingAndTrailingSpaces(){
        album.setAlbumName(" Big Vicious ");
        assertEquals("Big Vicious",album.getAlbumName());
    }

    @Test
    public void setOfTracksCannotBeNull(){
        Exception e = assertThrows(NullPointerException.class, () -> album.setTracks(null));
        assertEquals("Set of tracks cannot be null",e.getMessage());
    }

   @Test
    @DisplayName("Set of tracks cannot contain null elements")
    public void setOfTracksCannotContainNullElements(){
        Set<Track> str = Sets.newHashSet();
        str.add(null);
        Exception e = assertThrows(NullPointerException.class,() -> album.setTracks(str));
        assertEquals("Set of track cannot contain null elements",e.getMessage());
    }

    @Test
    @DisplayName("test set valid tracks")
    public void testSetValidTracks()
    {
        Set<Track> str = Sets.newHashSet();
        Track track = new Track("Before you die","26:10",1);
        str.add(track);
        album.setTracks(str);
        assertEquals(str,album.getTracks());
    }

    @Test
    @DisplayName("Record number cannot be null")
    public void recordNumberCannotBeNull(){
        assertThrows(NullPointerException.class,() -> album.setRecordNumber(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ",""})
    @DisplayName("Record number cannot be empty or blank")
    public void recordNumberCannotBeEmptyOrBlank(String args){
        assertThrows(IllegalArgumentException.class,() -> album.setRecordNumber(args));
    }

    @Test
    @DisplayName("Record number should start with ECM ")
    public void recordNumberShouldStartWithECMWithSpace(){
        assertThrows(IllegalArgumentException.class,() -> album.setRecordNumber("ECM"));
    }

    @Test
    @DisplayName("Test set valid record number")
    public void testSetValidRecordNumber(){
        album.setRecordNumber("ECM 1234");
        assertEquals("ECM 1234",album.getRecordNumber());
    }

    @ParameterizedTest
    @ValueSource(ints = {1699, 2029})
    @DisplayName("Release year must be in the range 1970 to current year")
    public void releaseYearMustBeInTheRange1970ToCurrentYear(){
        assertThrows(IllegalArgumentException.class,() -> album.setReleaseYear(1699));
    }

    @Test
    @DisplayName("Test set valid release year")
    public void testSetValidReleaseYear(){
        album.setReleaseYear(2020);
        assertEquals(2020,album.getReleaseYear());
    }

    @Test
    @DisplayName("Set of reviews cannot contain null element")
    public void setOfReviewsCannotContainNullElement()
    {
        Exception e = assertThrows(NullPointerException.class,() -> album.setReviews(null));
        assertEquals("Set of album reviews cannot be null",e.getMessage());
    }

    @DisplayName("Set up reviews set with a valid review")
    public Set<Review> setUpValidReview() {
        Set<Review> reviews = Sets.newHashSet();
        Review review = new Review("Its an amazing Album",7);
        reviews.add(review);
        return reviews;
    }

    @Test
    @DisplayName("Set valid reviews")
    public void setValidReviews() {
        Set<Review> reviews = setUpValidReview();
        album.setReviews(reviews);
        assertEquals(reviews,album.getReviews());
    }


    @Test
    public void listOfReleaseFormatCannotBeNull(){
        assertThrows(NullPointerException.class, () -> album.setReleaseFormat(null));
    }

    @Test
    @DisplayName("List of release format cannot contain null element")
    public void listOfReleaseFormatCannotContainsNullElement(){
        List<String> str = Lists.newArrayList();
        str.add(null);
        assertThrows(NullPointerException.class,() -> album.setReleaseFormat(str));
    }

    @Test
    @DisplayName("List of release format cannot contain empty element")
    public void listOfReleaseFormatCannotContainsEmptyElement(){
        List<String> str = Lists.newArrayList();
        str.add("");
        assertThrows(IllegalArgumentException.class,() -> album.setReleaseFormat(str));
    }

    @Test
    @DisplayName("test set valid list of release format")
    public void testSetValidListOfReleaseFormat(){
        List<String> str = Lists.newArrayList();
        str.add("CD, LP");
        album.setReleaseFormat(str);
        assertEquals(str,album.getReleaseFormat());
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Genre cannot be empty or blank")
    public void genreCannotBeEmptyOrBlank(String arg) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> album.setGenre(arg));
        assertEquals("Genre cannot be empty or blank",e.getMessage());
    }

    @Test
    @DisplayName("Test set valid genre name")
    public void testSetValidGenre(){
        album.setGenre("opera");
        assertEquals("opera", album.getGenre());
    }
    //adding this method to test
    @Test
    @DisplayName("Test set valid sales")
    public void testSetValidSales(){
        album.setSales(400);
        assertEquals(400, album.getSales());
    }
    //adding this method to test
    @Test
    @DisplayName("Test set invalid sales")
    public void testSetInValidSales(){
        Exception e = assertThrows(IllegalArgumentException.class,() -> album.setSales(-3));
        assertEquals("Sales cannot be negative",e.getMessage());
    }
}