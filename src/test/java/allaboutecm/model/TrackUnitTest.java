package allaboutecm.model;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrackUnitTest {

    private Track track;

    @BeforeEach
    public void setUp() {
        track = new Track("Honey Fountain","04:34");
    }

    @Test
    @DisplayName("Should construct track object")
    public void shouldConstructTrackObject()
    {
        assertNotNull(track, "Track object cannot be null");
    }

    @Test
    @DisplayName("Track name cannot be null")
    public void trackNameCannotBeNull() {
        Exception e = assertThrows(NullPointerException.class, () -> track.setTrackName(null));
        assertEquals("Track name cannot be null",e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Track name cannot be empty or blank")
    public void trackNameCannotBeEmptyOrBlank(String arg) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> track.setTrackName(arg));
        assertEquals("Track name cannot be empty or blank",e.getMessage());
    }

    @Test
    @DisplayName("Test set valid track name")
    public void testSetValidTrackName(){
        track.setTrackName("The things you tell me");
        assertEquals("The things you tell me",track.getTrackName());
    }

    @Test
    @DisplayName("Test set album name with leading and trailing spaces")
    public void testSetTrackNameWithLeadingAndTrailingSpaces(){
        track.setTrackName(" The things you tell me ");
        assertEquals("The things you tell me", track.getTrackName());
    }

    @Test
    @DisplayName("Length cannot be null")
    public void lengthCannotBeNull(){
        assertThrows(NullPointerException.class,() -> track.setLength(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {" ","","  \t"})
    @DisplayName("Length cannot be empty or blank")
    public void lengthCannotBeEmptyOrBlank(String args){
        Exception e = assertThrows(IllegalArgumentException.class,() -> track.setLength(args));
        assertEquals("Length cannot be empty or blank",e.getMessage());
    }

    @Test
    @DisplayName("Test set valid Length")
    public void testSetValidLength(){
        track.setLength("26:07");
        assertEquals("26:07",track.getLength());
    }

}