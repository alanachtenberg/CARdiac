var EcgData;

function loadECGdata(){
    gapi.client.cardiacApi.eCG.listECG().execute(function(resp) {
        console.log(resp)
        resp.items = resp.items || [];
        EcgData = resp.items

        $('EcgDataTable').dataTable().fnClearTable();

        for (var i = 0; i < resp.items.length; i++) {
            $('#EcgDataTable').dataTable().fnAddData( [
                resp.items[i].id,
                resp.items[i].heartRate,
                resp.items[i].problemOne,
                resp.items[i].problemTwo,
                resp.items[i].problemThree,
                ]
              );

        }
    } );
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
//    gapi.client.cardiacApi.eCG.insertECG().execute(function resp){
//    }
};

/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */
initECGApi = function() {
    var ROOT = "https://cardiac-404.appspot.com/_ah/api"
    gapi.client.load('cardiacApi', 'v1',initTable, ROOT);
};
