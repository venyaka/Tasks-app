package veniamin.tasksapp.backend.dto.responce;

import lombok.Data;

@Data
public class TokenRespDTO {

    private String accessToken;

    private String refreshToken;

}
