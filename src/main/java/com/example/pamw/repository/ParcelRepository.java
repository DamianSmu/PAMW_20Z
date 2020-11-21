package com.example.pamw.repository;

import com.example.pamw.entity.Parcel;
import com.example.pamw.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParcelRepository extends CrudRepository<Parcel, String> {
}
