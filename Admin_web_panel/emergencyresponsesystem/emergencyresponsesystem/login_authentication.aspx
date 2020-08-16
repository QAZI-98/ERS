<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="login_authentication.aspx.cs" Inherits="emergencyresponsesystem.login_authentication"  Async="true" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title></title>
    <link rel="stylesheet" href="styles/style.css" type="text/css" />
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>
</head>
   
<body>
    <form id="form1" runat="server">
        <div class="box-wrapper">
    
    <img src="images/pnglogo6.png" />
            <ul>
                <li><i class="material-icons" style="font-size:18px;color:#EB7D16; padding:5px;">email</i><asp:Label ID="Label1"  class="labelVal"  runat="server" Text="Username">
</asp:Label></li>
            <li><br /></li>
                <li><asp:TextBox ID="TextBox1" class="textbox-element" runat="server"></asp:TextBox></li>
              <li><br /></li>
                <li><i class="material-icons" style="font-size:18px;color:#EB7D16; padding:5px;">lock</i><asp:Label ID="Label2" runat="server" class="labelVal"  Text="Password"></asp:Label></li>
            <li><br /></li>
                <li><asp:TextBox ID="TextBox2" class="textbox-element" runat="server" placeholder="Enter your password"  TextMode="Password"></asp:TextBox></li>
            <li><br /></li>
                <li><asp:Button ID="Button1" class="btn" runat="server" Text="Login" OnClick="Button1_Click" /></li>
        <li>  <asp:Label ID="label_status" class="labelVal"  runat="server" Text=""></asp:Label>   </li>
            
            </ul>
                </div>
    </form>
</body>
</html>
