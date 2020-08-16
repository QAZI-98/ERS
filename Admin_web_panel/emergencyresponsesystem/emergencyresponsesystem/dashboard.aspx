<%@ Page Title="" Language="C#" MasterPageFile="~/fullsite.Master" AutoEventWireup="true" CodeBehind="Dashboard.aspx.cs" Inherits="emergencyresponsesystem.Dashboard" %>
<asp:Content ID="Content1" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
    Dashboard - RapidBox
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="head" runat="server">
        <link rel="stylesheet" href="styles/dashboard.css" type="text/css" />
 
       <script src="https://www.gstatic.com/firebasejs/3.3.0/firebase.js"></script>
    <script src="https://www.gstatic.com/firebasejs/7.14.1/firebase-app.js"></script>
      <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
     <script src="script/firebaseConnect.js"></script>
    <script src="/__/firebase/7.14.1/firebase-app.js"></script>
  <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
    <script src="script/moment.min.js"></script>
       <link rel="stylesheet" href="styles/loaderStyle.css">
     
    <style>
          
  
      .preloader{
         
          position: fixed;
          top: 0;
          left: 0;
          width: 100vw;
          height: 100vh;
          z-index: 1000;
      }
      .wrapper-dashboard{
          margin-left:50px;

      }
      .medteam{
              border-radius: 50%;
    width: 36px;
    height: 36px;
    padding: 8px;

    background: #fff;
    border: 10px solid red;
    color: #666;
    text-align: center;

    font: 32px Arial, sans-serif;

      }
      </style>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
  <div class="wrapper-dashboard">

  
    <h2>Dashboard</h2>


      <div class="loader"> 
                <img src="images/loadingff.gif" width="30" height="30" alt="Loading"/>
                </div>
    <div class="col-sm-9">
      <div class="well" style="margin-left: -12px;">
        <h4>Live Updates</h4>
       
         
      </div>
      <div class="row">
        <div class="col-sm-3">
          <div class="well">
            <h4>Users</h4>
          

           <p id="users">0</p> 
          </div>
        </div>
        <div class="col-sm-3">
          <div class="well">
            <h4>Ambulances</h4>
            <p id="medteam">0</p> 
          </div>
        </div>
        <div class="col-sm-3">
          <div class="well">
            <h4>Fire Fighters</h4>
            <p id="fireteam">0</p> 
          </div>
        </div>
        <div class="col-sm-3">
          <div class="well">
            <h4>Crime Team</h4>
            <p id="policeteam">0</p> 
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-sm-4">
          <div class="well">
            <h4>Number of Accidents</h4>
              <p id="accidents">0</p>
          </div>
        </div>
        <div class="col-sm-4">
          <div class="well">
            <h4>Number of Crimes</h4>
           
              <p id="crimes">0</p>
         
              </div>
        </div>
        <div class="col-sm-4">
          <div class="well">
            <h4>Number of Hazards</h4>
        
              <p id="hazards">0</p>
         
              </div>
        </div>
   
          <div class="col-sm-4" style="width:559px;"  >
          <div class="well" >
               <div id="chart_div" style="width: 100%; height: 300px;"></div>

         
              </div>

        </div>
        <div class="col-sm-4">
          <div class="well" style="width:252px;">
            <h4>Available</h4>
                <h5>Fire Fighters</h5>
              <p id="fireavailable">0</p>
                <h5>Police</h5>
              <p id="PoliceTeam">0</p>
                <h5>Ambulances</h5>
              <p id="MedicalTeam">0</p>
         
              </div>
        </div>
   <div class="col-sm-5">
          <div class="well" style="width: 530px;">
     
       <div id="chart1_div" style="width:100%;height:200px"></div>
              </div>
   </div>
          <div class="col-sm-5">
              <div class="well" style="width:252px; margin-left: 213px;">
                  <div id="countEvery"></div>
         </div>
                  </div>
      
      </div>

            </div>
  </div>  


      <script src="https://www.gstatic.com/firebasejs/3.3.0/firebase.js"></script>
   
     <script>

         var config = {
             apiKey: "AIzaSyBWsXITM8yJGVUuNWWovt3_k7sk3IJc-nU",
             authDomain: "emergencyresponseapplica-ae858.firebaseapp.com",
             databaseURL: "https://emergencyresponseapplica-ae858.firebaseio.com",
             projectId: "emergencyresponseapplica-ae858",
             storageBucket: "emergencyresponseapplica-ae858.appspot.com",
             messagingSenderId: "963985829766",
             appId: "1:963985829766:web:68194c8d8921948a6f8607"
         };
         // Initialize Firebase


         firebase.initializeApp(config);
        
         // Get a reference to the database service
         var database = firebase.database();
       
         var dbRef = database.ref().child('Users/Victim');
         var dbRef2 = database.ref().child('Users/Dispatcher/MedicalTeam');
         var dbRef3 = database.ref().child('Users/Dispatcher/FirefighterTeam');
         var dbRef4 = database.ref().child('Users/Dispatcher/PoliceTeam');
         var dbRefAvailabeFireFighters = database.ref().child('FirefighterTeamAvailable')
         var dbRefAvailabeCrime = database.ref().child('PoliceTeamAvailable')
         var dbRefAvailabeMedical = database.ref().child('MedicalTeamAvailable')


         var dbRef5 = database.ref().child('incident');

         dbRef.on('value', gotData, errData)
         dbRef2.on('value', gotDataMed, errData)
         dbRef3.on('value', gotDataFire, errData)
         dbRef4.on('value', gotDataPol, errData)

         dbRef5.on('value', gotDataIncident, errData)
         dbRefAvailabeFireFighters.on('value', gotDataAvailable, errData)
         dbRefAvailabeCrime.on('value', gotDataCrimeAvailable, errData)
         dbRefAvailabeMedical.on('value', gotDataMedicalAvailable, errData)
         function gotDataAvailable(data) {
             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("fireavailable").innerHTML = kl;
             console.log(keys.length);
         }
         function gotDataCrimeAvailable(data) {

             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("PoliceTeam").innerHTML = kl;
             console.log(keys.length);
         }
         function gotDataMedicalAvailable(data) {

             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("MedicalTeam").innerHTML = kl;
             console.log(keys.length);
         }


         function gotData(data) {

             //console.log(data.val());
             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("users").innerHTML = kl;
             console.log(keys.length);
         }
         function gotDataMed(data) {

             //console.log(data.val());
             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("medteam").innerHTML = kl;
             console.log(keys.length);
         }
         function gotDataFire(data) {

             //console.log(data.val());
             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("fireteam").innerHTML = kl;
             console.log(keys.length);
         }
         function gotDataPol(data) {

             //console.log(data.val());
             var scores = data.val();
             var keys = Object.keys(scores);
             var kl = keys.length;
             document.getElementById("policeteam").innerHTML = kl;
             console.log(keys.length);
         }

         function gotDataIncident(data) {
             var acc = 0;
             var crm = 0;
             var fire = 0;

             var scores = data.val();
             var keys = Object.keys(scores);
             for (var i = 0; i < keys.length; i++) {

                 var k = keys[i];
                 var subtype = scores[k].subtype;
                 if (subtype == "accident")
                 {
                     acc++;
                 }
                 if (subtype == "crime") {
                     crm++;
                 }
                 if (subtype == "hazard") {
                     fire++;
                 }


             }
             document.getElementById("accidents").innerHTML = acc;
             document.getElementById("crimes").innerHTML = crm;
             document.getElementById("hazards").innerHTML = fire;


             

         }

     
         function errData(err) {

             console.log(err);
         }


         /* dbRef.on('child_added', function (data) {
             var coordinates;
             var lat;
             var lon;
             //console.log(data.val());
             coordinates = data.val();

             var usercount = document.getElementById('users');
             var keys = Object.keys(coordinates);

             for (var i = 0; i < keys.length; i++) {
                 var d = keys[i];
                 var lat = coordinates[d].email;
                 
             }
             document.getElementById("users").innerHTML = x;
             //usercount.innerText(x);    
             //usercount.innerHTML(keys.length);

             /*  var marker, i;
               var map = new google.maps.Map(document.getElementById('map'), {
                   zoom: 10,
                   center: new google.maps.LatLng(-33.92, 151.25),
                   mapTypeId: 'roadmap'
  
               });
               for (i = 0; i < coordinates.length; i++) {
                  
               }
               */
         //});


        

         
</script>
    <script src="script/areaChart.js"></script>
   <script src="script/BarLineChart.js"></script>   
    <script src="script/donutUsers.js"></script>
    <script>
         window.onload = function () {
             //hide the preloader
             document.querySelector(".loader").style.display = "none";
         }
    </script> 
       <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBljaPNDPxsTh5MnpeVwBS9x7rlAwThU08&libraries=places&callback=initAutocomplete"
    async defer></script>

</asp:Content>
