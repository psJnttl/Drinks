import React from 'react';
import PropTypes from 'prop-types';
import {Table} from 'react-bootstrap';
import axios from 'axios';

class EventLog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {eventLog: [], searchName: ""  }
    this.fetchEventLog = this.fetchEventLog.bind(this);
    this.setEventLogList = this.setEventLogList.bind(this);
    this.parseDate = this.parseDate.bind(this);
    this.onChangeSearchName = this.onChangeSearchName.bind(this);
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

  parseDate(java8LocalDateTime) {
    console.log(java8LocalDateTime);
    const year = java8LocalDateTime.year;
    const month = java8LocalDateTime.monthValue -1;
    const day = java8LocalDateTime.dayOfMonth;
    const hour = java8LocalDateTime.hour;
    const minute = java8LocalDateTime.minute;
    const second = java8LocalDateTime.second;
    const millis = java8LocalDateTime.nano / 1000000;
    const timeStamp = new Date(year, month, day, hour, minute, second, millis);
    return timeStamp.toString();
  }

  setEventLogList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, date: this.parseDate(item.date), action: item.action, targetEntity: item.targetEntity,
                targetId: item.targetId, targetName: item.targetName, username: item.username }) );
    this.setState({eventLog: theList});
  }

  onChangeSearchName(e) {
    this.setState({searchName: e.target.value});
  }

  checkDuplicate(list, item) {
    const index = _.findIndex(list, item);
    if (-1 === index) {
      return false;
    }
    return true;
  }

  concatenateSearchResults(target, source) {
    const result1 = source.filter( item => !this.checkDuplicate(target, item));
    const result = _.concat(target, result1);
    return result;
  }

  componentDidMount() {
    this.fetchEventLog();
  }

  render() {
    const byAction = this.state.eventLog.filter(item => item.action.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const byTargetEntity = this.state.eventLog.filter(item => item.targetEntity.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const byTargetName = this.state.eventLog.filter(item => item.targetName.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const byUsername = this.state.eventLog.filter(item => item.username.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const concat1 = this.concatenateSearchResults(byAction, byTargetEntity);
    const concat2 = this.concatenateSearchResults(concat1, byTargetName);
    const concat3 = this.concatenateSearchResults(concat2, byUsername);
    const sorted = _.orderBy(concat3, [function(l) { return l.id }], ['asc']);
    const dataRows = concat3.map((row, index) =>
      <tr key={index}>
        <td>{row.id}</td>
        <td>{row.action}</td>
        <td>{row.date}</td>
        <td>{row.targetEntity}</td>
        <td>{row.targetId}</td>
        <td>{row.targetName}</td>
        <td>{row.username}</td>
      </tr>
    );
    return (
      <div>
        <ul style={{'display': 'flex', 'listStyleType': 'none', 'marginTop': '10px'}}>
          <li>
            <input
              className="searchinput"
              type="text"
              placeholder="search"
              onChange={ this.onChangeSearchName }
              value={this.state.searchName}
              autoComplete="off"
              title="by action, target entity, target name or or username"
            />
          </li>
        </ul>
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
