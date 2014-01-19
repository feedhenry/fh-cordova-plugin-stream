function StreamManager(){
  var play_success = null;
  var play_error = null;
  var pause_success = null;
  var pause_error = null;
  var stop_success = null;
  var stop_error = null;
}

StreamManager.prototype.play = function(options, success, fail){
  options.media_type = "audio";
  if(options.path && options.path.indexOf("http://") != -1){
    options.remote = true;
  } else {
    options.path = "www/" + options.path;
    options.remote = false;
  }
  if(options.controls){
    options.controls = "true";
  } else {
    options.controls = "false";
  }

  if(options.loop){
    options.loop = "true";
  }else {
    options.loop = "false";
  }
  this.play_success = success;
  this.play_error = fail;
  cordova.exec(null, null, "Stream", "play", [options]);
}

StreamManager.prototype.pause = function(options, success, fail){
  this.pause_success = success;
  this.pause_error = fail;
  cordova.exec(null, null, "Stream", "pause", [options]);
}

StreamManager.prototype.stop = function(options, success, fail) {
  this.stop_success = success;
  this.top_error = fail;
  cordova.exec(null, null, "Stream", "stop", [options]);
}

StreamManager.prototype.play_success = function(){
  if(this.play_success){
    this.play_success();
  }
}

StreamManager.prototype.play_error = function(){
  if(this.play_error){
    this.play_error();
  }
}

StreamManager.prototype.pause_success = function(){
  if(this.pause_success){
    this.pause_success();
  }
}

StreamManager.prototype.pause_error = function(){
  if(this.pause_error){
    this.pause_error();
  }
}

StreamManager.prototype.stop_success = function(){
  if(this.stop_success){
    this.stop_success();
  }
}

StreamManager.prototype.stop_error = function(){
  if(this.stop_error){
    this.stop_error();
  }
}


cordova.addConstructor(function() {
                        
    if(!window.plugins)        {
        window.plugins = {};
    }
        
    window.plugins.stream = new StreamManager();
});