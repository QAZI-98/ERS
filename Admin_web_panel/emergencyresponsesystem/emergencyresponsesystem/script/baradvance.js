
var fcc = new firebaseConnectClass();
var x = fcc.present();

var database = x;

var dbRef5 = database.ref().child('incident');


dbRef5.once('value', gotDataIncident, errData)
//dbRef5.once('value', gotDataCrime, errData)
//dbRef5.once('value', gotDataHazards, errData)
//dbRef5.once('value', gotDataOthers , errData)


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

var hazJan = 0;
var hazFeb = 0;
var hazMar = 0;
var hazApr = 0;
var hazMay = 0;
var hazJune = 0;
var hazJul = 0;
var hazAug = 0;
var hazSep = 0;
var hazOct = 0;
var hazNov = 0;
var hazDec = 0;

var othJan = 0;
var othFeb = 0;
var othMar = 0;
var othApr = 0;
var othMay = 0;
var othJune = 0;
var othJul = 0;
var othAug = 0;
var othSep = 0;
var othOct = 0;
var othNov = 0;
var othDec = 0;
/*
function gotDataCrime(data) {
    var scores = data.val();
    var keys = Object.keys(scores);
    //  SimpleDateFormat sdf;
  
    for (var i = 0; i < keys.length; i++) {

        //sdf = new SimpleDateFormat("d/MM/yy");
        //date = sdf.format(new Date());

        var k = keys[i];
        var subtype = scores[k].subtype;
        var date = scores[k].date;
        //        var myDate = Date.parse(date);
        //var dateFinal = new Date(myDate);
        //  var v = dateFinal.getDate();

        var dateMonth = moment(date, 'dd/mm/yy');

        var v = dateMonth.format('m');

        if (subtype == "crime") {

            //  const myDate = Date.parse(date);
            //    var dateFinal = new Date(myDate);
            //  console.log(myDate);

            //var parts = myDate.toString().split('/');
            //console.log(parts);
            //var mydate = parts[1];

            //console.log(y)
            if (v == 1) {
                crmJan++;

            }
            else if (v == 2) {
                crmFeb++;

            }
            else if (v == 3) {
                crmMar++;

            }
            else if (v == 4) {
                crmApr++;

            }
            else if (v == 5) {
                console.log(crmMay);
                crmMay++;

            }
            else if (v == 6) {

                crmJune++;
  
            }
            else if (v == 7) {
                crmJul++;

            }
            else if (v == 8) {
                crmAug++;

            }
            else if (v == 9) {
                crmSep++;

            }
            else if (v == 10) {
                crmOct++;

            }
            else if (v == 11) {
                crmNov++;

            }
            else if (v == 12) {
                crmDec++;

            }


        }


    }

}
function gotDataHazards(data) {
    var scores = data.val();
    var keys = Object.keys(scores);
    //  SimpleDateFormat sdf;
    var date;
    //  var v;
    //var y;
    for (var i = 0; i < keys.length; i++) {

        //sdf = new SimpleDateFormat("d/MM/yy");
        //date = sdf.format(new Date());

        var k = keys[i];
        var subtype = scores[k].subtype;
        var date = scores[k].date;
        //        var myDate = Date.parse(date);
        //var dateFinal = new Date(myDate);
        //  var v = dateFinal.getDate();

        var dateMonth = moment(date, 'dd/mm/yy');

        var v = dateMonth.format('m');

        //      var myDate = Date.parse(date);

        //  v = myDate.get
        //const datexx = moment(myDate, 'DD/MM/YYYY');
        //const split = myDate.split('/');
        // console.log('month', split[1])

        // v = dateFinal.getDay();
        //v = datexx.format('M');

        console.log("month " + v);
      //  console.log(y)
        if (subtype == "hazard") {
          //  console.log(y)
            if (v == 1) {
                hazJan++;

            }
            else if (v == 2) {
                hazFeb++;

            }
            else if (v == 3) {
                hazMar++;

            }
            else if (v == 4) {
                hazApr++;

            }
            else if (v == 5) {
                hazMay++;

            }
            else if (v == 6) {

                hazJune++;

            }
            else if (v == 7) {
                hazJul++;

            }
            else if (v == 8) {
                hazAug++;

            }
            else if (v == 9) {
                hazSep++;

            }
            else if (v == 10) {
                hazOct++;

            }
            else if (v == 11) {
                hazNov++;

            }
            else if (v == 12) {
                hazDec++;

            }


        }

    }
}
function gotDataOthers(data) {
    var scores = data.val();
    var keys = Object.keys(scores);
    //  SimpleDateFormat sdf;
    var date;
    //  var v;
    //var y;
    for (var i = 0; i < keys.length; i++) {

        //sdf = new SimpleDateFormat("d/MM/yy");
        //date = sdf.format(new Date());

        var k = keys[i];
        var subtype = scores[k].subtype;
        var date = scores[k].date;
        //        var myDate = Date.parse(date);
        //var dateFinal = new Date(myDate);
        //  var v = dateFinal.getDate();

        var dateMonth = moment(date, 'dd/mm/yy');

        var v = dateMonth.format('m');

        //      var myDate = Date.parse(date);

        //  v = myDate.get
        //const datexx = moment(myDate, 'DD/MM/YYYY');
        //const split = myDate.split('/');
        // console.log('month', split[1])

        // v = dateFinal.getDay();
        //v = datexx.format('M');

        console.log("month " + v);
      //  console.log(y)
                if (subtype == "others") {


            //  const myDate = Date.parse(date);
            //    var dateFinal = new Date(myDate);
            //  console.log(myDate);

            //var parts = myDate.toString().split('/');
            //console.log(parts);
            //var mydate = parts[1];

       //     console.log(y)
            if (v == 1) {
                othJan++;

            }
            else if (v == 2) {
                othFeb++;

            }
            else if (v == 3) {
                othMar++;

            }
            else if (v == 4) {
                othApr++;

            }
            else if (v == 5) {
                console.log(crmMay);
                othMay++;

            }
            else if (v == 6) {

                othJune++;
                console.log("crime june" + crmJune);

            }
            else if (v == 7) {
                othJul++;

            }
            else if (v == 8) {
                othAug++;

            }
            else if (v == 9) {
                othSep++;

            }
            else if (v == 10) {
                othOct++;

            }
            else if (v == 11) {
                othNov++;

            }
            else if (v == 12) {
                othDec++;

            }


            //crm++;
        }

    }


}*/
    function gotDataIncident(data) {

        var scores = data.val();
        var keys = Object.keys(scores);
        //  SimpleDateFormat sdf;
        var date;
        //  var v;
        //var y;
        for (var i = 0; i < keys.length; i++) {

            //sdf = new SimpleDateFormat("d/MM/yy");
            //date = sdf.format(new Date());

            var k = keys[i];
            var subtype = scores[k].subtype;
            var date = scores[k].date;
            //        var myDate = Date.parse(date);
            //var dateFinal = new Date(myDate);
            //  var v = dateFinal.getDate();

            var dateMonth = moment(date, 'dd/mm/yy');

            var v = dateMonth.format('m');

            //      var myDate = Date.parse(date);

            //  v = myDate.get
            //const datexx = moment(myDate, 'DD/MM/YYYY');
            //const split = myDate.split('/');
            // console.log('month', split[1])

            // v = dateFinal.getDay();
            //v = datexx.format('M');

            console.log("month " + v);
          //  console.log(y)
            if (subtype == "accident") {

                //console.log(date);

                //  console.log(myDate);

                //var parts = myDate.toString().split('/');
                //console.log(parts);
                //var mydate = parts[1];

                if (v == 1) {
                    accJan++;

                }
                else if (v == 2) {
                    accFeb++;

                }
                else if (v == 3) {
                    accMar++;

                }
                else if (v == 4) {
                    accApr++;

                }
                else if (v == 5) {
                    accMay++;

                }
                else if (v == 6) {
                    accJune++;

                }
                else if (v == 7) {
                    accJul++;

                }
                else if (v == 8) {
                    accAug++;

                }
                else if (v == 9) {
                    accSep++;

                }
                else if (v == 10) {
                    accOct++;

                }
                else if (v == 11) {
                    accNov++;

                }
                else if (v == 12) {
                    accDec++;

                }
                // console.log(v);

                acc++;
            }

            else if (subtype == "crime") {

                //  const myDate = Date.parse(date);
                //    var dateFinal = new Date(myDate);
                //  console.log(myDate);

                //var parts = myDate.toString().split('/');
                //console.log(parts);
                //var mydate = parts[1];

                //console.log(y)
                if (v == 1) {
                    crmJan++;

                }
                else if (v == 2) {
                    crmFeb++;

                }
                else if (v == 3) {
                    crmMar++;

                }
                else if (v == 4) {
                    crmApr++;

                }
                else if (v == 5) {
                    console.log(crmMay);
                    crmMay++;

                }
                else if (v == 6) {

                    crmJune++;

                }
                else if (v == 7) {
                    crmJul++;

                }
                else if (v == 8) {
                    crmAug++;

                }
                else if (v == 9) {
                    crmSep++;

                }
                else if (v == 10) {
                    crmOct++;

                }
                else if (v == 11) {
                    crmNov++;

                }
                else if (v == 12) {
                    crmDec++;

                }


            }
            else if (subtype == "hazard") {
                //  console.log(y)
                if (v == 1) {
                    hazJan++;

                }
                else if (v == 2) {
                    hazFeb++;

                }
                else if (v == 3) {
                    hazMar++;

                }
                else if (v == 4) {
                    hazApr++;

                }
                else if (v == 5) {
                    hazMay++;

                }
                else if (v == 6) {

                    hazJune++;

                }
                else if (v == 7) {
                    hazJul++;

                }
                else if (v == 8) {
                    hazAug++;

                }
                else if (v == 9) {
                    hazSep++;

                }
                else if (v == 10) {
                    hazOct++;

                }
                else if (v == 11) {
                    hazNov++;

                }
                else if (v == 12) {
                    hazDec++;

                }


            }
            if (subtype == "others") {


                //  const myDate = Date.parse(date);
                //    var dateFinal = new Date(myDate);
                //  console.log(myDate);

                //var parts = myDate.toString().split('/');
                //console.log(parts);
                //var mydate = parts[1];

                //     console.log(y)
                if (v == 1) {
                    othJan++;

                }
                else if (v == 2) {
                    othFeb++;

                }
                else if (v == 3) {
                    othMar++;

                }
                else if (v == 4) {
                    othApr++;

                }
                else if (v == 5) {
                    console.log(crmMay);
                    othMay++;

                }
                else if (v == 6) {

                    othJune++;
                    console.log("crime june" + crmJune);

                }
                else if (v == 7) {
                    othJul++;

                }
                else if (v == 8) {
                    othAug++;

                }
                else if (v == 9) {
                    othSep++;

                }
                else if (v == 10) {
                    othOct++;

                }
                else if (v == 11) {
                    othNov++;

                }
                else if (v == 12) {
                    othDec++;

                }


                //crm++;
            }







        }

        var ctx = document.getElementById("myBarChart");
        var myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ["Jan", "feb", "Mar", "Apr", "May", "Jun", "Aug", "Sep", "Oct", "Nov", "Dec"],
                datasets: [{
                    label: 'Accidents',
                    data: [accJan, accFeb, accMar, accApr, accMay, accJune, accJul, accAug, accSep, accOct, accNov, accDec],
                    backgroundColor: [
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                        'rgba(246, 33, 13, 0.3)',
                    ],
                    borderColor: [
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                        'rgba(246, 33, 13, 1)',
                    ],
                    borderWidth: 1
                }, {
                    label: 'Crime',
                    data: [crmJan, crmFeb, crmMar, crmApr, crmMay, crmJune, crmJul, crmAug, crmSep, crmOct, crmNov, crmDec],
                    backgroundColor: [
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',
                        'rgba(246, 223, 18, 0.3)',

                    ],
                    borderColor: [
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                        'rgba(246, 223, 18, 1)',
                    ],
                    borderWidth: 1
                },
                {
                    label: 'Hazards',
                    data: [hazJan, hazFeb, hazMar, hazApr, hazMay, hazJune, hazJul, hazAug, hazSep, hazOct, hazNov, hazDec],
                    backgroundColor: [
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                        'rgba(67, 170, 139, 0.3)',
                    ],
                    borderColor: [
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',
                        'rgba(67, 170, 139, 1)',

                    ],
                    borderWidth: 1
                }, {
                    label: 'Others',
                    data: [othJan, othFeb, othMar, othApr, othMay, othJune, othJul, othAug, othSep, othOct, othNov, othDec],
                    backgroundColor: [
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',
                        'rgba(4, 16, 11, 0.3)',

                    ],
                    borderColor: [
                        'rgba(4, 16, 11, 1)',

                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',
                        'rgba(4, 16, 11, 1)',

                    ],
                    borderWidth: 1
                }


                ]
            },
            options: {
                responsive: false,
                scales: {
                    xAxes: [{
                        ticks: {
                            maxRotation: 90,
                            minRotation: 80
                        }
                    }],
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                }
            }
        });


    }


