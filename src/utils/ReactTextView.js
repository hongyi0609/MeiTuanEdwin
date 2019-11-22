
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
        this._onStateChanged = this._onStateChanged.bind(this)
    }

    _onClicked = (event) => {
        if(!this.props.onClicked){
            return
        }
        //使用event.nativeEvent.msg获取原生层传递的数据
        this.props.onClicked(event.nativeEvent.msg);
    }

    _onStateChanged(event: Event) {
        if (!this.props.onStateChanged) {
            return;
        }
        this.props.onStateChanged(event.nativeEvent.state);
    }

    render() {
        return (
            <RCTReactTextView 
                {...this.props}
                onStateChanged={this._onStateChanged}
                onClicked={this._onClicked} />
            );
    }
}
ReactTextView.propTypes = {
    title: PropTypes.string,
    onStateChanged: PropTypes.func,
    onClicked: PropTypes.func,
    ...ViewPropTypes,
}

// 导出原生View  原生View名称 、 属性类型
const RCTReactTextView = requireNativeComponent('ReactTextView', ReactTextView,{nativeOnly:{onClicked:true}}/**
 * onClicked事件只在原生组件中生效
 */)
module.exports = ReactTextView