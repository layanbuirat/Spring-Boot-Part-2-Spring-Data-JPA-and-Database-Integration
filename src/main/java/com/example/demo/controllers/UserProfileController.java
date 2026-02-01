package com.example.demo.controllers;
import com.example.demo.models.UserProfile;
import com.example.demo.services.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping("/users/{userId}/profile")
    public UserProfile createProfile(@PathVariable Integer userId, @RequestBody UserProfile profile) {
        return userProfileService.createProfileForUser(userId, profile);
    }

    @GetMapping("/profiles/{userId}")
    public Optional<UserProfile> getProfile(@PathVariable Integer userId) {
        return userProfileService.getProfileByUserId(userId);
    }

    @PutMapping("/profiles/{userId}")
    public UserProfile updateProfile(@PathVariable Integer userId, @RequestBody UserProfile profile) {
        Optional<UserProfile> existingProfile = userProfileService.getProfileByUserId(userId);
        if (existingProfile.isPresent()) {
            UserProfile updatedProfile = existingProfile.get();
            updatedProfile.setFirstName(profile.getFirstName());
            updatedProfile.setLastName(profile.getLastName());
            updatedProfile.setPhoneNumber(profile.getPhoneNumber());
            return userProfileService.createProfileForUser(userId, updatedProfile);
        }
        return null;
    }
}