const { NativeModules, DeviceEventEmitter, Alert } = require('react-native');
const { RNSsJwplayer } = NativeModules;

module.exports = {
  ...RNSsJwplayer,

  playVideo: function playVideo(params:PlayVideoParams) {

  	// set listeners
  	if(this.onErrorListener) this.onErrorListener.remove();
  	if(params.onError) {
  		this.onErrorListener = DeviceEventEmitter.addListener('JWPlayerErrorEvent', params.onError);
  	}

  	if(this.onPlayListener) this.onPlayListener.remove();
  	if(params.onPlay) {
  		this.onPlayListener = DeviceEventEmitter.addListener('JWPlayerPlayEvent', params.onPlay);
  	}

  	if(this.onPauseListener) this.onPauseListener.remove();
  	if(params.onPause) {
  		this.onPauseListener = DeviceEventEmitter.addListener('JWPlayerPauseEvent', params.onPause);
  	}

  	if(this.onTickListener) this.onTickListener.remove();
  	if(params.onTick) {
  		this.onTickListener = DeviceEventEmitter.addListener('JWPlayerTickEvent', params.onTick);
  	}

  	if(this.onCompleteListener) this.onCompleteListener.remove();
  	if(params.onComplete) {
  		this.onCompleteListener = DeviceEventEmitter.addListener('JWPlayerCompleteEvent', params.onComplete);
  	}

  	if(this.onCloseListener) this.onCloseListener.remove();
  	if(params.onClose) {
  		this.onCloseListener = DeviceEventEmitter.addListener('JWPlayerCloseEvent', params.onClose);
  	}

    return RNSsJwplayer.playVideo(params.streamUrl, params.time, params.vastUrl);
  },

  showToast: function showToast(message) {
  	return RNSsJwplayer.showToast(message);
  }

}



// inner classes
class PlayVideoParams {

	streamUrl:string = '';
	time:number = 0;
	vastUrl:string = '';

	onError:Function = null;
	onPlay:Function = null;
	onPause:Function = null;
	onTick:Function = null;
	onComplete:Function = null;
	onClose:Function = null;
}