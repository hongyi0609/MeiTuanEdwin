
/**
 * JS调用原生的View组件,基于原生createViewManagers方法通信，这里对标ReactTextView
 */

'use strict';

var React = require('react')
var ReactNative = require('react-native');
var {
    requireNativeComponent,
    View
} = ReactNative;

import PropTypes from "prop-types";
var ViewPropTypes = require('ViewPropTypes');

class ReactTextView extends React.Component {
    constructor(props){
        super(props)
    }

    render() {
        return <RCTReactTextView {...this.props} />;
    }
}
ReactTextView.propTypes = {
    title: PropTypes.string,
    ...ViewPropTypes,
}

// 导出原生View  原生View名称 、 属性类型
const RCTReactTextView = requireNativeComponent('ReactTextView', ReactTextView)
module.exports = ReactTextView