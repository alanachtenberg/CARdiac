//GOOGLE AUTH
var CLIENT_ID =
    "775668101465-m8jfmuet88enh2hofbiub68j83a8l007.apps.googleusercontent.com";
/**
 * Scopes used by the application.
 * @type {string}
 */
var SCOPES =
    'https://www.googleapis.com/auth/userinfo.email';



var TIMEOUT = 10000;
var EcgData = ["default",0,false,false,false];

function loadECGdata(){
    gapi.client.cardiacApi.ecgApi.listECG().execute(function(resp) {
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

initTable = function(){
    var apiTable=$('#EcgDataTable').DataTable();
    apiTable.row.add(EcgData).draw();
    setTimeout(loadECGdata(), TIMEOUT);
};
/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */

 signIn = function (){
    gapi.auth.authorize({CLIENT_ID,
           scope: SCOPES, immediate: true},
        initTable);
 }

initECGApi = function() {
    var ROOT = "https://cardiac-404.appspot.com/_ah/api"
    callback = function(){
        gapi.client.load('cardiacApi', 'v1',signIn, ROOT);
    }
    gapi.client.load('oauth2', 'v2', callback); //callback loads the next api
};
