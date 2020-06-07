package allaboutecm.model;

import jdk.nashorn.internal.objects.annotations.Property;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.Objects;


import static org.apache.commons.lang3.Validate.*;


public class Review extends Entity{
    @Property(name = "albumReview")
    private String  albumReview;
    @Property(name = "rating")
    private int rating;
    private URL websiteURL;

    public Review(String albumReview, int rating){
        notNull(albumReview);
        notBlank(albumReview);

        this.albumReview = albumReview;
        this.rating = rating;
        this.websiteURL = null;

    }

    public String getAlbumReview() { return albumReview; }

    public void setAlbumReview(String albumReview){
        notNull(albumReview,"Review cannot be null");
        notBlank(albumReview,"Review cannot be empty or blank");

        this.albumReview = albumReview.trim();
    }

    public int getRating(){return rating;}

    public void setRating(int rating){
        if (1 <= rating && rating <= 10)
            this.rating= rating;
        else
            throw new IllegalArgumentException("Rating should be between 1 and 10");
    }

    public URL getWebsiteURL() {
        notNull(websiteURL,"websiteURL is null");
        return websiteURL;
    }
    public void setWebsiteURL(URL musicalURL)  throws IOException {
        notNull(musicalURL);
        HttpURLConnection connection = (HttpURLConnection) musicalURL.openConnection();
        int responseCode = connection.getResponseCode();
        if (responseCode == 200)
            this.websiteURL = musicalURL;
        else
            throw new IllegalArgumentException("Url is not valid");
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return albumReview.equals(review.albumReview) &&
                rating == review.rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(albumReview,rating);
    }




}