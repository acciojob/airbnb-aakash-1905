package com.driver.services;

import com.driver.model.Booking;
import com.driver.model.Facility;
import com.driver.model.Hotel;
import com.driver.model.User;
import com.driver.repository.HotelManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class HotelManagementService {

    HotelManagementRepository hotelManagementRepository = new HotelManagementRepository();
    public boolean addHotel(Hotel hotel) {
        if(hotel==null || hotel.getHotelName()==null){
            return false;
        }
        return hotelManagementRepository.addHotel(hotel);
    }

    public void addUser(User user) {
        hotelManagementRepository.addUser(user);
    }

    public String getHotelWithMostFacilities() {
        int facilities= 0;

        String hotelName = "";

        List<Hotel> hotels = hotelManagementRepository.getAllHotels();
        for(Hotel hotel:hotels){

            if(hotel.getFacilities().size()>facilities){
                facilities = hotel.getFacilities().size();
                hotelName = hotel.getHotelName();
            }
            else if(hotel.getFacilities().size()==facilities){
                if(hotel.getHotelName().compareTo(hotelName)<0){
                    hotelName = hotel.getHotelName();
                }
            }
        }
        return hotelName;
    }

    public int bookARoom(Booking booking) {
        String key = UUID.randomUUID().toString();

        booking.setBookingId(key);

        String hotelName = booking.getHotelName();
        Hotel hotel = hotelManagementRepository.getHotelByName(hotelName);

        int availableRooms = hotel.getAvailableRooms();

        if(availableRooms<booking.getNoOfRooms()){
            return -1;
        }

        int amountToBePaid = hotel.getPricePerNight()*booking.getNoOfRooms();
        booking.setAmountToBePaid(amountToBePaid);

        //Make sure we check this part of code as well
        hotel.setAvailableRooms(hotel.getAvailableRooms()-booking.getNoOfRooms());

        //saving data to databases
        hotelManagementRepository.bookARoom(booking,hotel);

        return  amountToBePaid;
    }

    public int getBookings(Integer aadharCard) {
        return  hotelManagementRepository.getBookings(aadharCard);
    }

    public Hotel updateFacilities(List<Facility> newFacilities, String hotelName) {
        Hotel hotel = hotelManagementRepository.getHotelByName(hotelName);

        List<Facility> oldFacilities = hotel.getFacilities();

        for(Facility facility: newFacilities){

            if(oldFacilities.contains(facility)){
                continue;
            }else{
                oldFacilities.add(facility);
            }
        }

        hotel.setFacilities(oldFacilities);

        hotelManagementRepository.updateFacilities(hotelName);

        return hotel;
    }
}
