package com.uit.se104.feedback_system.dto.auth;

import com.uit.se104.feedback_system.dto.user.UserResponse;

public record AuthResponse(
    String accessToken,
    String tokenType,
    UserResponse user
) {}

// example:
// {
//   "accessToken": "eyJhbGciOiJIUzI1...",
//   "tokenType": "Bearer",
//   "user": {
//       ...
//   }
// }