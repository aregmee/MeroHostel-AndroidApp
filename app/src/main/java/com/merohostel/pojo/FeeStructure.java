package com.merohostel.pojo;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by asimsangram on 2/16/16.
 */
public class FeeStructure {

    private int id;
    private int admissionFee;
    private int securityDeposit;
    private int oneBed;
    private int twoBed;
    private int threeBed;
    private int fourBed;

    public FeeStructure(int id, int admissionFee, int securityDeposit, int oneBed, int twoBed, int threeBed, int fourBed) {
        this.id = id;
        this.admissionFee = admissionFee;
        this.securityDeposit = securityDeposit;
        this.oneBed = oneBed;
        this.twoBed = twoBed;
        this.threeBed = threeBed;
        this.fourBed = fourBed;
    }

    public FeeStructure(JSONObject feeStructureJSONObject) throws Exception {

        id = feeStructureJSONObject.getInt("id");
        try {
            admissionFee = feeStructureJSONObject.getInt("admission");
        }catch (Exception e){

            admissionFee = 0;
        }
        try {
            securityDeposit = feeStructureJSONObject.getInt("security_deposit");
        }catch (Exception e){

            securityDeposit = 0;
        }
        try {
            oneBed = feeStructureJSONObject.getInt("1_bed");
        }catch(Exception e){

            oneBed = 0;
        }
        try {
            twoBed = feeStructureJSONObject.getInt("2_bed");
        }catch(Exception e){

            twoBed = 0;
        }
        try {
            threeBed = feeStructureJSONObject.getInt("3_bed");
        }catch(Exception e){

            threeBed = 0;
        }
        try {
            fourBed = feeStructureJSONObject.getInt("4_bed");
        }catch (Exception e){

            fourBed = 0;
        }
    }

    public int getId() {
        return id;
    }

    public int getAdmissionFee() {
        return admissionFee;
    }

    public int getSecurityDeposit() {
        return securityDeposit;
    }

    public int getOneBed() {
        return oneBed;
    }

    public int getTwoBed() {
        return twoBed;
    }

    public int getThreeBed() {
        return threeBed;
    }

    public int getFourBed() {
        return fourBed;
    }
}
