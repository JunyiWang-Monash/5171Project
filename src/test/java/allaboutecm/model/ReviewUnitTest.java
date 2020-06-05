package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class ReviewUnitTest {
    private Review review;

    @BeforeEach
    public void setUp() { review = new Review( "best genre", 5); }

    @Test
    @DisplayName("Should construct review object")
    public void shouldConstructReviewObject()
    {
        assertNotNull(review, "Review object cannot be null");
    }

    @Test
    @DisplayName("Review cannot be null")
    public void reviewCannotBeNull() {
        Exception e = assertThrows(NullPointerException.class, () -> review.setAlbumReview(null));
        assertEquals("Review cannot be null",e.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "    \t"})
    @DisplayName("Review cannot be empty or blank")
    public void reviewCannotBeEmptyOrBlank(String arg) {
        Exception e = assertThrows(IllegalArgumentException.class, () -> review.setAlbumReview(arg));
        assertEquals("Review cannot be empty or blank",e.getMessage());
    }


    @Test
    public void musicalURLCannotBeNull() {
        assertThrows(NullPointerException.class,() -> review.setWebsiteURL(null));
    }

    @Test
    @DisplayName("Test set invalid review URL")
    public void testSetInvalidURL() throws IOException {
        URL url = new URL("https://www.jetbrains.com/idea/abc");
        assertThrows(IllegalArgumentException.class,() -> review.setWebsiteURL(url));
    }

    @Test
    @DisplayName("Test set valid review URL")
    public void testSetValidMusicalURL() throws IOException {
        URL url = new URL("https://www.jetbrains.com/idea");
        review.setWebsiteURL(url);
        assertEquals(url,review.getWebsiteURL());
    }

    @Test
    @DisplayName("Test set valid rating")
    public void testSetValidRating(){
        review.setRating(5);
        assertEquals(5,review.getRating());
    }

    @ParameterizedTest
    @ValueSource(ints = {0,11})
    @DisplayName("Test set invalid input")
    public void testSetInvalidInput(int rt){
        Exception e = assertThrows(IllegalArgumentException.class,() -> review.setRating(rt));
        assertEquals("Rating should be between 1 and 10",e.getMessage());
    }

    @Test
    @DisplayName("Set valid review")
    public void setValidReview() {
        review.setAlbumReview("Its an amazing album");
        assertEquals("Its an amazing album",review.getAlbumReview());
    }

    @Test
    @DisplayName("Set review with leading and trailing spaces")
    public void setValidReviewWithLeadingAndTrailingSpaces() {
        review.setAlbumReview(" Its an amazing album ");
        assertEquals("Its an amazing album",review.getAlbumReview());
    }

    //mutation kill
    @Test
    @DisplayName("Review with same album review and rating mean same review")
    public void reviewWithSameAlbumReviewAndRatingMeanSameReview()
    {
        Review review = new Review("Nice album",9);
        Review review1 = new Review("Nice album",9);
        assertEquals(review, review1);
    }

    //mutation kill
    @Test
    @DisplayName("Two different reviews are not equal")
    public void TwiDifferentReviewAreNotEqual()
    {
        Review review = new Review("Nice album",9);
        Review review1 = new Review("Nice album",8);
        assertNotEquals(review, review1);
    }

    //mutation kill
    @Test
    @DisplayName("Review is equal to itself")
    public void reviewIsEqualToItself()
    {
        assertEquals(review, review);
    }

    //mutation kill
    @Test
    @DisplayName("Review with same album review and rating mean same review")
    public void reviewCannotBeEqualToNull()
    {
        Review review = new Review("Nice album",9);
        assertNotEquals(review, null);
    }

    //mutation kill
    @Test
    @DisplayName("Review cannot be equal to musical instrument")
    public void reviewCannotBeEqualToMusicalInstrument()
    {
        Review review = new Review("Nice album",9);
        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");
        assertNotEquals(review,musicalInstrument);
    }

    //mutation kill
    @ParameterizedTest
    @ValueSource(ints = {1,10})
    @DisplayName("ratingIs1Or10")
    public void ratingIs1Or10(int a)
    {
        review.setRating(a);
        assertEquals(a,review.getRating());
    }
}