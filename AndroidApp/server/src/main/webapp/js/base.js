/**
 * @fileoverview
 * Provides methods for the Hello Endpoints sample UI and interaction with the
 * Hello Endpoints API.
 */

/** google global namespace for Google projects. */
var google = google || {};

/** appengine namespace for Google Developer Relations projects. */
google.appengine = google.appengine || {};

/** samples namespace for App Engine sample code. */
google.appengine.samples = google.appengine.samples || {};

/** hello namespace for this sample. */
google.appengine.samples.hello = google.appengine.samples.hello || {};

/**
 * Prints a greeting to the greeting log.
 * param {Object} greeting Greeting to print.
 */
google.appengine.samples.hello.print = function(stringBean) {
    var element = document.createElement('div');
    element.classList.add('row');
    element.innerHTML = stringBean.text;
    document.querySelector('#outputLog').appendChild(element);
};

/**
 * Gets a numbered greeting via the API.
 * @param {string} id ID of the greeting.
 */
google.appengine.samples.hello.getGreeting = function(id) {
    gapi.client.cardiacApi.main.getGreeting({'id': id}).execute(
        function(resp) {
            if (!resp.code) {
                google.appengine.samples.hello.print(resp);
            }
        });
};

/**
 * Lists greetings via the API.
 */
google.appengine.samples.hello.listGreeting = function() {
    gapi.client.cardiacApi.main.listGreeting().execute(
        function(resp) {
            if (!resp.code) {
                var output=document.querySelector('#outputLog');
                var child= output.firstElementChild
                while(output.childElementCount>0){
                    output.removeChild(output.firstElementChild);
                    if(child!=null) {
                        child = child.nextElementSibling;
                    }
                }
                resp.items = resp.items || [];
                for (var i = 0; i < resp.items.length; i++) {
                    google.appengine.samples.hello.print(resp.items[i]);
                }
            }
        });
};


/**
 * Lists greetings via the API.
 */
google.appengine.samples.hello.multiplyGreeting = function() {
    gapi.client.cardiacApi.main.multiply().execute(
        function(resp) {
            if (!resp.code) {
                var output=document.querySelector('#outputLog');
                var child= output.firstElementChild
                while(output.childElementCount>0){
                    output.removeChild(output.firstElementChild);
                    if(child!=null) {
                        child = child.nextElementSibling;
                    }
                }
                resp.items = resp.items || [];
                for (var i = 0; i < resp.items.length; i++) {
                    google.appengine.samples.hello.print(resp.items[i]);
                }
            }
        });
};

/**
 * Enables the button callbacks in the UI.
 */
google.appengine.samples.hello.enableButtons = function() {
    var getGreeting = document.querySelector('#getGreeting');
    getGreeting.addEventListener('click', function(e) {
        google.appengine.samples.hello.getGreeting(
            document.querySelector('#id').value);
    });

    var listGreeting = document.querySelector('#listGreeting');
    listGreeting.addEventListener('click',
        google.appengine.samples.hello.listGreeting);
    var multiplyGreeting = document.querySelector('#multiplyGreeting');
    listGreeting.addEventListener('click',
        google.appengine.samples.hello.multiplyGreeting);

};
/**
 * Initializes the application.
 * @param {string} apiRoot Root of the API's path.
 */
google.appengine.samples.hello.init = function(apiRoot) {
    // Loads the OAuth and helloworld APIs asynchronously, and triggers login
    // when they have completed.
    var apisToLoad;
    var callback = function() {
        if (--apisToLoad == 0) {
            google.appengine.samples.hello.enableButtons();
        }
    }

    apisToLoad = 1; // must match number of calls to gapi.client.load()
    gapi.client.load('cardiacApi', 'v1', callback, apiRoot);
};