package com.example.taxi.repositories;

import com.example.taxi.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findAll();

    List<Trip> findByArrCity(String arrCity);

    List<Trip> findByDepCity(String depCity);

    List<Trip> findByDepCityAndArrCity(String depCity, String arrCity);

    List<Trip> findByAutoClass(String autoClass);

    List<Trip> findByArrCityAndAutoClass(String arrCity, String autoClass);

    List<Trip> findByDepCityAndAutoClass(String depCity, String autoClass);

    List<Trip> findByDepCityAndArrCityAndAutoClass(String depCity, String arrCity, String autoClass);


}




