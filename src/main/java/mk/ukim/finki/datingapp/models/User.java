package mk.ukim.finki.datingapp.models;

import mk.ukim.finki.datingapp.models.enumerations.Role;
import mk.ukim.finki.datingapp.models.enumerations.Sex;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "dating_users")
public class User implements UserDetails {

    @Id
    private String username;

    private String password;

    private String name;

    private String surname;

    private int age;

    private String bio;

    private String city;

    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Sex sex;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @ManyToMany
    List<User> likes;

    @ManyToMany
    List<User> interestedIn;

    @ManyToMany
    List<User> matched;

    public User(String username, String password, String name, String surname, Role role,
                int age, String bio, String city, Sex sex, String phoneNumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.age = age;
        this.bio = bio;
        this.city = city;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
        this.likes = new ArrayList<>();
        this.interestedIn = new ArrayList<>();
        this.matched = new ArrayList<>();
    }

    public User() {

    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<User> getLikes() {
        return likes;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void addLikes(User user) {
        this.likes.add(user);
    }

    public List<User> getInterestedIn() {
        return interestedIn;
    }

    public void addInterestedIn(User user) {
        this.interestedIn.add(user);
    }

    public List<User> getMatched() {
        return matched;
    }

    public void addMatched(User user) {
        this.matched.add(user);
    }
}
