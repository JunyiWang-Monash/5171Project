package allaboutecm.model;

import jdk.nashorn.internal.objects.annotations.Property;
import org.neo4j.ogm.annotation.Relationship;

import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.Validate.notEmpty;
import static org.apache.commons.lang3.Validate.notNull;

public class Concert extends Entity {
    @Property(name = "date")
    private String date;

    @Property(name = "concertName")
    private String concertName;

    @Relationship(type = "has")
    private List<Musician> musicians;

    @Property(name = "city")
    private String city;

    @Property(name = "country")
    private String country;

    public Concert (String date,List<Musician> musicians,String concertName,String city,String country){
        notNull(city,"City cannot be Null");
        notEmpty(city,"City cannot be empty");
        notNull(date,"date cannot be Null");
        notEmpty(date,"date cannot be empty");
        notNull(country,"Country cannot be Null");
        notEmpty(country,"Country cannot be empty");
        notNull(concertName,"concert name cannot be null");
        notEmpty(concertName,"concert name cannot be empty or blank");
        this.date = date;
        this.musicians = musicians;
        this.concertName = concertName;
        this.city = city;
        this.country = country;

    }

    public String getDate() { return date; }

    public void setDate(String date) {
        notNull(date,"date cannot be null");
        notEmpty(date,"date cannot be empty or blank");
        this.date = date; }

    public List<Musician> getMusiciansList() { return musicians; }

    public void setMusiciansList(List<Musician> musicians) {
        notNull(musicians,"musicians list cannot be null");
        this.musicians = musicians; }

    public String getCity() { return city; }

    public void setCity(String city) {
        notNull(city,"city cannot be null");
        notEmpty(city,"city cannot be empty or blank");
        this.city = city; }

    public String getCountry() { return country; }

    public void setCountry(String country) {
        notNull(country,"country cannot be null");
        notEmpty(country,"country cannot be empty or blank");
        this.country = country;
    }

    public String getConcertName() { return concertName; }

    public void setConcertName(String concertName) {
        notNull(concertName,"concert name cannot be null");
        notEmpty(concertName,"concert name cannot be empty or blank");
        this.concertName = concertName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Concert concert = (Concert) o;
        return date.equals(concert.date) && musicians.equals(concert.musicians) && city.equals(concert.city)
                && country.equals(concert.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date,musicians,city,country);
    }
}

