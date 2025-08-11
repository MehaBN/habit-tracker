package com.habittracker.controller;

import com.habittracker.model.Habit;
import com.habittracker.model.User;
import com.habittracker.repository.HabitRepository;
import com.habittracker.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/habits")
public class HabitController {

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;  // ✅ Added

    public HabitController(HabitRepository habitRepository, UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.userRepository = userRepository; // ✅ Added
    }

    @GetMapping
    public String listHabits(Model model, Principal principal) {
        String username = principal.getName(); // logged-in username
        User user = userRepository.findByUsername(username).orElseThrow();
        model.addAttribute("habits", user.getHabits()); // show only this user's habits
        model.addAttribute("habit", new Habit()); // for form binding
        model.addAttribute("username", username); 
        return "habits";
    }

    @PostMapping
    public String addHabit(@ModelAttribute Habit habit, Principal principal) {
        String username = principal.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        habit.setUser(user); // assign habit to this user
        habitRepository.save(habit);
        return "redirect:/habits";
    }

    @PostMapping("/{id}/toggle")
    public String toggleHabit(@PathVariable Long id, Principal principal) {
        Habit habit = habitRepository.findById(id).orElseThrow();

        // ✅ Ensure logged-in user can only toggle their own habits
        String username = principal.getName();
        if (!habit.getUser().getUsername().equals(username)) {
            return "redirect:/habits"; // deny if not owner
        }

        habit.setCompleted(!habit.isCompleted());
        habitRepository.save(habit);
        return "redirect:/habits";
    }

    @PostMapping("/{id}/delete")
    public String deleteHabit(@PathVariable Long id, Principal principal) {
        Habit habit = habitRepository.findById(id).orElseThrow();

        // ✅ Ensure logged-in user can only delete their own habits
        String username = principal.getName();
        if (!habit.getUser().getUsername().equals(username)) {
            return "redirect:/habits";
        }

        habitRepository.delete(habit);
        return "redirect:/habits";
    }
}
