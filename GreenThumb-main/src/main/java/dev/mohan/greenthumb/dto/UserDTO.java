package dev.mohan.greenthumb.dto;

import java.util.Set;

public record UserDTO(
        Long id,
        String name,
        String email,
        Set<String> roles      // flattened role names, e.g. ["ROLE_USER"]
) {}