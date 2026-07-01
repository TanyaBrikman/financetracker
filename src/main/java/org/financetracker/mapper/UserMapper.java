package org.financetracker.mapper;

import lombok.Data;
import org.financetracker.dto.request.UserRequestDto;
import org.financetracker.dto.response.UserResponseDto;
import org.financetracker.entity.User;
import org.springframework.stereotype.Component;

@Data
@Component
public class UserMapper {

    public User toEntity(UserRequestDto userRequestDto) {
        if(userRequestDto == null) {
            throw new IllegalArgumentException("Response cannot be null");
        }

        return User.builder()
                .userName(userRequestDto.getUserName())
                .age(userRequestDto.getAge())
                .email(userRequestDto.getEmail())
                .build();
    }

    public UserResponseDto toResponseDto(User user) {
        if(user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .age(user.getAge())
                .email(user.getEmail())
                .build();
    }
}