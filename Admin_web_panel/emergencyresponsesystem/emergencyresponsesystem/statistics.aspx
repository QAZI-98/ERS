<%@ Page Title="" Language="C#" MasterPageFile="~/fullsite.Master" AutoEventWireup="true" CodeBehind="statistics.aspx.cs" Inherits="emergencyresponsesystem.statistics" %>
<asp:Content ID="Content1" ContentPlaceHolderID="ContentPlaceHolder2" runat="server">
    Statistics - RapidBox
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="head" runat="server">
   <script src="script/firebaseConnect.js"></script>
    <script src="script/jquery.min.js"></script>
    <script src="script/Chart.min.js"></script>
    <script src="script/moment.min.js"></script>
 <!-- <script src="script/bar.js"></script> -->
    
   
    <style>
        .chart-container {
            width:500px;
            height:500px;
        }
        .chart-wrapper{
            width:500px;
            height:500px;
        margin:0 auto;
            }
    </style>

</asp:Content>

<asp:Content ID="Content3" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <h2>Statistics</h2>
    <div class="col-sm-9">
 
        <div class="well">
     <h4>Live Charts</h4>
       </div>
        
      <div class="row">
  
          <div class="col-sm-5">
               <div class="well" style="width: 530px; margin-left: 15px;"">
     
               <canvas id="myBarChart" width="400" height="400"></canvas>
                  </div>
          </div>
          <div class="col-sm-5">
              <div class="well" style="width:252px; margin-left:200px;">
                   <canvas id="mychart"></canvas>
         </div>
                  </div>
   

                     <div class="chart-wrapper" >
       
        </div>
  


     </div>

      </div>
        
    
  
    <script src ="script/piechart.js"></script>
    <script src="script/baradvance.js"></script>
    
    </asp:Content>
