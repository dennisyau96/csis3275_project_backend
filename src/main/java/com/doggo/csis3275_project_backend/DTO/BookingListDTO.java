package com.doggo.csis3275_project_backend.DTO;

import com.doggo.csis3275_project_backend.Entities.Dog;
import com.doggo.csis3275_project_backend.Entities.Timeslot;

public class BookingListDTO {
    private String _id;
    private String booking_date;
    private boolean booking_confirmed;
    private Dog dog;
    private Timeslot timeslot;
    private Double price;
    private boolean booking_completed;

    public boolean isBooking_completed() {
        return booking_completed;
    }

    public void setBooking_completed(boolean booking_completed) {
        this.booking_completed = booking_completed;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public boolean isBooking_confirmed() {
        return booking_confirmed;
    }

    public void setBooking_confirmed(boolean booking_confirmed) {
        this.booking_confirmed = booking_confirmed;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Timeslot getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Timeslot timeslot) {
        this.timeslot = timeslot;
    }
}
