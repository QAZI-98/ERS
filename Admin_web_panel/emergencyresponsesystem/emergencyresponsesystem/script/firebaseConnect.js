class firebaseConnectClass {




     present() {

        var config;
        config = {
            apiKey: "AIzaSyBWsXITM8yJGVUuNWWovt3_k7sk3IJc-nU",
            authDomain: "emergencyresponseapplica-ae858.firebaseapp.com",
            databaseURL: "https://emergencyresponseapplica-ae858.firebaseio.com",
            projectId: "emergencyresponseapplica-ae858",
            storageBucket: "emergencyresponseapplica-ae858.appspot.com",
            messagingSenderId: "963985829766",
            appId: "1:963985829766:web:68194c8d8921948a6f8607"
        };
        if (firebase.apps.length === 0) {
            firebase.initializeApp(config);
        }
      //  firebase.initializeApp(config);
        var database = firebase.database();
        return database;
    }
}

