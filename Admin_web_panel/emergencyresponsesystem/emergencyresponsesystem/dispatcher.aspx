<%@ Page Title="" Language="C#" MasterPageFile="~/fullsite.Master" AutoEventWireup="true" CodeBehind="dispatcher.aspx.cs" Inherits="emergencyresponsesystem.dispatcher" %>
<asp:Content ID="Content1" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">

</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="head" runat="server">
     <script src="https://www.gstatic.com/firebasejs/3.3.0/firebase.js"></script>
    <script src="https://www.gstatic.com/firebasejs/7.15.1/firebase-app.js"></script>
    
     <script src="https://www.gstatic.com/firebasejs/7.15.1/firebase-auth.js"></script>
  <script src="https://www.gstatic.com/firebasejs/7.15.1/firebase-firestore.js"></script>
      <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
   <style>
       .form-control{
           width:200px;
       margin-bottom:1px;
       height:30px;

           }
       .form-group {
    margin-bottom: 10px;
}
   </style>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">

   

    <div class="container">
        <div class="form-group">
            <label for="sel1">Old Password</label><br />
 <input  id="oldpass" type="text" class="form-control" placeholder="Enter Password">
            </div>      
        <div class="form-group">
            <label for="sel1">New Password</label><br />
 <input  id="newpass" type="text" class="form-control" placeholder="Enter Password">
            </div>
      
          </div>
       <div class="form-group">
           <input id="signup" type="button" value="Reset Password" />
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
         firebase.initializeApp(config);
         var database = firebase.database();

         var dbRef_Team;
         var password = "";
         var dbRef2;
         function getDataColumns() {

              dbRef2 = database.ref().child('Users/Admin');
             dbRef2.once('value', gotUserTotal, errData)

         }
         function gotUserTotal(data) {
             var scores = data.val();
              
                  password = scores.password;

              //   console.log(username + "" + password);
                 
             
            
         }
         function errData(err) {

             console.log(err);
         }

         function handleSignUp() {
             var mOldPass = document.getElementById("oldpass").value;
             var mNewPass = document.getElementById("newpass").value;
            // var newPostKey = firebase.database().ref().child('password');
             var result = mOldPass.localeCompare(password);
             console.log(mOldPass);
             console.log("database " + password);
             if (result == 0) {
                 if (mNewPass.length > 6) {
                    

                     dbRef2.update({ password: mNewPass });
                     alert('New Password Successfully Updated!');

                 } else {
                     alert('Password is weak.');
                     return;

                 }    

             }
             else {
                 alert('Old Password is incorrect!');

             }
             
         }
         function initApp() {

             document.getElementById("signup").addEventListener('click', handleSignUp, false);
             getDataColumns();
         }
         window.onload = function () {
             initApp();
         };
        </script>
</asp:Content>
