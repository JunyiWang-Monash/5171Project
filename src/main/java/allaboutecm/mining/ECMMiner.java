package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.Album;
import allaboutecm.model.MusicalInstrument;
import allaboutecm.model.Musician;
import allaboutecm.model.MusicianInstrument;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TODO: implement and test the methods in this class.
 * Note that you can extend the Neo4jDAO class to make implementing this class easier.
 */
public class ECMMiner {
    private static Logger logger = LoggerFactory.getLogger(ECMMiner.class);

    private final DAO dao;

    public ECMMiner(DAO dao) {
        this.dao = dao;
    }

    /**
     * Returns the most prolific musician in terms of number of albums released.
     *
     * @Param k the number of musicians to be returned.
     * @Param startYear, endYear between the two years [startYear, endYear].
     * When startYear/endYear is negative, that means startYear/endYear is ignored.
     */
    public List<Musician> mostProlificMusicians(int k, int startYear, int endYear) {
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        Map<String, Musician> nameMap = Maps.newHashMap();
        for (Musician m : musicians) {
            nameMap.put(m.getName(), m);
        }

        ListMultimap<String, Album> multimap = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap = MultimapBuilder.treeKeys().arrayListValues().build();

        for (Musician musician : musicians) {
            Set<Album> albums = musician.getAlbums();
            for (Album album : albums) {
                boolean toInclude =
                        !((startYear > 0 && album.getReleaseYear() < startYear) ||
                                (endYear > 0 && album.getReleaseYear() > endYear));

                if (toInclude) {
                    multimap.put(musician.getName(), album);
                }
            }
        }

        Map<String, Collection<Album>> albumMultimap = multimap.asMap();
        for (String name : albumMultimap.keySet()) {
            Collection<Album> albums = albumMultimap.get(name);
            int size = albums.size();
            countMap.put(size, nameMap.get(name));
        }

        List<Musician> result = Lists.newArrayList();
        List<Integer> sortedKeys = Lists.newArrayList(countMap.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
            List<Musician> list = countMap.get(count);
            if (list.size() >= k) {
                break;
            }
            if (result.size() + list.size() >= k) {
                int newAddition = k - result.size();
                for (int i = 0; i < newAddition; i++) {
                    result.add(list.get(i));
                }
            } else {
                result.addAll(list);
            }
        }

        return result;
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k) {
        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        Map<String, Musician> nameMap1 = Maps.newHashMap();
        for (MusicianInstrument m : musicianInstruments) {
            nameMap1.put(m.getMusician().getName(), m.getMusician());
        }

        ListMultimap<String, MusicalInstrument> multimap1 = MultimapBuilder.treeKeys().arrayListValues().build();
        ListMultimap<Integer, Musician> countMap1 = MultimapBuilder.treeKeys().arrayListValues().build();

        for (MusicianInstrument musicianInstrument : musicianInstruments) {
            Set<MusicalInstrument> musicalInstrument = musicianInstrument.getMusicalInstruments();
            for (MusicalInstrument musicalInstruments: musicalInstrument) {
                multimap1.put(musicianInstrument.getMusician().getName(),musicalInstruments);
            }
        }



    Map<String, Collection<MusicalInstrument>> musicialInstrumentMultimap = multimap1.asMap();
        for (String name : musicialInstrumentMultimap.keySet()) {
        Collection<MusicalInstrument> musicalInstrument = musicialInstrumentMultimap.get(name);
        int size = musicalInstrument.size();
        countMap1.put(size, nameMap1.get(name));
    }

    List<Musician> result = Lists.newArrayList();
    List<Integer> sortedKeys = Lists.newArrayList(countMap1.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
        List<Musician> list = countMap1.get(count);
        if (list.size() >= k) {
            break;
        }
        if (result.size() + list.size() >= k) {
            int newAddition = k - result.size();
            for (int i = 0; i < newAddition; i++) {
                result.add(list.get(i));
            }
        } else {
            result.addAll(list);
        }
    }
        return result;

}

    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */

    public List<Musician> mostSocialMusicians(int k) {
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        Map<Integer, Musician> nameMap = Maps.newHashMap();
        Set<Album> musicianAlbums;
        Set<Musician> cooperateMusicians = Sets.newHashSet();
        for (Musician m : musicians) {
            musicianAlbums = m.getAlbums();
            for (Album album : musicianAlbums) {
                for (Musician musician : album.getFeaturedMusicians()) {
                    if (musician.getName() != m.getName()) {
                        if (cooperateMusicians.size() == 0) {
                            cooperateMusicians.add(musician);
                        }
                        else if (!cooperateMusicians.contains(musician)) {
                            cooperateMusicians.add(musician);
                        }
                    }
                }
            }
            int count = cooperateMusicians.size();
            nameMap.put(count,m);
        }
        List<Musician> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(nameMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys1) {
            Musician musician = nameMap.get(count);
            if(result1.size()>=k)
            {
                break;
            }
            else
                result1.add(musician);
        }
        return result1;
    }

    /**
     * Busiest year in terms of number of albums released.
     *
     * @Param k the number of years to be returned.
     */

    public List<Integer> busiestYear(int k) {
        Collection<Album> albums = dao.loadAll(Album.class);
        Map<Integer, Integer> releaseMap = Maps.newHashMap();
        Set<Integer> releaseYear = Sets.newHashSet();
        for (Album album: albums){
            releaseYear.add(album.getReleaseYear());
        }
        for (int year : releaseYear ){
            int count=0;
            for (Album album1: albums) {
                if(album1.getReleaseYear()==year)
                    count=count+1;
            }
            releaseMap.put(count,year);
        }
        List<Integer> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(releaseMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys1) {
            Integer year = releaseMap.get(count);
            if(result1.size()>=k)
            {
                break;
            }
            else
                result1.add(year);
        }
        return result1;
    }

    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */
    // simply can be defined over the musicians / musicalInstruments that are used in the album;
    // or in a more sophiscated way, can be defined over the name of the tracks, the description of the album, and so on.
    // 自由发挥 最好是选一个somewhat more sophiscated business logic and then test

    public List<String> mostSimilarAlbums(int k, Album album) {
        Collection<Album> albums = dao.loadAll(Album.class);
        List<String> result1 = Lists.newArrayList();
        for (Album album1: albums){
            List<String> nameList = Lists.newArrayList();
            List<String> nameList2 = Lists.newArrayList();
            List<Musician> musicians = album1.getFeaturedMusicians();
            List<Musician> musician1 = album.getFeaturedMusicians();
            for(Musician musician2 : musicians){
                nameList.add(musician2.getName());
            }
            for(Musician musician3 : musician1){
                nameList2.add(musician3.getName());
            }
                if(result1.size()>=k){
                    break;
                }
                else if(nameList.contains(nameList2)) {
                      result1.add(album1.getAlbumName());
                }
        }
        return result1;
        }


}
