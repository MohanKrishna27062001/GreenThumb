package dev.mohan.greenthumb.service;

import dev.mohan.greenthumb.dto.AuthResponseDTO;
import dev.mohan.greenthumb.dto.LoginRequestDTO;
import dev.mohan.greenthumb.dto.RegisterRequestDTO;
import dev.mohan.greenthumb.dto.UserDTO;

public interface AuthService {
    UserDTO register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
}