var fcc = new firebaseConnectClass();
var x = fcc.present();

var database = x;

var dbRef5 = database.ref().child('history');


var acc2020 = 0;
var acc2019 = 0;
var acc2021 = 0;

var crm2020 = 0;
var crm2019 = 0;
var crm2021 = 0;

var fire2020 = 0;
var fire2019 = 0;
var fire2021 = 0;

dbRef5.once('value', gotDataIncident, errData)


function gotDataIncident(data) {

    var scores = data.val();
    var keys = Object.keys(scores);
    for (var i = 0; i < keys.length; i++) {

        var k = keys[i];
        var timestamp = scores[k].timestamp;
        var reportType = scores[k].reportType;

        var a = new Date(timestamp * 1000);
        var v = a.getFullYear();

        //        var myDate = Date.parse(date);
        //var dateFinal = new Date(myDate);
        //  var v = dateFinal.getDate();

       // var dateYear = moment(timestamp, 'dd/mm/yy');

        //var v = dateYear.format('y');
        console.log(k+""+v);
        if (v == "2019") {
            if (reportType == "Accident") {
                acc2019++;
            }
            else if (reportType == "Crime") {
                crm2019++;
            }
            else if (reportType == "Fire") {
                fire2019++;
            }
        }
        else if (v == "2020") {
            
            if (reportType == "Accident") {
                acc2020++;
            }
            else if (reportType == "Crime") {
                crm2020++;
                console.log(reportType);
            }
            else if (reportType == "Fire") {
                fire2020++;
            }
        }
        else if (v == "2021") {
            if (reportType == "Accident") {
                acc2021++;
            }
            else if (reportType == "Crime") {
                crm2021++;
            }
            else if (reportType == "Fire") {
                fire2021++;
            }
        }

        console.log(crm2020);

    }
    
    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {
        var data = google.visualization.arrayToDataTable([
            ['Year', 'Accidents', 'Crimes', 'Fire'],
            ['2019', acc2019, crm2019, fire2019],
            ['2020', acc2020, crm2020, fire2020],
            ['2021', acc2021, crm2021, fire2021],
        ]);

        var options = {
            title: 'Emergency Help provided',
            hAxis: { title: 'Year', titleTextStyle: { color: '#333' } },
            vAxis: { minValue: 0 }
        };

        var chart = new google.visualization.AreaChart(document.getElementById('chart_div'));
        chart.draw(data, options);
    }

   
}