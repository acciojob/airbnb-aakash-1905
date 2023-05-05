package com.driver.repository;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import org.springframework.stereotype.Repository;

import java.util.*;


public class HotelManagementRepository {
    private HashMap<String,Hotel> hotelDb = new HashMap<>();
    private HashMap<Integer, User> userDb = new HashMap<>();

    private HashMap<String, Booking> bookingDb = new HashMap<>();

    private HashMap<Integer,Integer> countOfBookings = new HashMap<>();
    public boolean addHotel(Hotel hotel) {
        if(hotelDb.containsKey(hotel.getHotelName())){
            return false;
        }
        hotelDb.put(hotel.getHotelName(),hotel);
        return true;
    }

    public void addUser(User user) {
        userDb.put(user.getaadharCardNo(),user);
    }


    public void bookARoom(Booking booking,Hotel hotel) {

        bookingDb.put(booking.getBookingId(),booking);

        hotelDb.put(hotel.getHotelName(),hotel);

        int aadharCard = booking.getBookingAadharCard();
        Integer currentBookings = countOfBookings.get(aadharCard);
        countOfBookings.put(aadharCard, Objects.nonNull(currentBookings)?1+currentBookings:1);
    }

    public int getBookings(Integer aadharCard) {
        return countOfBookings.get(aadharCard);
    }

    public void updateFacilities(String hotelName) {
        Hotel hotel = hotelDb.get(hotelName);
        hotelDb.put(hotelName,hotel);
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> result = new ArrayList(hotelDb.values());
        return result;
    }

    public Hotel getHotelByName(String hotelName) {
        return hotelDb.get(hotelName);
    }
}
