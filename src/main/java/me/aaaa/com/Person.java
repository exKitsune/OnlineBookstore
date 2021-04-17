package me.aaaa.com;

public class Person {
    private String fName, lName, addr, city, state, phone, email, uid, pass, ccType;
    private int zip, ccNum;
    public Person(String fName, String lName, String addr, String city, String state, int zip, String phone, String email, String uid, String pass, String ccType, int ccNum) {
        this.fName = fName;
        this.lName = lName;
        this.addr = addr;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.phone = phone;
        this.email = email;
        this.uid = uid;
        this.pass = pass;
        this.ccType = ccType;
        this.ccNum = ccNum;
    }

    public Person() {}

    public void updateFirstName(String name) {
        fName = name;
    }

    public String getFirstName() {
        return fName;
    }

    public void updateLastName(String name) {
        lName = name;
    }

    public String getLastName() {
        return lName;
    }

    public void updateAddr(String naddr) {
        addr = naddr;
    }

    public String getAddr() {
        return addr;
    }

    public void updateCity(String ncity) {
        city = ncity;
    }

    public String getCity() {
        return city;
    }

    public void updateState(String nstate) {
        state = nstate;
    }

    public String getState() {
        return state;
    }

    public void updateZip(int nzip) {
        zip = nzip;
    }

    public int getZip() {
        return zip;
    }

    public void updatePhone(String nphone) {
        phone = nphone;
    }

    public String getPhone() {
        return phone;
    }

    public void updateEmail(String nemail) {
        email = nemail;
    }

    public String getEmail() {
        return email;
    }

    public void updateUID(String nuid) {
        uid = nuid;
    }

    public String getUID() {
        return uid;
    }

    public void updatePass(String npass) {
        pass = npass;
    }

    public String getPass() {
        return pass;
    }

    public void updateCCType(String ncctype) {
        ccType = ncctype;
    }

    public String getCCType() {
        return ccType;
    }

    public void updateCCNum(int nccnum) {
        ccNum = nccnum;
    }

    public int getCCNum() {
        return ccNum;
    }
}