package ru.sh.eschool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class State {
    private StateUser user;

    public StateUser getUser() {
        return user;
    }

    public void setUser(StateUser user) {
        this.user = user;
    }
}
