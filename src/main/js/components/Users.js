import React from 'react';
import PropTypes from 'prop-types';
import axios from 'axios';
import _ from 'lodash';
import {Button, Col, Glyphicon, Table} from 'react-bootstrap';
import SimpleInformationModal from './SimpleInformationModal';
import UserModal from './UserModal';
import SimpleConfirmationModal from './SimpleConfirmationModal';
import Pages from './Pages';

class Users extends React.Component {
  constructor(props) {
    super(props);
    this.state = {accounts: [], infoModalVisible: false, infoModalData: {},
      addModalVisible: false, roles: [], editModalVisible: false, user: {},
      delConfirmationVisible: false, searchName: "",
    }
    this.fetchUsers = this.fetchUsers.bind(this);
    this.setAccountsData = this.setAccountsData.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
    this.openAddModal = this.openAddModal.bind(this);
    this.closeAddModal = this.closeAddModal.bind(this);
    this.saveNewUser = this.saveNewUser.bind(this);
    this.fetchRoles = this.fetchRoles.bind(this);
    this.setRolesData = this.setRolesData.bind(this);
    this.openEditModal = this.openEditModal.bind(this);
    this.closeEditModal = this.closeEditModal.bind(this);
    this.saveOldUser = this.saveOldUser.bind(this);
    this.setDeleteConfirmModalVisible = this.setDeleteConfirmModalVisible.bind(this);
    this.deleteReply = this.deleteReply.bind(this);
    this.deleteUser = this.deleteUser.bind(this);
    this.onChangeSearchName = this.onChangeSearchName.bind(this);
  }

  fetchUsers() {
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

  fetchRoles() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    axios.get('api/roles', config)
         .then(function (response) {
              self.setRolesData(response.data);
         })
        .catch(function (response) {
          self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Fetch roles failed",
             notification: "Could not get the list of roles from server!",
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

  setRolesData(data) {
    const rolesData = data.map( item =>
      _.assign( {}, {id: item.id, name: item.name})  );
      this.setState({roles: rolesData});
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

  openAddModal() {
    this.setState({addModalVisible: true})
  }

  closeAddModal() {
    this.setState({addModalVisible: false});
  }

  saveNewUser(user) {
    this.closeAddModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const command = {
      username: user.username,
      password: user.password,
      roles: _.concat([], user.roles)
    };
    const self = this;
    axios.post('api/accounts', command, config)
         .then(function (response) {
              self.fetchUsers();
         })
        .catch(function (response) {
          let info;
          if (409 === response.response.status) {
            info = {header:"failedModalHeader",
            title:"Add user failed",
           notification: "User with same username exists!",
           name: ""}
          }
          else {
            info = {header:"failedModalHeader",
            title:"Add user failed",
           notification: "Could not get add new user!",
           name: ""}
          }
          self.setState({infoModalVisible: true,
              infoModalData: info });
        });
  }

  saveOldUser(user) {
    this.closeEditModal();
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const command = {
      username: user.username,
      newPassword: user.password,
      roles: _.concat([], user.roles)
    };
    const self = this;
    axios.put('api/accounts', command, config)
         .then(function (response) {
              self.fetchUsers();
         })
        .catch(function (response) {
          self.setState({infoModalVisible: true,
              infoModalData: {header:"failedModalHeader",
              title:"Modify user failed",
             notification: "Could not modify user on server!",
             name: ""} });
        });
  }

  openEditModal(item) {
    this.setState({editModalVisible: true, user: item});
  }

  closeEditModal() {
    this.setState({editModalVisible: false, user: {} });
  }

  setDeleteConfirmModalVisible(item) {
    this.setState({delConfirmationVisible: true, user: item});
  }

  deleteReply(answer) {
    if (true === answer) {
      this.deleteUser(this.state.user);
    }
    this.setState({delConfirmationVisible: false, user: {} });
  }

  deleteUser(user) {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const self = this;
    const url = 'api/accounts/' + user.username;
    axios.delete(url, config)
         .then(function (response) {
              self.fetchUsers();
         })
        .catch(function (response) {
          self.setState({infoModalVisible: true,
            infoModalData: {header:"failedModalHeader",
            title:"Delete user failed",
            notification: "Couldn't delete user on server!",
            name: ""} });

        });
  }

  onChangeSearchName(e) {
    this.setState({searchName: e.target.value});
  }

  userHasRole(user, name) {
    const roleIndex = _.findIndex(user.roles, (u) => (u.name.toLowerCase().includes(name)) );
    if (-1 === roleIndex) {
      return false;
    }
    return true;
  }

  concatenateSearchResults(target, source) {
    const result1 = source.filter( item => !this.checkDuplicate(target, item));
    const result = _.concat(target, result1);
    return result;
  }

  checkDuplicate(list, item) {
    const index = _.findIndex(list, item);
    if (-1 === index) {
      return false;
    }
    return true;
  }

  rowTool(item, index, self) {
    return (
      <tr key={index}>
        <td>{item.username}</td>
        <td>{self.listRoles(item.roles)}</td>
        <td>
          <Button bsStyle="danger" bsSize="small" onClick={() => self.setDeleteConfirmModalVisible(item)} title="delete user"><Glyphicon glyph="trash"/></Button>
          <Button bsStyle="warning" bsSize="small" onClick={() => self.openEditModal(item)} title="edit user"><Glyphicon glyph="pencil"/></Button>
        </td>
      </tr>
    );
  }

  componentDidMount() {
    this.fetchUsers();
    this.fetchRoles();
  }

  render() {
    const self = this;
    let userModal;
    if (true === this.state.addModalVisible) {
      userModal =
      <UserModal
        modalOpen={this.state.addModalVisible}
        close={this.closeAddModal}
        title="Add new user"
        save={this.saveNewUser}
        roles={this.state.roles}
      />
    }
    else if (true === this.state.editModalVisible) {
      userModal =
      <UserModal
        modalOpen={this.state.editModalVisible}
        close={this.closeEditModal}
        title="Edit user"
        save={this.saveOldUser}
        roles={this.state.roles}
        user={this.state.user}
      />
    }
    else {
      userModal = null;
    }
    const filteredByUsername = this.state.accounts.filter(item => item.username.toLowerCase().includes(this.state.searchName.toLowerCase()));
    const filteredByRole = this.state.accounts.filter(item => this.userHasRole(item, this.state.searchName.toLowerCase()));
    const concatenated = this.concatenateSearchResults(filteredByUsername, filteredByRole);
    const sorted = _.orderBy(concatenated, [function(u) { return u.username.toLowerCase(); }], ['asc']);

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
        <SimpleConfirmationModal
          modalOpen={this.state.delConfirmationVisible}
          title="Delete user"
          question={"Are you sure you want to delete " + this.state.user.username}
          reply={this.deleteReply}
          header="failedModalHeader"
        />

        {userModal}

        <Col sm={8}>
          <ul style={{'display': 'flex', 'listStyleType': 'none', 'marginTop': '10px'}}>
            <li>
              <Button bsStyle="success" onClick={ () => this.openAddModal() } title="add user"><Glyphicon glyph="plus"/></Button>
            </li>
            <li>
              <input
                className="searchinput"
                type="text"
                placeholder="search by username or role"
                onChange={ this.onChangeSearchName }
                value={this.state.searchName}
              autoComplete="off" />
            </li>
          </ul>
          <Pages
            items={sorted}
            itemToList={['username','roles','actions']}
            dataTool={this.rowTool}
            parentRef={self}
          />
        </Col>
      </div>
    );
  }
}
Users.PropTypes = {}
Users.defaultProps = {}
export default Users;
