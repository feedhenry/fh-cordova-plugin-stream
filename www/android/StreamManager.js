var exec = require("cordova/exec");

var dummysuccess=function(res){};
var dummyerror=function(res){};

function Audio() {
  this.successCallback={};
  this.errorCallback={};
};

Audio.prototype.action = function (params, successCallback, errorCallback) {
  this.params = params;

  switch (params.act) {
    case 'play':
      this.successCallback.play = successCallback;
      this.errorCallback.play= errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "play", [params.path]);
      break;
    case 'pause':
      this.successCallback.pause = successCallback;
      this.errorCallback.pause= errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "pause", []);
      break;
    case 'stop':
      this.successCallback.stop = successCallback;
      this.errorCallback.stop= errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "stop", []);
      break;
    case 'getvolume':
      this.successCallback.getvolume = successCallback;
      this.errorCallback.getvolume= errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "getvolume", []);
      break;
    case 'setvolume':
      this.successCallback.setvolume = successCallback;
      this.errorCallback.setvolume= errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "setvolume", [params.level]);
      break;
    case 'getbufferprogress':
      this.successCallback.getbufferprogress = successCallback;
      this.errorCallback.getbufferprogress = errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "getbufferprogress", []);
      break;
    case 'seek':
      this.successCallback.seek = successCallback;
      this.errorCallback.seek = errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "seek", [params.seekPosition]);
      break;
    case 'getcurrentposition':
      this.successCallback.getcurrentposition = successCallback;
      this.errorCallback.getcurrentposition = errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "getcurrentposition", []);
      break;
    case 'resume':
      this.successCallback.resume = successCallback;
      this.errorCallback.resume = errorCallback;
      exec(dummysuccess, dummyerror, "Stream", "resume", []);
      break;
    default:
      errorCallback("Data_Act: No Action defined");
      break;
  };


};

Audio.prototype.success = function(act, result) {

  if(this.successCallback[act])
  {
    this.successCallback[act](result);
  };
};

Audio.prototype.failure = function(act, result) {
  if(this.errorCallback[act])
  {
    this.errorCallback[act](result);
  };
};

cordova.addConstructor(function() {
                        
    if(!window.plugins)        {
        window.plugins = {};
    }
        
    window.plugins.stream = new Audio();
});