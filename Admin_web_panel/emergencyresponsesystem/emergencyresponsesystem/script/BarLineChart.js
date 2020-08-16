var fcc = new firebaseConnectClass();
var x = fcc.present();

var database = x;

var dbRef5 = database.ref().child('incident');


var acc = 0;
var crm = 0;
var fire = 0;
var oth = 0;

dbRef5.once('value', gotDataIncident, errData)


function gotDataIncident(data) {

    var scores = data.val();
    var keys = Object.keys(scores);
    for (var i = 0; i < keys.length; i++) {

        var k = keys[i];
        var subtype = scores[k].subtype;

  //      var a = new Date(timestamp * 1000);
    //    var v = a.getFullYear();

        //        var myDate = Date.parse(date);
        //var dateFinal = new Date(myDate);
        //  var v = dateFinal.getDate();

        // var dateYear = moment(timestamp, 'dd/mm/yy');

        //var v = dateYear.format('y');
      //  console.log(k + "" + v);
        if (subtype == "accident") {
            acc++;
        }
        if (subtype == "crime") {
            crm++;
        }
        if (subtype == "hazard") {
            fire++;
        }
        if (subtype == "others") {
            oth++;
        }
//        console.log(crm2020);

    }

    google.charts.load("current", { packages: ["corechart"] });
    google.charts.setOnLoadCallback(drawChart);
    function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ["Element", "Density", { role: "style" }],
            ["Accidents", acc, "#5c9aff"],
            ["Crimes", crm, "#f54b42"],
            ["Fire", fire, "#ffe75c"],
            ["Others", oth, "#808080"]

        ]);

        var view = new google.visualization.DataView(data);
        view.setColumns([0, 1,
            {
                calc: "stringify",
                sourceColumn: 1,
                type: "string",
                role: "annotation"
            },
            2]);

        var options = {
            title: "Emergencies reported on Newsfeed",
            width: 400,
            height: 200,
            bar: { groupWidth: "95%" },
            legend: { position: "none" },
        };
        var chart = new google.visualization.BarChart(document.getElementById("chart1_div"));
        chart.draw(view, options);
    }
    }


