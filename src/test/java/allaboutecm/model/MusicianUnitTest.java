package allaboutecm.model;

import com.google.common.collect.Sets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MusicianUnitTest {
    private Musician musician;

    @BeforeEach
    public void setUp() {
        musician = new Musician("KEITH JARRETT");
    }

    @Test
    @DisplayName("Should construct musician object")
    public void shouldConstructMusicianObject() {
        assertNotNull(musician, "Musician object cannot be null");
    }

    @Test
    @DisplayName("Musician name cannot be null")
    public void musicianNameCannotBeNull() {
        Exception e = assertThrows(NullPointerException.class, () -> musician.setName(null));
        assertEquals("Musician name cannot be null", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    @DisplayName("Musician name length cannot be greater than or equal to 40")
    public void musicianNameCannotBeGreaterThanOrEqualTo40(String args)
    {
        assertThrows(IllegalArgumentException.class,() -> new Musician(args));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Musician name cannot be empty or blank")
    public void musicianNameCannotBeEmptyOrBlank(String arg) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> musician.setName(arg));
        assertEquals("Musician name cannot be empty or blank", e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa","aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"})
    @DisplayName("Set musician name length cannot be greater than or equal to 40")
    public void SetMusicianNameCannotBeGreaterThanOrEqualTo40(String args)
    {
        assertThrows(IllegalArgumentException.class,() -> musician.setName(args));
    }

    @Test
    @DisplayName("Same musician name means same Musician")
    public void sameMusicianNameMeansSameMusician()
    {
        Musician musician1 = new Musician("KEITH JARRETT");
        assertEquals(musician,musician1);
    }

    @Test
    @DisplayName("Test set valid musician name")
    public void testSetValidMusicianName(){
        musician.setName("Trevor Watts");
        assertEquals("Trevor Watts", musician.getName());
    }

    @Test
    @DisplayName("Test set musician name with leading and trailing spaces")
    public void testSetMusicianNameWithLeadingAndTrailingSpaces(){
        musician.setName(" Trevor Watts ");
        assertEquals("Trevor Watts",musician.getName());
    }

    @Test
    @DisplayName("Set of albums cannot be null")
    public void setOfAlbumsCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> musician.setAlbums(null));
        assertEquals("Set of albums cannot be null",e.getMessage());
    }

    @DisplayName("Setting up the album set with album")
    public Set<Album> settingUpTheAlbumSetWithAlbum() {
        Set<Album> test = Sets.newLinkedHashSet();
        Album album;
        album = new Album(2019, "ECM 2667", "Keith Jarrett");
        test.add(album);
        return test;
    }

    @Test
    public void testSetAlbums()
    {
        Set<Album> test = settingUpTheAlbumSetWithAlbum();
        musician.setAlbums(test);
        assertEquals(test,musician.getAlbums());
    }

    @Test
    @DisplayName("Set of albums cannot contain null element")
    public void setOfAlbumsCannotContainNullElement()
    {
        Set<Album> test = Sets.newLinkedHashSet();
        test.add(null);
        Exception e = assertThrows(NullPointerException.class,() -> musician.setAlbums(test));
        assertEquals("Set of albums cannot contain null element",e.getMessage());
    }

    @Test
    @DisplayName("Test set an invalid musician Url")
    public void testSetInvalidMusicianUrl() throws IOException {
        URL url = new URL("https://www.jetbrains.com/idea/123");
        assertThrows(IllegalArgumentException.class,() -> musician.setMusicianUrl(url));
    }

    @Test
    @DisplayName("Test set a valid musician Url")
    public void testSetValidAlbumURL() throws IOException {
        URL url = new URL("https://www.jetbrains.com/idea/");
        musician.setMusicianUrl(url);
        assertEquals(url,musician.getMusicianUrl());
    }

    @ParameterizedTest
    @ValueSource(strings = {""," ","  \t"})
    @DisplayName("Cannot set empty or blank biography")
    public void cannotSetEmptyBiography(String args){
        Exception e = assertThrows(IllegalArgumentException.class,() -> musician.setBiography(args));
        assertEquals("Cannot set empty or blank biography",e.getMessage());
    }

    @Test
    @DisplayName("Set valid biography")
    public void setValidBiography(){
        musician.setBiography("Keith Jarrett is a multicultural jazz musician, among whose ancestors is Miles Davis. Like Davis, he can make the trumpet a vehicle for uttering the most poignant human cries.");
        assertEquals("Keith Jarrett is a multicultural jazz musician, among whose ancestors is Miles Davis. Like Davis, he can make the trumpet a vehicle for uttering the most poignant human cries.",musician.getBiography());
    }
}