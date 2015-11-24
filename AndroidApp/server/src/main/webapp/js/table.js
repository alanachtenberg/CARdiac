
//GOOGLE AUTH
var CLIENT_ID = "775668101465-m8jfmuet88enh2hofbiub68j83a8l007.apps.googleusercontent.com";

var SCOPES =  'https://www.googleapis.com/auth/userinfo.email';



var TIMEOUT = 10000;
var EcgData = ["default",0,false,false,false];

function loadECGdata(){
    var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
        if (!resp.code) {
               gapi.client.cardiacApi.ecgApi.listECG().execute(function(resp) {
                   if (resp.code == 401){
                        $(".alert").show();
                   }else{
                        $(".alert").hide();

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
                   }
                   } );
               setTimeout(function(){loadECGdata()},TIMEOUT);
        }else{
            signIn()
        }
    });

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
    gapi.auth.authorize({client_id:CLIENT_ID, scope: SCOPES, immediate: false}, initTable);
 }

initECGApi = function() {
    var ROOT = "https://cardiac-404.appspot.com/_ah/api"
    callback = function(){
        gapi.client.load('cardiacApi', 'v1',signIn, ROOT);
    }
    gapi.client.load('oauth2', 'v2', callback); //callback loads the next api
};
