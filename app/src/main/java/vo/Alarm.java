package vo;

public class Alarm {

    private int alarmNo; // ARM_NO
    private String memberId; // MEM_ID

    public int getAlarmNo() {
        return alarmNo;
    }

    public void setAlarmNo(int alarmNo) {
        this.alarmNo = alarmNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getRegPotNo() {
        return regPotNo;
    }

    public void setRegPotNo(String regPotNo) {
        this.regPotNo = regPotNo;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    public String getValType() {
        return valType;
    }

    public void setValType(String valType) {
        this.valType = valType;
    }

    private String regPotNo; // REG_POTNO
    private int check; // CHECK
    private String valType; // VAL_TYPE

}
