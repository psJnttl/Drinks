import React from 'react';
import PropTypes from 'prop-types';
import {Button, Table} from 'react-bootstrap';
import Pages from './Pages';
import SimpleInformationModal from './SimpleInformationModal';
import {concatenateSearchResults} from './util';
import NetworkApi from './NetworkApi';
import _ from 'lodash';

class EventLog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {eventLog: [], searchName: "",
    infoModalVisible: false, infoModalData: {},
    dateStartStr: "", dateStart: null,
    dateEndStr: "", dateEnd: null,
    }
    this.fetchEventLog = this.fetchEventLog.bind(this);
    this.setEventLogList = this.setEventLogList.bind(this);
    this.parseDate = this.parseDate.bind(this);
    this.onChangeSearchName = this.onChangeSearchName.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.onChangeDateStart = this.onChangeDateStart.bind(this);
    this.onChangeDateEnd = this.onChangeDateEnd.bind(this);
  }

  fetchEventLog() {
    const self = this;
    NetworkApi.get('api/logentries')
         .then(function (response) {
            self.setEventLogList(response);
         })
         .catch(function (response) {
           self.setState({infoModalVisible: true,
               infoModalData: {header:"failedModalHeader",
               title:"Fetch Event Log failed",
              notification: "Could not get the list of events from server!",
              name: ""} });
         });
  }

  parseDate(java8LocalDateTime) {
    const year = java8LocalDateTime.year;
    const month = java8LocalDateTime.monthValue -1;
    const day = java8LocalDateTime.dayOfMonth;
    const hour = java8LocalDateTime.hour;
    const minute = java8LocalDateTime.minute;
    const second = java8LocalDateTime.second;
    const millis = java8LocalDateTime.nano / 1000000;
    const timeStamp = new Date(year, month, day, hour, minute, second, millis);
    return timeStamp;
  }

  setEventLogList(data) {
    const theList = data.map( item =>
       _.assign({}, {id: item.id, dateStr: this.parseDate(item.date).toString(),
                date: this.parseDate(item.date), action: item.action,
                targetEntity: item.targetEntity, targetId: item.targetId,
                targetName: item.targetName, username: item.username }) );
    this.setState({eventLog: theList});
  }

  onChangeSearchName(e) {
    this.setState({searchName: e.target.value});
  }

  rowTool(item, index, self) {
    return (
      <tr key={index}>
        <td>{item.id}</td>
        <td>{item.action}</td>
        <td>{item.dateStr}</td>
        <td>{item.targetEntity}</td>
        <td>{item.targetId}</td>
        <td>{item.targetName}</td>
        <td>{item.username}</td>
      </tr> );
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false, infoModalData: {}});
  }

  onChangeDateStart(e) {
    let dateStart = null;
    const startDateReady = new RegExp("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    if (true === startDateReady.test(e.target.value) ) {
      console.log("we have valid start date")
      dateStart = new Date(e.target.value);
    }
    this.setState({dateStartStr: e.target.value, dateStart: dateStart});
  }

  onChangeDateEnd(e) {
    let dateEnd = null;
    const endDateReady = new RegExp("^[0-9]{4}-[0-9]{2}-[0-9]{2}$");
    if (true === endDateReady.test(e.target.value) ) {
      console.log("we have valid end date")
      dateEnd = new Date(e.target.value);
    }
    this.setState({dateEndStr: e.target.value, dateEnd: dateEnd});
  }

  filterByDates(itemArray) {
    if (null !== this.state.dateStart && null === this.state.dateEnd ) {
      const result = itemArray.filter(item => item.date >= this.state.dateStart);
      return result;
    }
    else if (null === this.state.dateStart && null !== this.state.dateEnd ) {
      const adjusted = this.state.dateEnd.getTime() + 86400000;
      const dateEnd = new Date(adjusted);
      const result = itemArray.filter(item => item.date <= dateEnd);
      return result;
    }
    else if (null !== this.state.dateStart && null !== this.state.dateEnd ) {
      const adjusted = this.state.dateEnd.getTime() + 86400000;
      const dateEnd = new Date(adjusted);
      const result = itemArray.filter(item => (item.date >= this.state.dateStart && item.date <= dateEnd));
      return result;
    }
    return itemArray;
  }

  filterByDates2(itemArray) {
    let startPred;
    if (null !== this.state.dateStart) {
      const start = this.state.dateStart;
      startPred = function(item) { return item.date >= start};
    }
    else {
      startPred = function(item) { return true};
    }
    let endPred;
    if (null !== this.state.dateEnd) {
      const end = this.state.dateEnd;
      endPred = function(item) { return item.date <= end};
    }
    else {
      endPred = function(item) { return true};
    }
    const result = itemArray.filter(item => (startPred(item) && endPred(item)));
    return result;
  }

  componentDidMount() {
    this.fetchEventLog();
  }

  render() {
    const self = this;
    const byAction = this.state.eventLog.filter(item => item.action.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const byTargetEntity = this.state.eventLog.filter(item => item.targetEntity.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const byTargetName = this.state.eventLog.filter(item => item.targetName.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const byUsername = this.state.eventLog.filter(item => item.username.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const concat1 = concatenateSearchResults(byAction, byTargetEntity);
    const concat2 = concatenateSearchResults(concat1, byTargetName);
    const concat3 = concatenateSearchResults(concat2, byUsername);
    const dateFiltered = this.filterByDates(concat3);
    const sorted = _.orderBy(dateFiltered, [function(l) { return l.id }], ['desc']);

    return (
      <div>
        <SimpleInformationModal
          modalOpen={this.state.infoModalVisible}
          header={this.state.infoModalData.header}
          title={this.state.infoModalData.title}
          notification = {this.state.infoModalData.notification}
          notification2 =""
          name={this.state.infoModalData.name}
          reply={this.closeInfoModal} />

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
          <li style={{'marginLeft': '4px'}}>
            <input
              className="searchinput"
              type="text"
              placeholder="YYYY-MM-DD"
              onChange={this.onChangeDateStart}
              value={this.state.dateStartStr}
              autoComplete="off"
              title="date starting from"
            />
          </li>
          <li style={{'marginLeft': '4px'}}>
            <input
              className="searchinput"
              type="text"
              placeholder="YYYY-MM-DD"
              onChange={this.onChangeDateEnd}
              value={this.state.dateEndStr}
              autoComplete="off"
              title="date ending to"
            />
          </li>
        </ul>

        <Pages
          items={sorted}
          columnNames={['id','action','date','target entity','target id','target name','username']}
          dataTool={this.rowTool}
          parentRef={self}
        />
      </div>
    );
  }
}
EventLog.PropTypes = {}
EventLog.defaultProps = {}
export default EventLog;
