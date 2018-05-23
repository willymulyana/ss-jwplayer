
# ss-jwplayer
_JWPlayer for RN Android_

## Getting started

`$ yarn add https://github.com/willymulyana/ss-jwplayer.git`

### Mostly automatic installation

`$ react-native link ss-jwplayer`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNSsJwplayerPackage;` to the imports at the top of the file
  - Add `new RNSsJwplayerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':ss-jwplayer'
  	project(':ss-jwplayer').projectDir = new File(rootProject.projectDir, 	'../node_modules/ss-jwplayer/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':ss-jwplayer')
  	```
4. Put your JWPlayer licence key to `node_modules\ss-jwplayer\android\src\main\AndroidManifest.xml`. Replace the `YOUR_LICENSE_KEY`


## Use Example

1. Clone this repo
2. Go to `Example` folder
    ```
      $ cd Example
  	```
3. Install required packages
    ```
      $ yarn
  	```
4. Run the Example app
    ```
      $ react-native run-android
  	```

## Usage
```javascript
import RNSsJwplayer from 'ss-jwplayer';

RNSsJwplayer.playVideo({
  streamUrl:'https://www.rmp-streaming.com/media/bbb-360p.mp4', // the video url
  time:20000, // progress video to certain time [in millis]
  vastUrl:'', // for showing ads in video (required JWPlayer premium license)
  // vastUrl:'https://s3-ap-southeast-1.amazonaws.com/ss-static-api/v1/jwplayerads/vmap-prerolls.xml', // sample
  onError:function (e) { ... },   // when error happen when playing video
  onPlay:function (e) { ... },
  onPause:function (e) { ... },
  onTick:function () { ... },     // per minute event
  onComplete:function () { ... }, // when complete playing the video
  onClose:function () { ... }     // when close the player
});
```
  
