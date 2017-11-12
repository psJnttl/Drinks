import React from 'react';
import PropTypes from 'prop-types';

class EventLog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
    this.method = this.method.bind(this);
  }
  method() {}
  render() {
    return (
      <div>
        <h4>Event Log</h4>
      </div>
    );
  }
}
EventLog.PropTypes = {}
EventLog.defaultProps = {}
export default EventLog;
