using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;
namespace emergencyresponsesystem
{
    public partial class login_authentication : System.Web.UI.Page
    {
        firebaseConfiguration fconfig;
        IFirebaseClient client;
        FirebaseResponse response;
        string username;
        string password;

        string usr;
        string pass;
        protected async void Page_Load(object sender, EventArgs e)
        {
            bool validate = Convert.ToBoolean(Session["status"]);
            if (validate == true)
            {

                Response.Redirect("~/dashboard.aspx", false);
            }
            else
            {
                fconfig = new firebaseConfiguration();
                client = new FireSharp.FirebaseClient(fconfig.conenctFirebase());
                response = await client.GetAsync("Users/Admin");
                loginCredentials lc = response.ResultAs<loginCredentials>();
                this.username = lc.username;
                this.password = lc.password;
                usr = TextBox1.Text.ToString();
                pass = TextBox2.Text.ToString();
    
    }
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
           if (client != null)
            {
                if(usr == username && pass == password)
                {
                    label_status.Text = "Login Successfull";
                    Session["status"] = true;
                    Response.Redirect("~/Dashboard.aspx",false);
                }
                else
                {
                    label_status.Text = "Login Failed!!";

                }
            }
        }
    }
}