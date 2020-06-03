package allaboutecm.mining;

import allaboutecm.dataaccess.DAO;
import allaboutecm.model.*;
import com.google.common.collect.*;

import java.util.*;

public class ECMMiner {

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
        ListMultimap<Integer, Musician> musicianMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for(Musician musician:musicians){
            Set<Album> albums1 = musician.getAlbums();
            int count = 0;
            for(Album album:albums1) {
                if (album.getReleaseYear() >= startYear && album.getReleaseYear() <= endYear)
                    count = count + 1;
            }
            musicianMap.put(count,musician);
        }
        List<Musician> result = Lists.newArrayList();
        List<Integer> sortedKeys = Lists.newArrayList(musicianMap.keySet());
        sortedKeys.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys) {
            List<Musician> list = musicianMap.get(count);
            for (Musician m:list){
                if (result.size()>=k)
                    break;
                else
                    result.add(m);
            }
        }
        return result;
    }

    /**
     * Most talented musicians by the number of different musical instruments they play
     *
     * @Param k the number of musicians to be returned.
     */
    public List<Musician> mostTalentedMusicians(int k){
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
     * Most social musicians to reduce code smell
     */
    public List<Musician> mostSocialMusicians(int k){
        Collection<Musician> musicians = dao.loadAll(Musician.class);
        ListMultimap<Integer, Musician> nameMap = MultimapBuilder.treeKeys().arrayListValues().build();
        for(Musician musician:musicians){
            Set<Album> albums = musician.getAlbums();
            int collaborateNumber = 0;
            int value = 0;
            for(Album album:albums)
            {
                List<Musician> musicians1 = album.getFeaturedMusicians();
                for(Musician musician1:musicians1){
                    if (musician1.equals(musician))
                        collaborateNumber = collaborateNumber + 1;
                    if(collaborateNumber > value)
                        value = collaborateNumber;
                }
            }
            nameMap.put(value,musician);
        }

        List<Musician> result1 = socialComplexity(nameMap,k);

        /*For Shivani
        List<Integer> sortedKeys1 = Lists.newArrayList(nameMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys1) {
            List<Musician> musicians2 = nameMap.get(count);
            for (Musician musician2 : musicians2) {
                if (result1.size() >= k) {
                    break;
                } else
                    result1.add(musician2);
            }
         */
        return result1;
    }

    public List<Musician> socialComplexity(ListMultimap<Integer, Musician> nameMap,int k){
        List<Musician> result1 = Lists.newArrayList();
        List<Integer> sortedKeys1 = Lists.newArrayList(nameMap.keySet());
        sortedKeys1.sort(Ordering.natural().reverse());
        for (Integer count : sortedKeys1) {
            List<Musician> musicians2 = nameMap.get(count);
            for (Musician musician2 : musicians2) {
                if (result1.size() >= k) {
                    break;
                } else
                    result1.add(musician2);
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
        List<Album> result1 = avoidDuplicate(albumMap,k);

        /*For Shivani
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
        }*/
        return result1;
    }

    public List<Album> avoidDuplicate(ListMultimap<Integer, Album> albumMap, int k)
    {
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
        List<Album> result1 = avoidDuplicate(albumMap,k);
        /*For Shivani
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
        }*/
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
        List<Album> result1 = similarCognitive(similarMap,k);
        /*
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
        }*/
        return result1;
    }

    public List<Album> similarCognitive(ListMultimap<Integer, Album> similarMap,int k){
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
