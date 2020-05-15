package allaboutecm.model;

import allaboutecm.dataaccess.neo4j.URLConverter;
import com.google.common.collect.Sets;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;
import static org.apache.commons.lang3.Validate.notNull;

/**
 * An artist that has been featured in (at least) one ECM record.
 *
 * See {@https://www.ecmrecords.com/artists/1435045745}
 */
@NodeEntity
public class Musician extends Entity {
    @Property(name="name")
    private String name;

    @Convert(URLConverter.class)
    @Property(name="musicianURL")
    private URL musicianUrl;

    @Relationship(type="albums")
    private Set<Album> albums;

    @Property(name="biography")
    private String biography;

    public Musician() {
    }

    public Musician(String name) {
        notNull(name);
        notBlank(name);
        this.name = name;
        this.musicianUrl = null;
        albums = Sets.newLinkedHashSet();
        biography = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        notNull(name,"Musician name cannot be null");
        notBlank(name,"Musician name cannot be empty or blank");
        this.name = name.trim();
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        notNull(albums,"Set of albums cannot be null");
        for (Album album : albums) {
            if (album == null)
                throw new NullPointerException("Set of albums cannot contain null element");

        }
        this.albums = albums;
    }

    public URL getMusicianUrl() {
        return musicianUrl;
    }

    public void setMusicianUrl(URL musicianUrl) throws IOException {
        notNull(musicianUrl);
        HttpURLConnection connection = (HttpURLConnection) musicianUrl.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();
        if (responseCode == 200)
            this.musicianUrl = musicianUrl;
        else
            throw new IllegalArgumentException("Url is not valid");
    }

    public String getBiography(){ return biography;}

    public void setBiography(String biography){
        notNull(biography,"Cannot set null biography");
        notBlank(biography,"Cannot set empty or blank biography");
        this.biography = biography;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician that = (Musician) o;
        return Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
