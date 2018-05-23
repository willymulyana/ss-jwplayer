/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Button,
} from 'react-native';
import RNSsJwplayer from 'ss-jwplayer';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

type Props = {};
export default class App extends Component<Props> {

  onError(e) {
    RNSsJwplayer.showToast('onError');
  }

  onPlay(e) {
    RNSsJwplayer.showToast('onPlay');
  }

  onPause(e) {
    RNSsJwplayer.showToast('onPause');
  }

  onTick(e) {
    RNSsJwplayer.showToast('onTick');
  }

  onComplete(e) {
    RNSsJwplayer.showToast('onComplete');
  }

  onClose(e) {
    RNSsJwplayer.showToast('onClose');
  }

  render() {
    return (
      <View style={styles.container}>
        <Button title='Show Toast' onPress={()=>{
          RNSsJwplayer.showToast('test message');
        }} />
        <View style={{height:10}} />
        <Button title='Play Video' onPress={()=>{
          RNSsJwplayer.playVideo({
            streamUrl:'https://www.rmp-streaming.com/media/bbb-360p.mp4',
            time:20000,
            vastUrl:'',
            // vastUrl:'https://s3-ap-southeast-1.amazonaws.com/ss-static-api/v1/jwplayerads/vmap-prerolls.xml',
            onError:this.onError,
            onPlay:this.onPlay,
            onPause:this.onPause,
            onTick:this.onTick,
            onComplete:this.onComplete,
            onClose:this.onClose
          });
        }} />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'white',
  },
  player: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
