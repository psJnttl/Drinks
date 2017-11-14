import React from 'react';
import PropTypes from 'prop-types';

class Drinks extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    return (
      <h4>Drinks listed here</h4>
    );
  }
}
Drinks.PropTypes = {}
Drinks.defaultProps = {}
export default Drinks;
