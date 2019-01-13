package com.example.dungbong.finalproject;

public class PlanListItem {
    private String planname = null;
    private String plandate = null;
    private int clearflag = -1;

    public PlanListItem(){

    }

    public PlanListItem(String planname,String plandate,int clearflag){
        this.planname = planname;
        this.plandate = plandate;
        this.clearflag = clearflag;
    }

    public String Return_PlanName(){
        return this.planname;
    }

    public String Return_PlanDate(){
        return this.plandate;
    }

    public int Return_ClearFrag(){
        return clearflag;
    }


}
