
//GOOGLE AUTH
var CLIENT_ID = "775668101465-m8jfmuet88enh2hofbiub68j83a8l007.apps.googleusercontent.com";
var SCOPES =  'https://www.googleapis.com/auth/userinfo.email';

$(document).ready(function(){
    $("#sign_up_button").click(function(){
    var thisObject = $(this);
        googleSignIn( function(){
            gapi.client.oauth2.userinfo.get().execute(function(resp) {
                if (!resp.code) {
                    gapi.client.cardiacApi.userApi.registerUser().execute(function(resp){
                        if (resp.code == 409){
                            thisObject.text("User is already registered");
                            thisObject.hide(2000);
                        }else{
                            thisObject.text("Registration Complete")
                            thisObject.hide(2000);
                        }
                    });
                }
            });
        });
    });
});

 googleSignIn = function (callback){
    gapi.auth.authorize({client_id:CLIENT_ID, scope: SCOPES, immediate: false}, callback);
 }

initApi = function() {
    var ROOT = "https://cardiac-404.appspot.com/_ah/api"
    callback = function(){
        gapi.client.load('cardiacApi', 'v1',null, ROOT);
    }
    gapi.client.load('oauth2', 'v2', callback); //callback loads the next api
};
