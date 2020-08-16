using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace emergencyresponsesystem
{
    public partial class Dashboard : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            bool validate = Convert.ToBoolean(Session["status"]);
            if (validate == false)
            {

                Response.Redirect("~/login_authentication.aspx", false);
            }
           
        }

        protected void Button1_Click(object sender, EventArgs e)
        {
           if (Session["status"] != null)
            {
                Session["status"] = false;
                Response.Redirect("~/login_authentication.aspx", false);

            }
        }
    }
}