package com.example.demo.services;
import com.example.demo.models.User;
import com.example.demo.models.UserProfile;
import com.example.demo.repositories.UserProfileRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service

public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserRepository userRepository;

    public UserProfile createProfileForUser(Integer userId, UserProfile profile) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            profile.setUser(user.get());
            return userProfileRepository.save(profile);
        }
        return null;
    }

    public Optional<UserProfile> getProfileByUserId(Integer userId) {
        return userProfileRepository.findByUserId(userId);
    }
}