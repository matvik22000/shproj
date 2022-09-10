import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    String prsId;
    String fio;

    public String getPrsId() {
        return prsId;
    }

    public void setPrsId(String prsId) {
        this.prsId = prsId;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }
}