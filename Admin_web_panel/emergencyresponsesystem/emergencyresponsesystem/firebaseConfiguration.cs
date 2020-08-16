using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using FireSharp.Config;
using FireSharp.Interfaces;
using FireSharp.Response;

namespace emergencyresponsesystem
{
    public class  firebaseConfiguration
    {
        IFirebaseConfig config;
        public IFirebaseConfig conenctFirebase() {

            config = new FirebaseConfig();
               config.AuthSecret  = your key here";
           config.BasePath = "https://emergencyresponseapplica-ae858.firebaseio.com/";
            return config;
        }
    }
}