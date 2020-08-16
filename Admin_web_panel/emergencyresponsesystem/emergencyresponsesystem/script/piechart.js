var fcc = new firebaseConnectClass();
var x = fcc.present();

var database = x;

var dbRef5 = database.ref().child('incident');

dbRef5.on('value', gotDataIncident, errData)
var acc = 0;
var crm = 0;
var fire = 0;
var oth = 0;
function gotDataIncident(data) {
    
    var scores = data.val();
    var keys = Object.keys(scores);
    for (var i = 0; i < keys.length; i++) {

        var k = keys[i];
        var subtype = scores[k].subtype;
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



    }
    //document.getElementById("accidents").innerHTML = acc;
    ///document.getElementById("crimes").innerHTML = crm;
    //document.getElementById("hazards").innerHTML = fire;
    var total = acc + crm + fire + oth;
    var tempA = acc / total;
    console.log("tempA "+tempA);
    var accidentPercent = tempA * 360;
    var tempB = crm / total;
    console.log("tempB " + tempB);

    var crimePercent = tempB * 360;
    var tempC = fire / total;
    console.log("tempC " + tempC);

    var hazardPercent = tempC * 360;
    var tempD = oth / total;
    console.log("tempD " + tempD);

    var otherPercent = tempD * 360;

    var roundAccident = Math.round(accidentPercent);
    var roundCrime = Math.round(crimePercent);
    var roundHazards = Math.round(hazardPercent);
    var roundOthers = Math.round(otherPercent);



    let ctx = document.getElementById('mychart').getContext('2d');
    let labels = ['Accident', 'Crime', 'Hazards', 'Others'];
    let colorbox = ['#f83640', '#F6DF12', '#43AA8B', '#253d2b'];


    let mychart = new Chart(ctx, {
        type: 'pie',
        data: {
            datasets: [{
                data: [roundAccident, roundCrime, roundHazards, roundOthers],
                backgroundColor: colorbox
            }],
            labels: labels
        },
        options: {
            responsive: true,
            legend: {
                position: 'bottom'
            },
            plugins: {
                datalabels: {
                    color: '#fff',
                    anchor: 'end',
                    align: 'start',
                    offset: -10,
                    borderWidth: 2,
                    borderColor: '#fff',
                    borderRadius: 25,
                    backgroundColor: (context) => {
                        return context.dataset.backgroundColor;

                    },
                    font: {
                        weight: 'bold',
                        size: '10'
                    },
                    formatter: (value) => {
                        return value + ' %';
                    }
                }
            }
        }

    })
}

function errData(err) {

    console.log(err);
}
