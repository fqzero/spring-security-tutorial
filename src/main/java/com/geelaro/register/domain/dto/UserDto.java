package com.geelaro.register.domain.dto;

import javax.validation.constraints.*;

public class UserDto {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    @Email
    private String email;

    @NotEmpty
    @NotNull
    @Size(min = 6,max = 20)
    private String passWd;

    @NotEmpty
    @NotNull
    @Size(min = 6,max = 20)
    private String matchPassWd;

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

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getMatchPassWd() {
        return matchPassWd;
    }

    public void setMatchPassWd(String matchPassWd) {
        this.matchPassWd = matchPassWd;
    }
}
