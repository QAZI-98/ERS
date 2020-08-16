
var fcc = new firebaseConnectClass();
var x = fcc.present();

var database = x;

var dbRef5 = database.ref().child('incident');

dbRef5.on('value', gotDataIncident, errData)
var acc = 0;
var crm = 0;
var fire = 0;
var oth = 0;

var accJan = 0;
var accFeb = 0;
var accMar = 0;
var accApr = 0;
var accMay = 0;
var accJune = 0;
var accJul = 0;
var accAug = 0;
var accSep = 0;
var accOct = 0;
var accNov = 0;
var accDec = 0;

var crmJan = 0;
var crmFeb = 0;
var crmMar = 0;
var crmApr = 0;
var crmMay = 0;
var crmJune = 0;
var crmJul = 0;
var crmAug = 0;
var crmSep = 0;
var crmOct = 0;
var crmNov = 0;
var crmDec = 0;


function gotDataIncident(data) {
    
    var scores = data.val();
    var keys = Object.keys(scores);
    //  SimpleDateFormat sdf;
    var date;
    for (var i = 0; i < keys.length; i++) {

        //sdf = new SimpleDateFormat("d/MM/yy");
        //date = sdf.format(new Date());

        var k = keys[i];
        var subtype = scores[k].subtype;
        var date = scores[k].date;
        if (subtype == "accident") {

            //console.log(date);
       
            const myDate = Date.parse(date);
            var dateFinal = new Date(myDate); 
          //  console.log(myDate);

            //var parts = myDate.toString().split('/');
            //console.log(parts);
            //var mydate = parts[1];
            var v = dateFinal.getMonth();

            if (v == 0) {
                accJan++;

            }
            else if (v == 1) {
                accFeb++;

            }
            else if (v == 2) {
                accMar++;

            }
            else if (v == 3) {
                accApr++;

            }
            else if (v == 4) {
                accMay++;

            }
            else if (v == 5) {
                accJune++;

            }
            else if (v == 6) {
                accJul++;

            }
            else if (v == 7) {
                accAug++;

            }
           else if (v == 8) {
                accSep++;

            }
           else if (v == 9) {
                accOct++;

            }
            else if (v == 10) {
                accNov++;

            }
            else if (v == 11) {
                accDec++;

            }
           // console.log(v);

            acc++;
        }
       else if (subtype == "crime") {

            const myDate = Date.parse(date);
            var dateFinal = new Date(myDate);
            //  console.log(myDate);

            //var parts = myDate.toString().split('/');
            //console.log(parts);
            //var mydate = parts[1];
            var v = dateFinal.getMonth();

            if (v == 0) {
                crmJan++;

            }
            else if (v == 1) {
                crmFeb++;

            }
            else if (v == 2) {
                crmMar++;

            }
            else if (v == 3) {
                crmApr++;

            }
            else if (v == 4) {
                console.log(crmMay);
                crmMay++;

            }
            else if (v == 5) {
                crmJune++;

            }
            else if (v == 6) {
                crmJul++;

            }
            else if (v == 7) {
                crmAug++;

            }
            else if (v == 8) {
                crmSep++;

            }
            else if (v == 9) {
                crmOct++;

            }
            else if (v == 10) {
                crmNov++;

            }
            else if (v == 11) {
                crmDec++;

            }


            crm++;
        }
        if (subtype == "hazard") {
            fire++;
        }
        if (subtype == "others") {
            oth++;
        }

       
    }

   
}

function errData(err) {

    console.log(err);
}

var ctx = document.getElementById('barchartcanvas').getContext('2d');
    var data = {
        label: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Oct", "Nov", "Dec"],
        datasets: [
            {
                label: "Accidents",
                data: [accJan, accFeb, accMar, accApr, accMay, accJune, accJul, accAug, accSep, accOct, accNov, accDec],
                backgroundColor: [
                    "rgba(10,20,30,0.3)",
                    "rgba(10,20,30,0.3)",
                    "rgba(10,20,30,0.3)",
                    "rgba(10,20,30,0.3)",
                    "rgba(10,20,30,0.3)"
                ],
                borderColor: [
                    "rgba(10,20,30,1)",
                    "rgba(10,20,30,1)",
                    "rgba(10,20,30,1)",
                    "rgba(10,20,30,1)",
                    "rgba(10,20,30,1)"
                ],
                borderWidth: 1
            },
            {
                label: "Crime",
                // data: [crmJan, crmFeb, crmMar, crmApr, crmMay, crmJune, crmJul, crmAug, crmSep, crmOct, crmNov, crmDec],
                data: [50, 34, 21, 10, 6, 4, 5, 2, 3, 5, 3, 6],

                backgroundColor: [
                    "rgba(50,150,250,0.3)",
                    "rgba(50,150,250,0.3)",
                    "rgba(50,150,250,0.3)",
                    "rgba(50,150,250,0.3)",
                    "rgba(50,150,250,0.3)"
                ],
                borderColor: [
                    "rgba(50,150,250,1)",
                    "rgba(50,150,250,1)",
                    "rgba(50,150,250,1)",
                    "rgba(50,150,250,1)",
                    "rgba(50,150,250,1)"
                ],
                borderWidth: 1
            }


        ]


    };
    var options = {
        title: {
            display: true,
            position: "top",
            text: "Bar Graph",
            fontSize: 18,
            fontColor: "#111"
        },
        legend: {
            display: true,
            positon: "bottom"
        }
    };
    var charts = new Chart(ctx, {
        type: "bar",
        data: data,
        options: options

    });