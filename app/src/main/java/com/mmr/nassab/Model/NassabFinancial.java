package com.mmr.nassab.Model;

/**
 * Created by Mojtaba Rajabi on 08/01/2019.
 */

public class NassabFinancial {

    private String nassabId;
    private String nassabName;

    public String getNassabImage() {
        return nassabImage;
    }

    public void setNassabImage(String nassabImage) {
        this.nassabImage = nassabImage;
    }

    private String nassabImage;
    private double credit;
    private float easyNasb;
    private float hardNasb;

    public NassabFinancial() {

        this.credit = 0;
        this.easyNasb = 0;
        this.hardNasb = 0;
    }

    public String getNassabId() {
        return nassabId;
    }

    public void setNassabId(String nassabId) {
        this.nassabId = nassabId;
    }

    public String getNassabName() {
        return nassabName;
    }

    public void setNassabName(String nassabName) {
        this.nassabName = nassabName;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public float getEasyNasb() {
        return easyNasb;
    }

    public void setEasyNasb(int easyNasb) {
        this.easyNasb = easyNasb;
    }

    public float getHardNasb() {
        return hardNasb;
    }

    public void setHardNasb(int hardNasb) {
        this.hardNasb = hardNasb;
    }


    public void plusEasyNasb() {
        this.easyNasb++;
    }

    public void plusHardNasb() {
        this.hardNasb++;
    }

    public void plusNasb(String hardness, int length) {
        if (hardness.equals("1"))
            if (length == 1)
                this.easyNasb++;
            else
                this.easyNasb += 0.5;
        else if (hardness.equals("2"))
            if (length == 1)
                this.hardNasb++;
            else
                this.hardNasb += 0.5;
    }
}
