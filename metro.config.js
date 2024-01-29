/**
 * Metro configuration for React Native
 * https://github.com/facebook/react-native
 *
 * @format
 */
const {getDefaultConfig} = require('@react-native/metro-config');
const {mergeConfig} = require('metro-config');

module.exports = mergeConfig(getDefaultConfig(__dirname),{
  transformer: {
    getTransformOptions: async () => ({
      transform: {
        experimentalImportSupport: false,
        inlineRequires: false,
      },
    }),
  },
  resolver: {
    platform: ['android','ios'],
  }
});
