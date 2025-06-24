package jobhunter.domain.response;
import jakarta.persistence.*;
import jobhunter.util.constant.GenderEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;


public class UserUpdateDTO {
    private long id;
    private String name;
    private String email;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    private Instant updatedAt;
    private String updatedBy;
    private UserResponseDTO.companyUser company;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public UserResponseDTO.companyUser getCompany() {
        return company;
    }

    public void setCompany(UserResponseDTO.companyUser company) {
        this.company = company;
    }
}
