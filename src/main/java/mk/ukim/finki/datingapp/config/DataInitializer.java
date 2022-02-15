package mk.ukim.finki.datingapp.config;

import mk.ukim.finki.datingapp.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer {

    public static List<User> users = new ArrayList<>();
}
