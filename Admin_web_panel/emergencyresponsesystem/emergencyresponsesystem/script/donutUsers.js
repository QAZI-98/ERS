var fcc = new firebaseConnectClass();
var x = fcc.present();

var database = x;
var kl1;
var kl2;
var kl3;
var kl4;

var dbRef5 = database.ref().child('Users/Victim');
var dbRef2 = database.ref().child('Users/Dispatcher/FirefighterTeam');
var dbRef1 = database.ref().child('Users/Dispatcher/MedicalTeam');
var dbRef = database.ref().child('Users/Dispatcher/PoliceTeam');

dbRef5.once('value', gotUserTotal, errData)
dbRef2.once('value', gotFireTotal, errData)
dbRef1.once('value', gotMedTotal, errData)
dbRef.once('value', gotPolTotal, errData)

var total;
function gotUserTotal(data) {

    var scores = data.val();
    var keys = Object.keys(scores);
     kl1 = keys.length;
    // document.getElementById("users").innerHTML = kl;
   // total = kl;
}
function gotFireTotal(data) {

    var scores = data.val();
    var keys = Object.keys(scores);
    kl2 = keys.length;
    // document.getElementById("users").innerHTML = kl;
    // total = kl;
}
function gotMedTotal(data) {
    var scores = data.val();
    var keys = Object.keys(scores);
    kl3 = keys.length;

}
function gotPolTotal(data) {
    var scores = data.val();
    var keys = Object.keys(scores);
    kl4 = keys.length;


    google.charts.load('current', { 'packages': ['corechart'] });
    google.charts.setOnLoadCallback(drawChart);

    function drawChart() {

        var data = google.visualization.arrayToDataTable([
            ['Effort', 'Amount given'],
            ['Users', kl1],
            ['Fire Fighters', kl2],
            ['Ambulances', kl3],
            ['Police', kl4]


        ]);

        var options = {
            title: 'User and Dispatchers',
            pieHole: 0.5,
            pieSliceTextStyle: {
                color: 'White',
            },
            legend: 'none'
        };

        var chart = new google.visualization.PieChart(document.getElementById('countEvery'));
        chart.draw(data, options);

    }
}
