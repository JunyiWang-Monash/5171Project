package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Year;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.*;

/**
 * Represents an album released by ECM records.
 *
 * See {@https://www.ecmrecords.com/catalogue/143038750696/the-koln-concert-keith-jarrett}
 */
@NodeEntity
public class Album extends Entity {

    @Property(name="releaseYear")
    private int releaseYear;

    @Property(name="recordNumber")
    private String recordNumber;

    @Property(name="albumName")
    private String albumName;

    /**
     * CHANGE: instead of a set, now featuredMusicians is a list,
     * to better represent the order in which musicians are featured in an album.
     */
    @Relationship(type="featuredMusicians")
    private List<Musician> featuredMusicians;

    @Relationship(type="instruments")
    private Set<MusicianInstrument> instruments;

    @Convert(URLConverter.class)
    @Property(name="albumURL")
    private URL albumURL;

    @Relationship(type="contains")
    private Set<Track> tracks;

    @Relationship(type="has")
    private Set<Review> reviews;

    @Property(name="genre")
    private String genre;

    @Property(name="style")
    private String style;

    @Property(name="releaseFormat")
    private List<String> releaseFormat;

    @Property(name="sales")
    private int sales;

    public Album() {
    }

    public Album(int releaseYear, String recordNumber, String albumName) {
        notNull(recordNumber);
        notNull(albumName);

        notBlank(recordNumber);
        notBlank(albumName);
        if (releaseYear >= 1970 && releaseYear <= Year.now().getValue())
            this.releaseYear = releaseYear;
        else
            throw new IllegalArgumentException("Year should be between 1970 - current year");
        this.releaseYear = releaseYear;
        this.recordNumber = recordNumber;
        this.albumName = albumName;

        this.albumURL = null;

        featuredMusicians = Lists.newArrayList();
        instruments = Sets.newHashSet();
        tracks = Sets.newHashSet();
        sales = 0;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        notNull(recordNumber,"Record number cannot be null");
        notBlank(recordNumber,"Record number cannot be blank");  // newly added
        if(recordNumber.startsWith("ECM "))
            this.recordNumber = recordNumber;
        else
            throw new IllegalArgumentException("Record number should start with ACM ");

    }

    public List<Musician> getFeaturedMusicians() {
        return featuredMusicians;
    }

    public void setFeaturedMusicians(List<Musician> featuredMusicians) {
        notNull(featuredMusicians,"Set of featured musicians cannot be null");
        for (Musician musician : featuredMusicians) {
            if (musician == null)
                throw new NullPointerException("Set of featured musicians cannot contain null element");
        }
        this.featuredMusicians = featuredMusicians;
    }

    public Set<MusicianInstrument> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<MusicianInstrument> instruments) {
        notNull(instruments,"Set of musician instruments cannot be null");
        for (MusicianInstrument musicianInstrument : instruments) {
            if (musicianInstrument == null)
                throw new NullPointerException("Set of musician instruments cannot contain null element");

        }
        this.instruments = instruments;
    }

    public URL getAlbumURL() {
        return albumURL;
    }

    public void setAlbumURL(URL albumURL) throws IOException {
        notNull(albumURL);
        HttpURLConnection connection = (HttpURLConnection) albumURL.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200)
            this.albumURL = albumURL;
        else
            throw new IllegalArgumentException("Url is not valid");
    }

    public Set<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Set<Track> tracks) {
        notEmpty(tracks,"Set of tracks cannot be null");
        for (Track track : tracks) {
            if (track == null)
                throw new NullPointerException("Set of track cannot contain null elements");
        }
        this.tracks = tracks;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        if (releaseYear >= 1970 && releaseYear <= Year.now().getValue())
            this.releaseYear = releaseYear;
        else
            throw new IllegalArgumentException("Year should be between 1970 - current year");
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        notNull(albumName,"Album name cannot be null");
        notBlank(albumName,"Album name cannot be empty or blank");
        this.albumName = albumName.trim();
    }

    public void setReviews(Set<Review> reviews) {
        notEmpty(reviews,"Set of album reviews cannot be null");
        for (Review review : reviews) {
            if (review == null)
                throw new NullPointerException("Set of album reviews cannot contain null element");
        }
        this.reviews = reviews;
    }

    public Set<Review> getReviews(){
        return reviews;
    }

    public String getGenre(){return genre;}

    public void setGenre(String genre){

        notNull(genre,"Genre cannot be null");
        notBlank(genre,"Genre cannot be empty or blank");
        this.genre= genre.trim();
    }

    public String getStyle(){return style;}

    public void setStyle(String style){
        notNull(style,"Album style cannot be null");
        notBlank("Album style cannot be empty or blank");
        this.style = style.trim();
    }

    //getReleaseFormat
    public List<String> getReleaseFormat(){  return releaseFormat;}

    public void setReleaseFormat(List<String> releaseFormat){
        notEmpty(releaseFormat);
        for (String releaseFormats : releaseFormat) {
            if (releaseFormats == null)
                throw new NullPointerException("Release format list cannot contain null elements");
            if (releaseFormats.trim().equals(""))
                throw new IllegalArgumentException("Release format list cannot contain blank values");
        }
        this.releaseFormat = releaseFormat;
    }

    //getSales method
    public int getSales(){ return sales;
    }

    public void setSales(int sales) {
        if (sales >= 0)
            this.sales = sales;
        else
            throw new IllegalArgumentException("Sales cannot be negative");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return releaseYear == album.releaseYear &&
                recordNumber.equals(album.recordNumber) &&
                albumName.equals(album.albumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(releaseYear, recordNumber, albumName);
    }
}
