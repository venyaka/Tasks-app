package veniamin.tasksapp.backend.service;

import veniamin.tasksapp.backend.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface MailService {

    void sendUserVerificationMail(User user, HttpServletRequest request);

    void sendPasswordRestoreMail(User user, HttpServletRequest request);
}
