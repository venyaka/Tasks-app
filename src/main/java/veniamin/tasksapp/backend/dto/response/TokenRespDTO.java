package veniamin.tasksapp.backend.dto.response;

import lombok.Data;

@Data
public class TokenRespDTO {

    private String accessToken;

    private String refreshToken;

}
