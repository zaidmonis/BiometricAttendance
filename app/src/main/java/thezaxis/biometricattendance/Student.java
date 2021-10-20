package thezaxis.biometricattendance;

public class Student {
    public String id;
    public String name;
    public String address;
    public String branch;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String username; // student belong to which user?

    public Student(String id, String name, String address, String branch, String username) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.branch = branch;
        this.username = username;
    }

    public Student(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}