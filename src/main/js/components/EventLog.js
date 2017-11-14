import React from 'react';
import PropTypes from 'prop-types';
import {Table} from 'react-bootstrap';
import axios from 'axios';

class EventLog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {eventLog: [], }
    this.fetchEventLog = this.fetchEventLog.bind(this);
    this.setEventLogList = this.setEventLogList.bind(this);
  }

  fetchEventLog() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/logentries', config)
         .then(function (response) {
            self.setEventLogList(response.data);
         })
         .catch(function (response) {
            console.log("log fetch failed")
         });
  }

  setEventLogList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, date: item.date, action: item.action, targetEntity: item.targetEntity,
                targetId: item.targetId, targetName: item.targetName, username: item.username }) );
    this.setState({eventLog: theList});
  }

  componentDidMount() {
    this.fetchEventLog();
  }

  render() {
    const dataRows = this.state.eventLog.map((row, index) => 
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.action}</td>
        <td></td>
        <td>{row.targetEntity}</td>
        <td>{row.targetId}</td>
        <td>{row.targetName}</td>
        <td>{row.username}</td>
      </tr>
    );
    return (
      <div>
        <h4>Event Log</h4>
        <Table bordered condensed hover>
          <thead>
            <tr>
              <th>id</th>
              <th>action</th>
              <th>date</th>
              <th>target entity</th>
              <th>target id</th>
              <th>target name</th>
              <th>username</th>
            </tr>
          </thead>
          <tbody>
            {dataRows}
          </tbody>
        </Table>

      </div>
    );
  }
}
EventLog.PropTypes = {}
EventLog.defaultProps = {}
export default EventLog;
