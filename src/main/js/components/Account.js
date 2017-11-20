import React from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormControl, FormGroup} from 'react-bootstrap';
import axios from 'axios';
import SimpleInformationModal from './SimpleInformationModal';
import _ from 'lodash';

class Account extends React.Component {
  constructor(props) {
    super(props);
    this.state = {oldPasswd:"", newPasswd:"", newPasswd2:"",
      infoModalVisible: false, infoModalData: {}
    }
    this.onChangeOldPasswd = this.onChangeOldPasswd.bind(this);
    this.onChangeNewPasswd = this.onChangeNewPasswd.bind(this);
    this.onChangeNewPasswd2 = this.onChangeNewPasswd2.bind(this);
    this.isFormValid = this.isFormValid.bind(this);
    this.changePassword = this.changePassword.bind(this);
    this.clearFormFields = this.clearFormFields.bind(this);
    this.closeInfoModal = this.closeInfoModal.bind(this);
  }

  onChangeOldPasswd(e) {
    this.setState({oldPasswd: e.target.value});
  }

  onChangeNewPasswd(e) {
    this.setState({newPasswd: e.target.value});
  }

  onChangeNewPasswd2(e) {
    this.setState({newPasswd2: e.target.value});
  }

  isFormValid() {
    if (this.state.oldPasswd.length < 4 || this.state.newPasswd.length < 4 ||
        this.state.newPasswd2.length < 4 ) {
      return false;
    }
    if (this.state.newPasswd !== this.state.newPasswd2) {
      return false;
    }
    return true;
  }

  changePassword() {
    const config = {headers: {'X-Requested-With': 'XMLHttpRequest'}};
    const command = {
      username: this.props.authState.username,
      oldPassword: this.state.oldPasswd,
      newPassword: this.state.newPasswd,
      roles: [{id:0, role: "dummy"}]
    };
    const self = this;
    axios.put('/api/account', command, config)
         .then(function (response) {
           console.log("Password changed successfully.")
           self.setState({infoModalVisible: true,
               infoModalData: {header:"successModalHeader",
               title:"Password changed",
              notification: "Password changed successfully.",
              name: ""} });
         })
         .catch(function (response) {
           let infoData = {
             header:"failedModalHeader",
             title:"Password not changed!",
             notification: "Failed to change password!",
             name: ""
           }
           if (403 === response.response.status) {
             infoData['notification'] = "Wrong password for " + self.props.authState.username
           }
           self.setState({infoModalVisible: true, infoModalData: infoData });
         });
    return
  }

  clearFormFields() {
    this.setState({oldPasswd:"", newPasswd:"", newPasswd2:"", });
  }

  closeInfoModal() {
    this.clearFormFields();
    this.setState({infoModalVisible: false, infoModalData: {}});
  }

  render() {
    const username = this.props.authState.username;
    return (
      <div>
        <h5>Change password for {username}</h5>
        <Col sm={4}>
          <Form>
            <FormGroup controlId="formInput">
              <input
                className="searchinput"
                type="password"
                placeholder="old password"
                onChange={ this.onChangeOldPasswd }
                value={this.state.oldPasswd}
                autoComplete="off"
              />
            </FormGroup>
            <FormGroup controlId="formInput">
              <input
                className="searchinput"
                type="password"
                placeholder="new password"
                onChange={ this.onChangeNewPasswd }
                value={this.state.newPasswd}
                autoComplete="off"
              />
            </FormGroup>
            <FormGroup controlId="formInput">
              <input
                className="searchinput"
                type="password"
                placeholder="new password again"
                onChange={ this.onChangeNewPasswd2 }
                value={this.state.newPasswd2}
                autoComplete="off"
              />
            </FormGroup>
            <FormGroup controlId="buttons">
              <Button bsStyle="success" disabled={!this.isFormValid()} onClick={ () => this.changePassword() }>Change password</Button>
            </FormGroup>
          </Form>
        </Col>
        <SimpleInformationModal
          modalOpen={this.state.infoModalVisible}
          header={this.state.infoModalData.header}
          title={this.state.infoModalData.title}
          notification = {this.state.infoModalData.notification}
          notification2 =""
          name={this.state.infoModalData.name}
          reply={this.closeInfoModal} />
      </div>
    );
  }
}
Account.PropTypes = {
  authState: PropTypes.object.isRequired,
}
Account.defaultProps = {}
export default Account;
