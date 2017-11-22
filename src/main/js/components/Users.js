import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import _ from 'lodash';
import {Button, Col, Glyphicon, Pagination, Table} from 'react-bootstrap';
import SimpleInformationModal from './SimpleInformationModal';

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.state = {accounts: [], infoModalVisible: false, infoModalData: {},
    }
    this.fetchAccounts = this.fetchAccounts.bind(this);
    this.setAccountsData = this.setAccountsData.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
  }

  fetchAccounts() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/accounts', config)
         .then(function (response) {
              self.setAccountsData(response.data);
         })
        .catch(function (response) {
          self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Fetch users failed",
             notification: "Could not get the list of users from server!",
             name: ""} });
        });
  }

  setAccountsData(data) {
    const accountData = data.map( item =>
       _.assign({}, {
         username: item.username,
         password: "",
         roles: _.concat([], item.roles),
       }) );
       this.setState({accounts: accountData});
  }

  listRoles(roles) {
    var result = "";
    _.forEach(roles, function(value, key) {
      result += " " + value.name + ",";
    });
    const trimmed = _.trim(result, ", ");
    return trimmed;
  }

  closeInfoModal() {
    this.setState({infoModalVisible: false, infoModalData: {}});
  }

  componentDidMount() {
    this.fetchAccounts();
  }

  render() {
    const dataRows = this.state.accounts.map( (row, index) =>
      <tr key={index}>
        <td>{row.username}</td>
        <td>{this.listRoles(row.roles)}</td>
        <td>
          <Button bsStyle="danger" bsSize="small" onClick={() => this.setDeleteConfirmModalVisible(row)} title="delete user"><Glyphicon glyph="trash"/></Button>
          <Button bsStyle="warning" bsSize="small" onClick={() => this.openEditModal(row)} title="edit user"><Glyphicon glyph="pencil"/></Button>
        </td>
      </tr>
    );
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

        <Col sm={8}>
          <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add user"><Glyphicon glyph="plus"/></Button>
          <Table bordered condensed hover>
            <thead>
              <tr>
                <th>username</th>
                <th>roles</th>
                <th>actions</th>
              </tr>
            </thead>
            <tbody>
              {dataRows}
            </tbody>
          </Table>
        </Col>
      </div>
    );
  }
}
Users.PropTypes = {}
Users.defaultProps = {}
export default Users;
