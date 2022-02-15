package mk.ukim.finki.datingapp.service;

import mk.ukim.finki.datingapp.models.User;

public interface AuthService {

    User login(String username, String password);
}
