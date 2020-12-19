package com.example.pamw.repository;

import com.example.pamw.entity.Parcel;
import com.example.pamw.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;



public interface ParcelRepository extends JpaRepository<Parcel, String> {
    List<Parcel> findAllBySender(User sender);
}
