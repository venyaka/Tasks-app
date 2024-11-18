package veniamin.tasksapp.backend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import veniamin.tasksapp.backend.dto.request.RegisterReqDTO;
import veniamin.tasksapp.backend.dto.request.UserAuthorizeReqDTO;
import veniamin.tasksapp.backend.dto.responce.TokenRespDTO;

public interface AuthorizeService {

    ResponseEntity<TokenRespDTO> authorizeUser(UserAuthorizeReqDTO userAuthorizeDTO);

    void registerUser(@Valid RegisterReqDTO registerDTO, HttpServletRequest request);

    void sendVerificationCode(String email, HttpServletRequest request);

    void verificateUser(String email, String verificationToken);
}
