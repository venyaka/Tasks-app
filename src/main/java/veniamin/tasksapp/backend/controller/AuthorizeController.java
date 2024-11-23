package veniamin.tasksapp.backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import veniamin.tasksapp.backend.constant.PathConstants;
import veniamin.tasksapp.backend.dto.request.RegisterReqDTO;
import veniamin.tasksapp.backend.dto.request.UserAuthorizeReqDTO;
import veniamin.tasksapp.backend.dto.responce.TokenRespDTO;
import veniamin.tasksapp.backend.service.AuthorizeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import veniamin.tasksapp.backend.utils.LogsUtils;

@RestController
@RequiredArgsConstructor
@RequestMapping(PathConstants.AUTHORIZE_CONTROLLER_PATH)
public class AuthorizeController {

    private final AuthorizeService authorizeService;


    @PostMapping("/login")
    @Operation(summary = "Ендпоинт для авторизации, принимает два request body с  - email и  password, " +
            "в которых хранится email и пароль соответственно, " +
            "возвращает response с header'ом Authorization в формате Bearer jwtTokenInStringFormat и refreshToken в том же формате")
    public ResponseEntity<TokenRespDTO> authorizeUser(@Valid @RequestBody UserAuthorizeReqDTO userAuthorizeDTO) {
        return authorizeService.authorizeUser(userAuthorizeDTO);
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody RegisterReqDTO registerDTO, HttpServletRequest request) {
        authorizeService.registerUser(registerDTO, request);
    }

    @PostMapping("/verificateCode")
    public void sendVerificationCode(@RequestParam String email, HttpServletRequest request) {
        authorizeService.sendVerificationCode(email, request);
    }

    @PostMapping("/verification")
    public void verificateUser(@RequestParam(required = true) String email,
                               @RequestParam(required = true) String verificationToken,
                               HttpServletRequest request) {
        authorizeService.verificateUser(email, verificationToken);
    }
}
