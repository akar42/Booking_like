package com.example.hotelApp.room;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelApp.room.model.Room;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
