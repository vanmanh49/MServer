package com.vm.mserver.model;

/**
 * Created by VanManh on 04-Dec-17.
 */

public class Student {
    private int ms;
    private String name;
    private double toan;
    private double ly;
    private double hoa;

    public Student() {
    }

    public Student(int ms, String name, double toan, double ly, double hoa) {
        this.ms = ms;
        this.name = name;
        this.toan = toan;
        this.ly = ly;
        this.hoa = hoa;
    }

    public int getMs() {
        return ms;
    }

    public void setMs(int ms) {
        this.ms = ms;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getToan() {
        return toan;
    }

    public void setToan(double toan) {
        this.toan = toan;
    }

    public double getLy() {
        return ly;
    }

    public void setLy(double ly) {
        this.ly = ly;
    }

    public double getHoa() {
        return hoa;
    }

    public void setHoa(double hoa) {
        this.hoa = hoa;
    }

    @Override
    public String toString() {
        return "\nMã số: " + this.ms
                + "          Tên: " + this.name
                + "\n\nToán: " + this.toan
                + "    ||    Lý: " + this.ly
                + "    ||    Hóa: " + this.hoa
                +"\n";
    }

    public boolean checkValidate() {
        if (this.name != null && !this.name.equals("")) {
            return true;
        }
        return false;
    }
}
