package com.rapidbox.emergencyresponseapplication.historyRecyclerView;

public class historyObject {

    private String rideId;
    private String time;
    private String reportTypeString;
    private String fromLocation;
    private String toLocation;
    private String teamType;
  //  private String teamName;
//    private String teamPhone;




    public historyObject(String rideId,String time,String reportTypeString,String fromLocation,String toLocation,String teamType ){
        this.rideId =rideId;
        this.time = time;
        this.reportTypeString = reportTypeString;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.teamType = teamType;
    //    this.teamName = teamName;
      //  this.teamPhone = teamPhone;

    }
    public String getTeamType(){

        return teamType;
    }
 /*   public String getTeamName(){

        return teamName;
    }
    public String getTeamPhone(){

        return teamPhone;
    }
*/
    public String getRideId(){

        return rideId;
    }

    public String getFromLocation(){

        return fromLocation;
    }
    public String getToLocation(){

        return toLocation;
    }

    public String getReportType(){

        return reportTypeString;
    }
    public void setReportType(String reportTypeString){

        this.reportTypeString = reportTypeString;

    }

    public void setRideId(String rideId){
        this.rideId =rideId;


    }
    public String getTime(){

        return time;
    }

    public void setTime(){

        this.time = time;

    }

}
