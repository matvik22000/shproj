package ru.sh.eschool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class StateUser {
    private int prsId;

    public int getPrsId() {
        return prsId;
    }

    public void setPrsId(int prsId) {
        this.prsId = prsId;
    }
}