/*
    function gotDataCrime(data) {
        var scores = data.val();
        var keys = Object.keys(scores);
        //  SimpleDateFormat sdf;
        var date;
        var v;
        var y;
        for (var i = 0; i < keys.length; i++) {

            //sdf = new SimpleDateFormat("d/MM/yy");
            //date = sdf.format(new Date());

            var k = keys[i];
            var subtype = scores[k].subtype;
            var date = scores[k].date;

            var myDate = Date.parse(date);
            var dateFinal = new Date(myDate);
            y = dateFinal.getFullYear();
            v = dateFinal.getDay();
            //v = datexx.format('M');

            console.log("month " + v);
            console.log(y)

            if (subtype == "crime") {

                //  const myDate = Date.parse(date);
                //    var dateFinal = new Date(myDate);
                //  console.log(myDate);

                //var parts = myDate.toString().split('/');
                //console.log(parts);
                //var mydate = parts[1];

                console.log(y)
                if (v == 1) {
                    crmJan++;

                }
                else if (v == 2) {
                    crmFeb++;

                }
                else if (v == 3) {
                    crmMar++;

                }
                else if (v == 4) {
                    crmApr++;

                }
                else if (v == 5) {
                    console.log(crmMay);
                    crmMay++;

                }
                else if (v == 6) {

                    crmJune++;
                    console.log("crime june" + crmJune);

                }
                else if (v == 7) {
                    crmJul++;

                }
                else if (v == 8) {
                    crmAug++;

                }
                else if (v == 9) {
                    crmSep++;

                }
                else if (v == 10) {
                    crmOct++;

                }
                else if (v == 11) {
                    crmNov++;

                }
                else if (v == 12) {
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

    }*/



function errData(err) {

    console.log(err);
}
