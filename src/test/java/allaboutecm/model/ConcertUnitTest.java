package allaboutecm.model;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcertUnitTest {
    private Concert concert;

    @BeforeEach
    public void setup()
    {
        Musician musician = new Musician("Keith Jarrett");
        List<Musician> musicians = Lists.newArrayList(musician);
        concert = new Concert("04-07-2020",musicians,"Melbourne Festival",
                "Melbourne","Australia");
    }

    @Test
    public void shouldConstructConcertObject(){
        assertNotNull(concert,"Concert object cannot be null");
    }

    @Test
    public void testSetValidDate()
    {
        concert.setDate("08-07-2020");
        assertEquals("08-07-2020",concert.getDate());
    }

    @Test
    public void dateCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> concert.setDate(null));
        assertEquals("date cannot be null",e.getMessage());
    }

    @Test
    public void testSetValidListMusicians()
    {
        Musician musician = new Musician("John Snow");
        Musician musician1 = new Musician("Roman Reigns");
        List<Musician> musicians = Lists.newArrayList();
        musicians.add(musician);
        musicians.add(musician1);
        concert.setMusiciansList(musicians);
        assertEquals(musicians,concert.getMusiciansList());
    }

    @Test
    public void testSetValidCity()
    {
        concert.setCity("Sydney");
        assertEquals("Sydney",concert.getCity());
    }

    @Test
    public void cityCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> concert.setCity(null));
        assertEquals("city cannot be null",e.getMessage());
    }

    @Test
    public void testSetValidCountry()
    {
        concert.setCountry("Hungary");
        assertEquals("Hungary",concert.getCountry());
    }

    @Test
    public void countryCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> concert.setCountry(null));
        assertEquals("country cannot be null",e.getMessage());
    }

    @Test
    public void testSetValidConcertName()
    {
        concert.setConcertName("Sydney festival");
        assertEquals("Sydney festival",concert.getConcertName());
    }

    @Test
    public void concertNameCannotBeNull()
    {
        Exception e = assertThrows(NullPointerException.class,() -> concert.setConcertName(null));
        assertEquals("concert name cannot be null",e.getMessage());
    }
}
