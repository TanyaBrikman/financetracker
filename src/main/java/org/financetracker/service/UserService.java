package org.financetracker.service;

import lombok.RequiredArgsConstructor;
import org.financetracker.dto.request.UserRequestDto;
import org.financetracker.dto.response.UserResponseDto;
import org.financetracker.entity.User;
import org.financetracker.exception.ResourceNotFoundException;
import org.financetracker.mapper.UserMapper;
import org.financetracker.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        User savedUser = userRepository.save(userMapper.toEntity(userRequestDto));
        return userMapper.toResponseDto(savedUser);
    }

    public Page<UserResponseDto> getAllUsers(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10);
        }
        return userRepository
                .findAll(pageable)
                .map(userMapper::toResponseDto);
    }

    public UserResponseDto getUserById(Long id) {
        return userRepository
                .findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}