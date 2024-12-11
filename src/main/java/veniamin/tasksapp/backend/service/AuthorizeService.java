package veniamin.tasksapp.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import veniamin.tasksapp.backend.dto.request.RegisterReqDTO;
import veniamin.tasksapp.backend.dto.request.UserAuthorizeReqDTO;
import veniamin.tasksapp.backend.dto.response.TokenRespDTO;

public interface AuthorizeService {

    ResponseEntity<TokenRespDTO> authorizeUser(UserAuthorizeReqDTO userAuthorizeDTO);

    void registerUser(@Valid RegisterReqDTO registerDTO, HttpServletRequest request);

    void sendVerificationCode(String email, HttpServletRequest request);

    void verificateUser(String email, String verificationToken);
}
