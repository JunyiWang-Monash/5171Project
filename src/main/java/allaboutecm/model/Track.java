package allaboutecm.model;
import jdk.nashorn.internal.objects.annotations.Property;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.*;
import static org.apache.commons.lang3.Validate.notBlank;


public class Track extends Entity {

    @Property(name = "trackName")
    private String trackName;

    @Property(name = "length")
    private String length;

    @Property(name="trackNumber")
    private int trackNumber;


    public Track(String trackName, String length, int trackNumber) {
        notNull(length,"Track length cannot be null");
        notBlank(length,"Track length cannot be empty or blank");

        notNull(trackName,"Track name cannot be null");
        notBlank(trackName,"Track name cannot be empty or blank");

        if(trackNumber <= 0)
            throw new IllegalArgumentException("Track number cannot be zero or negative");

        this.trackName = trackName;
        this.length = length;
        this.trackNumber = trackNumber;

    }

    public String getTrackName(){
        return trackName;
    }

    public void setTrackName(String trackName){
        notNull(trackName,"Track name cannot be null");
        notBlank(trackName,"Track name cannot be empty or blank");
        this.trackName = trackName.trim();
    }

    public String getLength(){return length;}

    public void setLength(String length) {
        notNull(length,"length cannot be null");
        notBlank(length,"Length cannot be empty or blank");
        this.length = length;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(int trackNumber) {
        if(trackNumber <= 0)
        {
            throw new IllegalArgumentException("Track number cannot be zero or negative");
        }
        this.trackNumber = trackNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return trackName.equals(track.trackName) &&
                length.equals(track.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackName,length);
    }




}
