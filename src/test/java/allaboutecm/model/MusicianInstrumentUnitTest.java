package allaboutecm.model;

import com.google.common.collect.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class MusicianInstrumentUnitTest {
    private MusicianInstrument musicianInstrument;

    @BeforeEach
    public void setUp() {
        Musician musician = new Musician("Keith Jarrett");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        musicalInstruments.add(musicalInstrument);
        musicianInstrument = new MusicianInstrument(musician,musicalInstruments);
    }

    @Test
    @DisplayName("Should construct musicianInstrument object")
    public void shouldConstructMusicianInstrumentObject()
    {
        assertNotNull(musicianInstrument, "MusicianInstrument object cannot be null");
    }

    @Test
    @DisplayName("Musician cannot be null")
    public void musicianCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> musicianInstrument.setMusician(null));
        assertEquals("Musician cannot be null",e.getMessage());
    }

    @Test
    @DisplayName("Set valid musician")
    public void setValidMusician()
    {
        Musician musician1 = new Musician("Avishai Cohen");
        musicianInstrument.setMusician(musician1);
        assertEquals(musician1,musicianInstrument.getMusician());
    }

    @Test
    @DisplayName("Musical Instruments cannot be null")
    public void musicalInstrumentCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> musicianInstrument.setMusicalInstruments(null));
        assertEquals("The musical instrument cannot be null",e.getMessage());
    }

    @Test
    @DisplayName("Set valid musical instrument")
    public void setValidMusicalInstrument()
    {
        Set<MusicalInstrument> musicalInstrument1 = Sets.newHashSet();
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        musicalInstrument1.add(musicalInstrument);
        musicianInstrument.setMusicalInstruments(musicalInstrument1);
        assertEquals(musicalInstrument1,musicianInstrument.getMusicalInstruments());
    }

    @Test
    @DisplayName("Same musician and same musical instrument means same musician instrument")
    public void sameMusicianAndSameMusicalInstrumentMeansSameMusicianInstrument()
    {
        Musician musician1 = new Musician("Keith Jarrett");
        Set<MusicalInstrument> musicalInstruments = Sets.newHashSet();
        MusicalInstrument musicalInstrument = new MusicalInstrument("Violin");
        musicalInstruments.add(musicalInstrument);
        MusicianInstrument musicianInstrument1 = new MusicianInstrument(musician1,musicalInstruments);
        MusicianInstrument musicianInstrument2 = new MusicianInstrument(musician1,musicalInstruments);
        assertEquals(musicianInstrument1,musicianInstrument2);
    }


}