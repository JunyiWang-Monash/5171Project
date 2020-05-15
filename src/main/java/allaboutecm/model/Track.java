package allaboutecm.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.io.IOException;


import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.*;
import static org.apache.commons.lang3.Validate.notBlank;


public class Track extends Entity {

    private String trackName;
    private String length;




    public Track(String trackName, String length) {
        notNull(length,"Track length cannot be null");
        notBlank(length,"Track length cannot be empty or blank");

        notNull(trackName,"Track name cannot be null");
        notBlank(trackName,"Track name cannot be empty or blank");

        this.trackName = trackName;
        this.length = length;

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return trackName == track.trackName &&
                length.equals(track.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackName,length);
    }




}
