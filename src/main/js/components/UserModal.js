import React from 'react';
import PropTypes from 'prop-types';
import _ from 'lodash';
import {Button, Col, Form, FormControl, FormGroup, Glyphicon, Modal, Table} from 'react-bootstrap';
import SelectEntity from './SelectEntity';
import UserRoles from './UserRoles';

class UserModal extends React.Component {
  constructor(props) {
    super(props);
    this.state = {user: {username: "", password: "", roles: []}, role: {},
    }
    this.isFormValid = this.isFormValid.bind(this);
    this.onChangeUsername = this.onChangeUsername.bind(this);
    this.onChangePassword = this.onChangePassword.bind(this);
    this.handleSelectRole = this.handleSelectRole.bind(this);
    this.handleAddRole = this.handleAddRole.bind(this);
  }

  isFormValid() {
    return false;
  }

  onChangeUsername(e) {
    const username = e.target.value;
    const user = _.assign({}, this.state.user, {username: username});
    this.setState({user: user});
  }

  onChangePassword(e) {
    const password = e.target.value;
    const user = _.assign({}, this.state.user, {password: password});
    this.setState({user: user});
  }

  handleSelectRole(e) {
    const id = parseInt(e.target.value);
    const roleIndex = _.findIndex(this.props.roles, (r) => (r.id === id) );
    const role = _.assign({}, this.props.roles[roleIndex]);
    this.setState({role: role});
  }

  handleAddRole() {
    if (undefined !== this.state.role) {
      // check for duplicate: a role must only appear once in the list
      const oldRoles = this.state.user.roles;
    }
    return;
  }

  componentDidMount() {
    this.setState({user: this.props.user});
  }

  render() {
    if (false === this.props.modalOpen) {
      return null;
    }
    return (
      <div>
        <Modal bsSize="small" show={this.props.modalOpen} onHide={() => this.props.close()}>
          <Modal.Header closeButton bsClass={this.props.header}>
            <Modal.Title>{this.props.title}</Modal.Title>
          </Modal.Header>
          <Modal.Body>
            <FormGroup controlId="formInput">
              <FormControl type="text" placeholder="username" onChange={this.onChangeUsername} value={this.state.user.username} />
            </FormGroup>
            <FormGroup controlId="formInput">
              <FormControl type="password" placeholder="password" onChange={this.onChangePassword} value={this.state.user.password} />
            </FormGroup>
            <FormGroup controlId="formSelect">
              <Table>
                <tbody>
                  <tr>
                    <td>
                      <SelectEntity
                        title="Role"
                        entityList={this.props.roles}
                        onSelect={this.handleSelectRole}
                        value={this.state.role}
                      />
                    </td>
                    <td>
                      <Button bsStyle="success" onClick={ () => this.handleAddRole() } title="add role"><Glyphicon glyph="plus"/></Button>
                    </td>
                  </tr>
                </tbody>
              </Table>
              <UserRoles
                roles={this.state.user.roles}
                delete={this.deleteRole}
              />
            </FormGroup>
          </Modal.Body>
          <Modal.Footer bsClass="modalFooter">
            <Button bsStyle="success" onClick={ () => this.props.save(this.state.user) }
              disabled={! this.isFormValid()} >Save</Button>
            <Button bsStyle="danger" onClick={ () => this.props.close() }>Cancel</Button>
          </Modal.Footer>

        </Modal>
      </div>
    );
  }
}
UserModal.PropTypes = {
  modalOpen: PropTypes.bool.isRequired,
  close: PropTypes.func.isRequired,
  title: PropTypes.string,
  header: PropTypes.string,
  save: PropTypes.func.isRequired,
  user: PropTypes.object,
  roles: PropTypes.array.isRequired,
}
UserModal.defaultProps = {
  title: "Add an user",
  header: "successModalHeader",
  user: {username: "", password: "", roles: []}
}
export default UserModal;
