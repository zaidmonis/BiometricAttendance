package thezaxis.biometricattendance;

import androidx.annotation.Nullable;

public class User {

    public String username;
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof User))
            return false;
        User otherUser = (User) obj;
        if (this.username.equals(otherUser.username) && this.password.equals(otherUser.password)){
            return true;
        }
        return false;
    }
}
