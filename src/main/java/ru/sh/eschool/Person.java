package ru.sh.eschool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
class Person {
    private int prsId;
    private String fio;
    private boolean isEmployee;

    public int getPrsId() {
        return prsId;
    }

    public void setPrsId(int prsId) {
        this.prsId = prsId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public boolean getIsEmployee() {
        return isEmployee;
    }

    public void setIsEmployee(boolean employee) {
        isEmployee = employee;
    }

    @Override
    public String toString() {
        return prsId + " " + fio + " " + isEmployee;
    }
}