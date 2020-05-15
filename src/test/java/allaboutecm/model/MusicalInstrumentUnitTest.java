package allaboutecm.model;

import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class MusicalInstrumentUnitTest {
    private MusicalInstrument  musicalInstrument;

    @BeforeEach
    public void setUp() {  musicalInstrument = new MusicalInstrument( "Piano");}

    @Test
    @DisplayName(("Should construct musical instrument object"))
    public void shouldConstructAMusicalInstrumentObject(){
        assertNotNull(musicalInstrument, "MusicalInstrument object cannot be null");
    }

    @Test
    @DisplayName("MusicalInstrument name cannot be null")
    public void musicalInstrumentNameCannotBeNull() {
        Exception e = assertThrows(NullPointerException.class, () -> musicalInstrument.setName(null));
        assertEquals("Musical instrument name cannot be null",e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("MusicalInstrument name cannot be empty or blank")
    public void musicalInstrumentNameCannotBeEmptyOrBlank(String arg) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> musicalInstrument.setName(arg));
        assertEquals("Musical instrument name cannot be empty or blank",e.getMessage());
    }

    @Test
    public void setMusicalInstrumentName() {
        musicalInstrument.setName("Piano");
        assertEquals("Piano",musicalInstrument.getName(), "The name is functioning as expected");
    }


    @Test
    @DisplayName("Test set album name with leading and trailing spaces")
    public void testSetNameWithLeadingAndTrailingSpaces() {
        musicalInstrument.setName(" Piano ");
        assertEquals("Piano", this.musicalInstrument.getName());
    }
}
