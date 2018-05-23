using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Ss.Jwplayer.RNSsJwplayer
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNSsJwplayerModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNSsJwplayerModule"/>.
        /// </summary>
        internal RNSsJwplayerModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNSsJwplayer";
            }
        }
    }
}
