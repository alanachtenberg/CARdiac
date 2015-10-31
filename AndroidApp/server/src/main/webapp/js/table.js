var TIMEOUT = 10000;
var EcgData = ["default",0,false,false,false];

function loadECGdata(){
    gapi.client.cardiacApi.eCG.listECG().execute(function(resp) {
        resp.items = resp.items || [];

        var ecgTable = $('#EcgDataTable').DataTable();
        if (ecgTable.data().length > 0){
            ecgTable.clear().draw();
        }
        for (var i = 0; i < resp.items.length; i++) {
            ecgTable.row.add(
                [ resp.items[i].id,
                resp.items[i].heartRate,
                resp.items[i].problemOne,
                resp.items[i].problemTwo,
                resp.items[i].problemThree ]
                ).draw();
        }
    } );
    setTimeout(function(){loadECGdata()},TIMEOUT);
};

$('#ReloadEcgButton').click(loadECGdata);

function initTable(){
    var apiTable=$('#EcgDataTable').DataTable();
    apiTable.row.add(EcgData).draw();
    setTimeout(loadECGdata(), TIMEOUT);
};
/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */
initECGApi = function() {
    var ROOT = "https://cardiac-404.appspot.com/_ah/api"
    gapi.client.load('cardiacApi', 'v1',initTable, ROOT);
};
