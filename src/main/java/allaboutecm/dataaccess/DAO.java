package allaboutecm.dataaccess;

import allaboutecm.model.*;

import java.util.Collection;

public interface DAO {
    <T extends Entity> T load(Class<T> clazz, Long id);

    <T extends Entity> T createOrUpdate(T entity);

    <T extends Entity> Collection<T> loadAll(Class<T> clazz);

    <T extends Entity> void delete(T entity);

    Musician findMusicianByName(String name);

    Album findAlbumByName(String name);

    MusicalInstrument findMusicalInstrumentByName(String name);

    Review findReviewByContent(String content);

    Track findTrackByName(String name);

    Concert findConcertByName(String concertName);
}
