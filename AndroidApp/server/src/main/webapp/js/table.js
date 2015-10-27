var TIMEOUT = 10000
var EcgData
function loadECGdata(){
    gapi.client.cardiacApi.eCG.listECG().execute(function(resp) {
        resp.items = resp.items || [];

        var ecgTable = $('EcgDataTable').DataTable();
        if (ecgTable.row.length > 0){
            table.clear();
        }
        for (var i = 0; i < resp.items.length; i++) {
            $('EcgDataTable').dataTable().fnAddData(
                [ resp.items[i].id,
                resp.items[i].heartRate,
                resp.items[i].problemOne,
                resp.items[i].problemTwo,
                resp.items[i].problemThree ]
                );
        }
    } );
    setTimeout(function(){loadECGdata()},TIMEOUT);
};

$('#ReloadEcgButton').click(loadECGdata);

function initTable(){
    $('#EcgDataTable').DataTable( {
                        data: EcgData,
                         columns: [
                            { title: "ID" },
                            { title: "Heart Rate" },
                            { title: "Problem One" },
                            { title: "Problem Two" },
                            { title: "Problem Three" }
                            ]
    } );
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
