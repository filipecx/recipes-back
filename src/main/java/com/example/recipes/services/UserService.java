package com.example.recipes.services;

import com.example.recipes.domain.user.User;
import com.example.recipes.dto.user.UserRegisterRequestDTO;
import com.example.recipes.dto.user.UserResponseDTO;
import com.example.recipes.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    public List<UserResponseDTO> getAllUsers(){
        return this.userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getId(), user.getName(), user.getEmail(), user.getUsername()))
                .collect(Collectors.toList());
    }

    public UserResponseDTO getUserById(UUID userId){
        User fetchedUser = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return new UserResponseDTO(
                fetchedUser.getId(),
                fetchedUser.getName(),
                fetchedUser.getEmail(),
                fetchedUser.getUsername());
    }

    public User getUserByUsername(String username){
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }

    public void registerUser(UserRegisterRequestDTO newUserData){
        try {
            this.getUserByUsername(newUserData.username());
        } catch(RuntimeException error) {
            String encryptedPassword = new BCryptPasswordEncoder().encode(newUserData.password());

            User newUser = new User();

            newUser.setEmail(newUserData.email());
            newUser.setName(newUserData.name());
            newUser.setUsername(newUserData.username());
            newUser.setPassword(encryptedPassword);

            this.userRepository.save(newUser);
        }
    }

    public UserResponseDTO deleteUser(UUID userId){
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        UserResponseDTO userResponseDTO = new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUsername()
        );
        this.userRepository.delete(user);

        return userResponseDTO;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));
    }
/*
    public UserResponseDTO authenticateUser(String username, String password){

    }

*/
}
