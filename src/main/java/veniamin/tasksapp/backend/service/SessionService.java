package veniamin.tasksapp.backend.service;

import veniamin.tasksapp.backend.entity.UserSession;

public interface SessionService {

    UserSession saveNewSession(Long userId);

}
