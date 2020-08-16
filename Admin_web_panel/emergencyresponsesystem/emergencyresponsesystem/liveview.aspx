<%@ Page Title="" Language="C#" MasterPageFile="~/fullsite.Master" AutoEventWireup="true" CodeBehind="liveview.aspx.cs" Inherits="emergencyresponsesystem.liveview" %>
<asp:Content ID="Content1" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
    Live View | RapidBox
   
   
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="head" runat="server">
   <!-- AIzaSyDhO2xc7ddhDsGDoKmFKKOMOgp_wgbIssk -->
   <!--  AIzaSyDYDwGJ20rc360dO8_MqjBmlVxpnBYeaug -->
       <script src="https://www.gstatic.com/firebasejs/3.3.0/firebase.js"></script>
    <script src="https://www.gstatic.com/firebasejs/7.14.1/firebase-app.js"></script>
    <script src="/__/firebase/7.14.1/firebase-app.js"></script>


    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #map {
        height: 100%;
      }
      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: 100%;
        margin: 0;
        padding: 0;
      }
      #description {
        font-family: Roboto;
        font-size: 15px;
        font-weight: 300;
      }

      #infowindow-content .title {
        font-weight: bold;
      }

      #infowindow-content {
        display: none;
      }

      #map #infowindow-content {
        display: inline;
      }

      .pac-card {
        margin: 10px 10px 0 0;
        border-radius: 2px 0 0 2px;
        box-sizing: border-box;
        -moz-box-sizing: border-box;
        outline: none;
        box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
        background-color: #fff;
        font-family: Roboto;
      }

      #pac-container {
        padding-bottom: 12px;
        margin-right: 12px;
      }

      .pac-controls {
        display: inline-block;
        padding: 5px 11px;
      }

      .pac-controls label {
        font-family: Roboto;
        font-size: 13px;
        font-weight: 300;
      }

      #pac-input {
        background-color: #fff;
        font-family: Roboto;
        font-size: 15px;
        font-weight: 300;
        margin-left: 12px;
        padding: 0 11px 0 13px;
        text-overflow: ellipsis;
        width: 400px;
      }

      #pac-input:focus {
        border-color: #4d90fe;
      }

      #title {
        color: #fff;
        background-color: #4d90fe;
        font-size: 25px;
        font-weight: 500;
        padding: 6px 12px;
      }
      #target {
        width: 345px;
      }


         </style>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    

    <h2>Live View</h2>
  
    <input id="pac-input" class="controls" type="text" placeholder="Search Box" />
    <div style="height:500px; width:100%;">
          

        <div id="map" ></div>
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

         var dbRef = database.ref('incident');
         dbRef.on('value', gotData, errData);
         var coordinates;
         var lat;
         var lon;
         function gotData(data) {
             //console.log(data.val());
             coordinates = data.val();
             var keys = Object.keys(coordinates);
             for (var i = 0; i < keys.length; i++) {
                 var k = keys[i];
                 lat = coordinates[k].latitude;
                 lon = coordinates[k].longitude;
                 console.log(lat, lon);
                 marker = new google.maps.Marker({
                     position: new google.maps.LatLng(lat, lon),
                     map: map
                 });


             }
             /*  var marker, i;
               var map = new google.maps.Map(document.getElementById('map'), {
                   zoom: 10,
                   center: new google.maps.LatLng(-33.92, 151.25),
                   mapTypeId: 'roadmap'
  
               });
               for (i = 0; i < coordinates.length; i++) {
                  
               }
               */
         }

         function errData(err) {
             console.log("error!!");
             console.log(err);
         }


         var map;
      
/*         var map;
         function initMap() {
        map = new google.maps.Map(document.getElementById('map'), {
          center: {lat: -34.397, lng: 150.644},
          zoom: 8
        });
         }*/
         function initAutocomplete() {
             map = new google.maps.Map(document.getElementById('map'), {
                 center: { lat: -33.8688, lng: 151.2195 },
                 zoom: 13,
                 mapTypeId: 'roadmap'
             });

            

             // Create the search box and link it to the UI element.
             var input = document.getElementById('pac-input');
             var searchBox = new google.maps.places.SearchBox(input);
             map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

             // Bias the SearchBox results towards current map's viewport.
             map.addListener('bounds_changed', function () {
                 searchBox.setBounds(map.getBounds());
             });

             var markers = [];
             // Listen for the event fired when the user selects a prediction and retrieve
             // more details for that place.
             searchBox.addListener('places_changed', function () {
                 var places = searchBox.getPlaces();

                 if (places.length == 0) {
                     return;
                 }

                 // Clear out the old markers.
                 markers.forEach(function (marker) {
                     marker.setMap(null);
                 });
                 markers = [];

                 // For each place, get the icon, name and location.
                 var bounds = new google.maps.LatLngBounds();
                 places.forEach(function (place) {
                     if (!place.geometry) {
                         console.log("Returned place contains no geometry");
                         return;
                     }
                     var icon = {
                         url: place.icon,
                         size: new google.maps.Size(71, 71),
                         origin: new google.maps.Point(0, 0),
                         anchor: new google.maps.Point(17, 34),
                         scaledSize: new google.maps.Size(25, 25)
                     };

                     // Create a marker for each place.
                     markers.push(new google.maps.Marker({
                         map: map,
                         icon: icon,
                         title: place.name,
                         position: place.geometry.location
                     }));

                     if (place.geometry.viewport) {
                         // Only geocodes have viewport.
                         bounds.union(place.geometry.viewport);
                     } else {
                         bounds.extend(place.geometry.location);
                     }
                 });
                 map.fitBounds(bounds);
             });
                    
         }
    </script>
<script>
</script>
     <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBljaPNDPxsTh5MnpeVwBS9x7rlAwThU08&libraries=places&callback=initAutocomplete"
    async defer></script>

</asp:Content>
