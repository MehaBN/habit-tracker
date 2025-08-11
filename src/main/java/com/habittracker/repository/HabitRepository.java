package com.habittracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.habittracker.model.Habit;
import com.habittracker.model.User;
import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {

    // Custom query: find all habits for a given user
    List<Habit> findByUser(User user);
}