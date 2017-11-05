import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';

class Logout extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
    this.method = this.method.bind(this);
  }
  method() {}
  render() {
    const self = this;
    if (this.props.authenticated) {
      axios.get('/logout')
           .then(function (response) {
             self.props.changeAuthState(false);
           })
          .catch(function (response) {
            self.props.changeAuthState(true);
          });
    }
    return (
      null
    );
  }
}
Logout.PropTypes = {
  authenticated: PropTypes.bool.isRequired,
  changeAuthState: PropTypes.func.isRequired,
}
Logout.defaultProps = {}
export default Logout;
