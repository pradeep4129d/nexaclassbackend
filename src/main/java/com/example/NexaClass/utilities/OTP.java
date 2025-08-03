package com.example.NexaClass.utilities;

import java.time.LocalTime;

public class OTP{
    String otp;
    LocalTime time;
    public OTP(String otp,LocalTime time){this.otp=otp; this.time=time;}

    public String getOtp() {
        return otp;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}