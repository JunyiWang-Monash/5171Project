package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
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
     * Most Talented Musicians backup
     */
    public List<Musician> mostTalentedMusician1(int k){
        Collection<MusicianInstrument> musicianInstruments = dao.loadAll(MusicianInstrument.class);
        ListMultimap<Integer, Musician> musicianInstrumentMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for(MusicianInstrument musicianInstrument:musicianInstruments){
            Musician musician = musicianInstrument.getMusician();
            int instrument = musicianInstrument.getMusicalInstruments().size();
            musicianInstrumentMap.put(instrument,musician);
        }
        List<Musician> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(musicianInstrumentMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys1) {
            List<Musician> musicians = musicianInstrumentMap.get(count);
            for(Musician musician:musicians)
            {
                if (result1.size() >= k)
                {
                    break;
                }
                else
                    result1.add(musician);
            }
        }
        return result1;
    }

    /**
     * Musicians that collaborate the most widely, by the number of other musicians they work with on albums.
     *
     * @Param k the number of musicians to be returned.
     */

    public List<Musician> mostSocialMusicians(int k) {
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        ListMultimap<Integer, Musician> nameMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for (Musician m : musicians) {
            Set<Musician> cooperateMusicians = Sets.newHashSet();
            Set<Album> musicianAlbums = m.getAlbums();
            for (Album album : musicianAlbums) {
                List<Musician> albumMusicians = album.getFeaturedMusicians();
                for (Musician musician : albumMusicians) {
                    if (musician.getName() != m.getName()) {
                        if (cooperateMusicians.size() == 0) {
                            cooperateMusicians.add(musician);
                        } else if (!cooperateMusicians.contains(musician)) {
                            cooperateMusicians.add(musician);
                        }
                    }
                }
            }
            int count = cooperateMusicians.size();
            nameMap.put(count, m);

        }
        List<Musician> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(nameMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys1) {
            List<Musician> musicians1 = nameMap.get(count);
            for (Musician musician1 : musicians1) {
                if (result1.size() >= k) {
                    break;
                } else
                    result1.add(musician1);
            }
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
        ListMultimap<Integer, Integer> releaseMap = MultimapBuilder.treeKeys().arrayListValues().build();
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
            List<Integer> releaseYears = releaseMap.get(count);
            for(Integer release:releaseYears)
            {
                if (result1.size() >= k)
                {
                    break;
                }
                else
                    result1.add(release);
            }
        }
        return result1;
    }

    //Added Code
    /**
     * Best selling albums.
     * Extra Credit Part
     * @Param k the number of albums to be returned.
     */

    public List<Album> bestSellingAlbums(int k) {
        Collection<Album> albums = dao.loadAll(Album.class);
        ListMultimap<Integer, Album> albumMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for (Album album: albums){
            int sales = 0;
            sales = album.getSales();
            albumMap.put(sales,album);
        }
        List<Album> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(albumMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer rating : sortedKeys1) {
            List<Album> albums1 = albumMap.get(rating);
            for(Album album:albums1)
            {
                if (result1.size() >= k)
                {
                    break;
                }
                else
                    result1.add(album);
            }
        }
        return result1;
    }

    //Added Code//to be changed
    /**
     * Highest rated albums.
     * Extra Credit Part
     * @Param k the number of albums to be returned.
     */

    public List<Album> highestRatedAlbums(int k) {
        Collection<Album> albums = dao.loadAll(Album.class);
        ListMultimap<Integer, Album> albumMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for (Album album: albums){
            Set<Review> reviews = album.getReviews();
            int rating = 0;
            int count = 0;
            int averageRating;
            for(Review review:reviews)
            {
                rating = rating + review.getRating();
                count=count+1;
            }
            if(count == 0)
                count=1;
            averageRating = rating/count;
            albumMap.put(averageRating,album);
        }
        List<Album> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(albumMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer rating : sortedKeys1) {
            List<Album> albums1 = albumMap.get(rating);
            for(Album album:albums1)
            {
                if (result1.size() >= k)
                {
                    break;
                }
                else
                    result1.add(album);
            }
        }
        return result1;
    }

    //Correct code
    /**
     * Most similar albums to a give album. The similarity can be defined in a variety of ways.
     * For example, it can be defined over the musicians in albums, the similarity between names
     * of the albums & tracks, etc.
     *
     * @Param k the number of albums to be returned.
     * @Param album
     */
    public List<Album> mostSimilarAlbums(int k,Album album1) {
        Collection<Album> albums = dao.loadAll(Album.class);
        ListMultimap<Integer, Album> similarMap = MultimapBuilder.treeKeys().arrayListValues().build();
        albums.remove(album1);
        List<Musician> musicians1 = album1.getFeaturedMusicians();
        for (Album album: albums) {
            List<Musician> musicians = album.getFeaturedMusicians();
            int count = 0;
            for(Musician musician:musicians1){
                for (Musician albumMusician:musicians){
                    if (musician.getName()==albumMusician.getName())
                    {
                        count = count + 1;
                    }
                }
            }
            similarMap.put(count,album);
        }
        List<Album> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(similarMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer similar : sortedKeys1) {
            List<Album> albums12 = similarMap.get(similar);
            for(Album albumz:albums12){
                if(result1.size()>=k)
                {
                    break;
                }
                else
                    result1.add(albumz);
            }
        }
        return result1;
    }

}
