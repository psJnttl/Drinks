import React from 'react';
import PropTypes from 'prop-types';

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
    this.method = this.method.bind(this);
  }
  method() {}
  render() {
    return (
      <h4>Users</h4>
    );
  }
}
Users.PropTypes = {}
Users.defaultProps = {}
export default Users;
